package br.unb.cic.copa.view.aluno2;

import br.unb.cic.copa.model.aluno2.*;
import br.unb.cic.copa.controller.aluno2.SelecaoController;
import br.unb.cic.copa.model.aluno2.exception.CopaException;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarJogadorView extends JFrame {

    private JComboBox<String> comboSelecao;
    private JTextField txtNome;
    private JTextField txtNumero;
    private JComboBox<Posicao> comboPosicao;
    private JComboBox<StatusJogador> comboStatus;
    private JCheckBox chkTitular;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtBusca;

    private final SelecaoController gerenciador = new SelecaoController();
    private Selecao selecaoAtual;

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public GerenciarJogadorView() {
        setTitle("Gerenciar Jogadores - Copa do Mundo 2026");
        setExtendedState(JFrame.MAXIMIZED_BOTH);        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        try{
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if(imgURL != null) setIconImage(new ImageIcon(imgURL).getImage());
        }catch (Exception ignored){}

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarComboSelecoes();
        setVisible(true);
    }


    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Gerenciar Jogadores");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);
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
        corpo.add(criarPainelBusca(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        comboSelecao = new JComboBox<>();
        comboSelecao.setFont(FONTE_CAMPO);
        comboSelecao.setPreferredSize(new Dimension(250, 32));
        comboSelecao.addActionListener(e -> carregarTabelaJogadores());

        txtNome   = criarCampo();
        txtNumero = criarCampo();
        comboPosicao = new JComboBox<>(Posicao.values());
        comboPosicao.setFont(FONTE_CAMPO);
        comboPosicao.setPreferredSize(new Dimension(250, 32));
        comboStatus = new JComboBox<>(StatusJogador.values());
        comboStatus.setFont(FONTE_CAMPO);
        comboStatus.setPreferredSize(new Dimension(250, 32));
        chkTitular = new JCheckBox("Titular");
        chkTitular.setFont(FONTE_LABEL);
        chkTitular.setBackground(COR_FUNDO);

        adicionarLinhaCombo(painel, gc, 0, "Seleção:", comboSelecao);
        adicionarLinha(painel, gc, 1, "Nome do Jogador:", txtNome);
        adicionarLinha(painel, gc, 2, "Número da Camisa:", txtNumero);
        adicionarLinhaCombo(painel, gc, 3, "Posição:", comboPosicao);
        adicionarLinhaCombo(painel, gc, 4, "Status:", comboStatus);
        adicionarCheckBox(painel, gc, 5, chkTitular);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Busca por nome
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar jogador por nome:");
        labelBusca.setFont(FONTE_LABEL);
        labelBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabelaJogadores());

        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabelaJogadores();
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Número", "Posição", "Titular", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
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
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarJogadorSelecionado();
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 200));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirJogador());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.add(btnExcluir);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarComboSelecoes() {
        comboSelecao.removeAllItems();
        for (Selecao s : gerenciador.listarTodas()) {
            comboSelecao.addItem(s.getNome());
        }
        if (comboSelecao.getItemCount() > 0) {
            comboSelecao.setSelectedIndex(0);
            carregarTabelaJogadores();
        }
    }

    private void carregarTabelaJogadores() {
        modeloTabela.setRowCount(0);
        String nomeSelecao = (String) comboSelecao.getSelectedItem();
        if (nomeSelecao == null) return;
        selecaoAtual = gerenciador.buscarSelecaoPorNome(nomeSelecao);
        if (selecaoAtual == null) return;

        List<Jogador> jogadores = selecaoAtual.getJogadores();
        String filtro = txtBusca.getText().trim().toLowerCase();

        for (Jogador j : jogadores) {
            if (filtro.isEmpty() || j.getNome().toLowerCase().contains(filtro)) {
                modeloTabela.addRow(new Object[]{
                        j.getNome(),
                        j.getNumeracao(),
                        j.getPosicao(),
                        j.isTitular() ? "Sim" : "Não",
                        j.getStatus()
                });
            }
        }
    }

    private void carregarJogadorSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || selecaoAtual == null) return;

        String nome = (String) modeloTabela.getValueAt(linha, 0);
        Jogador j = null;
        for (Jogador jog : selecaoAtual.getJogadores()) {
            if (jog.getNome().equals(nome)) {
                j = jog;
                break;
            }
        }
        if (j != null) {
            txtNome.setText(j.getNome());
            txtNumero.setText(String.valueOf(j.getNumeracao()));
            comboPosicao.setSelectedItem(j.getPosicao());
            chkTitular.setSelected(j.isTitular());
            comboStatus.setSelectedItem(j.getStatus());
        }
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JTextField campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private void adicionarLinhaCombo(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JComboBox<?> combo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(combo, gc);
    }

    private void adicionarCheckBox(JPanel painel, GridBagConstraints gc, int linha, JCheckBox chk) {
        gc.gridx = 1; gc.gridy = linha; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(chk, gc);
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

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnSalvarJog   = criarBotao("Salvar", COR_SALVAR);

        getRootPane().setDefaultButton(btnSalvarJog);

        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        btnSalvarJog.addActionListener(e -> salvarJogador());

        rodape.add(btnCancelar);
        rodape.add(btnSalvarJog);
        return rodape;
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

    private void excluirJogador() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um jogador na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nomeJogador = (String) modeloTabela.getValueAt(linha, 0);
        String nomeSelecao = (String) comboSelecao.getSelectedItem();
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o jogador '" + nomeJogador + "' da seleção " + nomeSelecao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                gerenciador.removerJogador(nomeSelecao, nomeJogador);
                JOptionPane.showMessageDialog(this, "Jogador excluído com sucesso!");
                carregarTabelaJogadores();
                limparFormulario();
            } catch (CopaException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarJogador() {
        String nomeSelecao = (String) comboSelecao.getSelectedItem();
        if (nomeSelecao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma seleção.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = txtNome.getText().trim();
        String numStr = txtNumero.getText().trim();
        Posicao posicao = (Posicao) comboPosicao.getSelectedItem();
        StatusJogador status = (StatusJogador) comboStatus.getSelectedItem();
        boolean titular = chkTitular.isSelected();

        if (nome.isEmpty() || numStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e número são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número deve ser inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int linha = tabela.getSelectedRow();
        try {
            if (linha >= 0) {
                // Editar jogador existente
                String nomeAntigo = (String) modeloTabela.getValueAt(linha, 0);
                gerenciador.editarJogador(nomeSelecao, nomeAntigo, nome, numero, posicao, titular, status);
            } else {
                // Adicionar novo jogador
                Jogador novoJogador = new Jogador(nome, numero, posicao, titular);
                novoJogador.setStatus(status);
                gerenciador.adicionarJogador(nomeSelecao, novoJogador);
            }
            JOptionPane.showMessageDialog(this, "Jogador salvo com sucesso!");
            carregarTabelaJogadores();
            limparFormulario();
        } catch (CopaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtNumero.setText("");
        comboPosicao.setSelectedIndex(0);
        chkTitular.setSelected(false);
        comboStatus.setSelectedIndex(0);
        tabela.clearSelection();
    }
}