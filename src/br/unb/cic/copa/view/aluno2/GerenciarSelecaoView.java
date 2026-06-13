package br.unb.cic.copa.view.aluno2;

import br.unb.cic.copa.model.aluno2.Selecao;
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

public class GerenciarSelecaoView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtGrupo;
    private JTextField txtTecnico;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final SelecaoController gerenciador = new SelecaoController(); // controller para acesso à lógica de negócio

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public GerenciarSelecaoView() {
        setTitle("Gerenciar Seleções - Copa do Mundo 2026");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        carregarTabela(""); // carrega todas as seleções ao abrir a tela
        setVisible(true);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Gerenciar Seleções");
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

        txtId      = criarCampo();
        txtId.setEditable(false);
        txtId.setBackground(new Color(230, 230, 240));
        txtNome    = criarCampo();
        txtGrupo   = criarCampo();
        txtTecnico = criarCampo();

        adicionarLinha(painel, gc, 0, "ID:",       txtId);
        adicionarLinha(painel, gc, 1, "Nome:",     txtNome);
        adicionarLinha(painel, gc, 2, "Grupo:",    txtGrupo);
        adicionarLinha(painel, gc, 3, "Técnico:",  txtTecnico);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Linha de busca
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar por nome:");
        labelBusca.setFont(FONTE_LABEL);
        labelBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabela(txtBusca.getText().trim())); // ao clicar, recarrega com filtro

        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela(txtBusca.getText().trim()); // filtro dinâmico ao digitar
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Grupo", "Técnico"}, 0) {
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
            if (!e.getValueIsAdjusting()) {
                carregarSelecaoSelecionada(); // ao selecionar linha, carrega dados no formulário
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 180));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirSelecao()); // ação de excluir a seleção selecionada

        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        painelExcluir.setBackground(COR_FUNDO);
        painelExcluir.add(btnExcluir);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelExcluir, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarTabela(String filtro) {
        modeloTabela.setRowCount(0); // limpa a tabela
        List<Selecao> todas = gerenciador.listarTodas(); // obtém todas as seleções do controller
        for (Selecao s : todas) { // percorre a lista
            if (filtro.isEmpty() || s.getNome().toLowerCase().contains(filtro.toLowerCase())) { // aplica filtro por nome
                modeloTabela.addRow(new Object[]{
                        s.getId(), s.getNome(), s.getGrupo(), s.getTecnico()
                });
            }
        }
    }

    private void carregarSelecaoSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            // preenche os campos com os dados da linha selecionada (para edição)
            txtId.setText(String.valueOf(modeloTabela.getValueAt(linha, 0)));
            txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
            txtGrupo.setText((String) modeloTabela.getValueAt(linha, 2));
            txtTecnico.setText((String) modeloTabela.getValueAt(linha, 3));
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
        JButton btnSalvarSel   = criarBotao("Salvar", COR_SALVAR);

        btnCancelar.addActionListener(e -> { // ação do botão Cancelar
            new MenuPrincipalView().setVisible(true); // retorna ao menu principal
            dispose(); // fecha a janela atual
        });

        btnSalvarSel.addActionListener(e -> salvarSelecao()); // ação do botão Salvar: chama metodo SalvarSelecao

        rodape.add(btnCancelar);
        rodape.add(btnSalvarSel);
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

    private void excluirSelecao() {
        int linha = tabela.getSelectedRow(); // linha selecionada na tabela
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma seleção na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir a seleção '" + nome + "' e todos os seus jogadores?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) { // usuário confirmou exclusão
            try {
                gerenciador.removerSelecao(nome); // chama controller para remover seleção (e desassociar jogadores)
                JOptionPane.showMessageDialog(this, "Seleção excluída com sucesso!");
                carregarTabela(txtBusca.getText().trim()); // recarrega a tabela
                limparFormulario(); // limpa os campos do formulário
            } catch (CopaException e) { // captura exceção de regra de negócio
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarSelecao() {
        try {
            String idStr = txtId.getText().trim();
            String nome = txtNome.getText().trim();
            String grupo = txtGrupo.getText().trim();
            String tecnico = txtTecnico.getText().trim();

            if (nome.isEmpty() || grupo.isEmpty() || tecnico.isEmpty()) { // valida campos obrigatórios
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (idStr.isEmpty()) {
                // Nova seleção
                Selecao nova = new Selecao(nome, grupo, tecnico);
                gerenciador.adicionarSelecao(nova); // chama controller para adicionar
                JOptionPane.showMessageDialog(this, "Seleção criada com sucesso!");
            } else {
                //edição – pega o nome antigo da linha selecionada antes de alterar os campos
                int linha = tabela.getSelectedRow();
                if (linha < 0) {
                    JOptionPane.showMessageDialog(this, "Selecione a seleção na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String nomeAntigo = (String) modeloTabela.getValueAt(linha, 1);
                gerenciador.editarSelecao(nomeAntigo, nome, grupo, tecnico); // chama controller para editar
                JOptionPane.showMessageDialog(this, "Seleção atualizada com sucesso!");
            }
            carregarTabela(""); // recarrega a tabela (limpa filtro)
            limparFormulario(); // limpa o formulário
        } catch (CopaException | IllegalArgumentException e) { // captura exceções de regras de negócio
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtId.setText("");
        txtNome.setText("");
        txtGrupo.setText("");
        txtTecnico.setText("");
        tabela.clearSelection(); // remove a seleção da tabela
    }
}