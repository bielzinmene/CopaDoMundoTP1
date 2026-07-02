package br.unb.cic.copa.view.aluno5;

import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.controller.aluno5.IngressosController;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno5.CategoriaIngresso;
import br.unb.cic.copa.model.aluno5.Venda;
import br.unb.cic.copa.model.aluno5.exception.VendaIngressoException;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VendaIngressoView extends JFrame {

    private JTextField txtComprador;
    private JTextField txtQuantidade;
    private JTextField txtBusca;

    private JComboBox<Partida> cbPartida;
    private JComboBox<CategoriaIngresso> cbCategoria;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final IngressosController controller;
    private final PartidaController partidaController;

    // Cores e fontes padronizadas
    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public VendaIngressoView() {
        controller = new IngressosController();
        partidaController = new PartidaController();

        setTitle("Gerenciar Ingressos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        //tenta carregar o ícone da janela da taça
        try{
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if(imgURL != null) setIconImage(new ImageIcon(imgURL).getImage()); // Aplica o ícone
        }catch (Exception ignored){} //se falhar, ignora o erro - não impede a execução

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarPartidas();
        carregarTabela();
        setVisible(true);

    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Gerenciar Ingressos");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        // Tenta carregar a imagem da taça
        try{
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa_3.png");
            if(imgURL != null){
                ImageIcon icone = new ImageIcon(imgURL);
                Image img = icone.getImage().getScaledInstance(50,60,Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon((img)));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        corpo.add(criarFormulario(), BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        txtComprador = criarCampo();
        txtQuantidade = criarCampo();

        cbPartida = new JComboBox<>();
        cbPartida.setFont(FONTE_CAMPO);
        cbPartida.setPreferredSize(new Dimension(250, 32));

        cbCategoria = new JComboBox<>(CategoriaIngresso.values());
        cbCategoria.setFont(FONTE_CAMPO);
        cbCategoria.setPreferredSize(new Dimension(250, 32));

        adicionarLinha(painel, gc, 1, "Comprador:", txtComprador);

        // Partida
        JLabel lblPartida = new JLabel("Partida:");
        lblPartida.setFont(FONTE_LABEL);
        lblPartida.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = 2;
        painel.add(lblPartida, gc);
        gc.gridx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(cbPartida, gc);

        // Categoria
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(FONTE_LABEL);
        lblCategoria.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = 3;
        painel.add(lblCategoria, gc);
        gc.gridx = 1;
        painel.add(cbCategoria, gc);

        adicionarLinha(painel, gc, 4, "Quantidade:", txtQuantidade);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Linha de busca
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar por comprador:");
        labelBusca.setFont(FONTE_LABEL);
        labelBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabela());

        // Busca em tempo real
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela();
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Tabela
        modeloTabela = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Comprador",
                        "Partida",
                        "Qtd Ingressos",
                        "Valor Total"
                },
                0
        );


        tabela = new JTable(modeloTabela);
        tabela.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(280);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(!isSelected){
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240,240,248));
                }
                return c;
            }
        });


        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 180));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirIngresso());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.add(btnExcluir);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnLimpar   = criarBotao("Limpar", COR_BUSCA);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        btnLimpar.addActionListener(e -> limparFormulario());

        btnSalvar.addActionListener(e -> salvarIngresso());

        rodape.add(btnCancelar);
        rodape.add(btnLimpar);
        rodape.add(btnSalvar);
        return rodape;
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(250, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return campo;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc, int linha, String texto, JTextField campo) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0;
        gc.gridy = linha;
        gc.weightx = 0.3;
        gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);
        gc.gridx = 1;
        gc.weightx = 0.7;
        gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private void carregarPartidas() {
        cbPartida.removeAllItems();
        List<Partida> partidas = partidaController.listarTodas();
        for (Partida p : partidas) {
            cbPartida.addItem(p);
        }
        if (cbPartida.getItemCount() > 0) {
            cbPartida.setSelectedIndex(0);
        }
    }

    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        String filtro = txtBusca.getText().trim().toLowerCase();

        for (Venda venda : controller.listarVendas()) {

            if (filtro.isEmpty()
                    || venda.getComprador().toLowerCase().contains(filtro)) {

                Partida partida =
                        partidaController.buscarPorId(
                                venda.getPartidaId()
                        );

                String descricaoPartida = "";

                if (partida != null) {

                    descricaoPartida =
                            partida.getSelecao1().getNome()
                                    + " x "
                                    + partida.getSelecao2().getNome()
                                    + " - "
                                    + partida.getData();
                }

                modeloTabela.addRow(
                        new Object[]{
                                venda.getId(),
                                venda.getComprador(),
                                descricaoPartida,
                                venda.getQuantidadeIngressos(),
                                venda.getValorTotal()
                        }
                );
            }
        }
    }



    private void salvarIngresso() {
        try {
            String comprador = txtComprador.getText().trim();
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            Partida partida = (Partida) cbPartida.getSelectedItem();
            CategoriaIngresso categoria = (CategoriaIngresso) cbCategoria.getSelectedItem();

            if (comprador.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Comprador é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade deve ser maior que zero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            controller.registrarVenda(comprador, partida, categoria, quantidade);
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (VendaIngressoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirIngresso() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir a venda de ID " + id + "?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                controller.excluirVenda(id);
                JOptionPane.showMessageDialog(this, "Venda removida com sucesso!");
                carregarTabela();
                limparFormulario();
            } catch (VendaIngressoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {

        txtComprador.setText("");
        txtQuantidade.setText("");

        if (cbPartida.getItemCount() > 0) {
            cbPartida.setSelectedIndex(0);
        }

        cbCategoria.setSelectedIndex(0);
        tabela.clearSelection();
    }
}