package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.UsuarioController;
import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno1.exception.*;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarUsuariosView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JTextField txtCpf;
    private JTextField txtPais;
    private JComboBox<String> cbFuncao;
    private JComboBox<String> cbStatus;
    private JTextField txtBusca;
    private JComboBox<String> cbFiltroPerfil;
    private JTextField txtFiltroPais;
    private JComboBox<String> cbFiltroStatus;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final Usuario usuarioLogado;
    private final UsuarioController controller = UsuarioController.getInstancia();


    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public GerenciarUsuariosView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Gestão de Usuários - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());


        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarTabela("", "", "", "");
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Gestão de Usuários");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);


        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa_3.png");
            if (imgUrl != null) {
                ImageIcon icone = new ImageIcon(imgUrl);
                Image img = icone.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        JPanel formPanel = criarFormulario();
        corpo.add(formPanel, BorderLayout.NORTH);

        JPanel central = new JPanel(new BorderLayout(0, 10));
        central.setBackground(COR_FUNDO);
        central.add(criarPainelBusca(), BorderLayout.NORTH);
        central.add(criarPainelTabela(), BorderLayout.CENTER);
        corpo.add(central, BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel principal = new JPanel(new GridLayout(1, 2, 15, 0));
        principal.setBackground(COR_FUNDO);
        principal.setBorder(BorderFactory.createTitledBorder("Dados do Usuário"));

        JPanel colunaEsquerda = new JPanel(new GridBagLayout());
        colunaEsquerda.setBackground(COR_FUNDO);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0;
        gc.weighty = 0;

        txtId   = criarCampoComPlaceholder("(gerado automaticamente)");
        txtNome = criarCampoComPlaceholder("Ex: João Silva");
        txtEmail = criarCampoComPlaceholder("Ex: joao@email.com");
        txtLogin = criarCampoComPlaceholder("Ex: joao.silva");
        txtSenha = new JPasswordField();
        txtSenha.setFont(FONTE_CAMPO);
        txtSenha.setPreferredSize(new Dimension(200, 32));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)));
        txtCpf  = criarCampoComPlaceholder("000.000.000-00");
        txtPais = criarCampoComPlaceholder("Ex: Brasil");

        cbFuncao = new JComboBox<>(new String[]{"Administrador", "Organizador", "Operador", "Arbitro"});
        cbFuncao.setPreferredSize(new Dimension(200, 32));
        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        cbStatus.setPreferredSize(new Dimension(200, 32));

        txtId.setEnabled(false);
        txtId.setText("(gerado automaticamente)");

        adicionarPar(colunaEsquerda, gc, "ID:", txtId);
        adicionarPar(colunaEsquerda, gc, "Nome:", txtNome);
        adicionarPar(colunaEsquerda, gc, "Email:", txtEmail);
        adicionarPar(colunaEsquerda, gc, "Login:", txtLogin);
        adicionarPar(colunaEsquerda, gc, "Senha:", txtSenha);

        JPanel colunaDireita = new JPanel(new GridBagLayout());
        colunaDireita.setBackground(COR_FUNDO);
        adicionarPar(colunaDireita, gc, "CPF:", txtCpf);
        adicionarPar(colunaDireita, gc, "País:", txtPais);
        adicionarPar(colunaDireita, gc, "Perfil:", cbFuncao);
        adicionarPar(colunaDireita, gc, "Status:", cbStatus);

        gc.weighty = 1.0;
        colunaDireita.add(Box.createVerticalGlue(), gc);

        principal.add(colunaEsquerda);
        principal.add(colunaDireita);

        return principal;
    }

    private void adicionarPar(JPanel painel, GridBagConstraints gc, String texto, JComponent componente) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0;
        gc.gridy = painel.getComponentCount() / 2;
        gc.weighty = 0;
        painel.add(label, gc);
        gc.gridx = 1;
        painel.add(componente, gc);
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createTitledBorder("Filtros de Busca"));

        JPanel filtros = new JPanel(new GridBagLayout());
        filtros.setBackground(COR_FUNDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.NONE;


        g.gridx = 0; g.gridy = 0; g.weightx = 0;
        filtros.add(new JLabel("Nome:"), g);
        g.gridx = 1; g.weightx = 0;
        txtBusca = criarCampoComPlaceholder("Nome do usuário");
        filtros.add(txtBusca, g);
        g.gridx = 2; g.weightx = 0;
        filtros.add(new JLabel("Perfil:"), g);
        g.gridx = 3; g.weightx = 0;
        cbFiltroPerfil = new JComboBox<>(new String[]{"Todos", "Administrador", "Organizador", "Operador", "Arbitro"});
        cbFiltroPerfil.setPreferredSize(new Dimension(150, 30));
        filtros.add(cbFiltroPerfil, g);


        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        filtros.add(new JLabel("País:"), g);
        g.gridx = 1; g.weightx = 0;
        txtFiltroPais = criarCampoComPlaceholder("País");
        filtros.add(txtFiltroPais, g);
        g.gridx = 2; g.weightx = 0;
        filtros.add(new JLabel("Status:"), g);
        g.gridx = 3; g.weightx = 0;
        cbFiltroStatus = new JComboBox<>(new String[]{"Todos", "Ativo", "Inativo"});
        cbFiltroStatus.setPreferredSize(new Dimension(150, 30));
        filtros.add(cbFiltroStatus, g);


        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        JButton btnLimparFiltros = criarBotao("Limpar Filtros", COR_BUSCA);
        botoes.add(btnBuscar);
        botoes.add(btnLimparFiltros);

        painel.add(filtros, BorderLayout.CENTER);
        painel.add(botoes, BorderLayout.SOUTH);


        btnBuscar.addActionListener(e -> aplicarFiltros());
        btnLimparFiltros.addActionListener(e -> {
            txtBusca.setText("");
            txtFiltroPais.setText("");
            cbFiltroPerfil.setSelectedIndex(0);
            cbFiltroStatus.setSelectedIndex(0);
            carregarTabela("", "", "", "");
        });
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { aplicarFiltros(); }
        });
        txtFiltroPais.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) { aplicarFiltros(); }
        });
        cbFiltroPerfil.addActionListener(e -> aplicarFiltros());
        cbFiltroStatus.addActionListener(e -> aplicarFiltros());

        return painel;
    }

    private JScrollPane criarPainelTabela() {
        configurarTabela();
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        return scroll;
    }

    private void configurarTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Nome", "Email", "Login", "Perfil", "País", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);


        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 248));
                }
                return c;
            }
        });

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                preencherFormulario();
            }
        });
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnLimpar   = criarBotao("Limpar Formulário", COR_BUSCA);
        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnExcluir  = criarBotao("Excluir", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        btnLimpar.addActionListener(e -> limparFormulario());
        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnSalvar.addActionListener(e -> salvarUsuario());

        rodape.add(btnLimpar);
        rodape.add(btnCancelar);
        rodape.add(btnExcluir);
        rodape.add(btnSalvar);
        return rodape;
    }

    private JTextField criarCampo(int largura) {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(largura, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)));
        return campo;
    }

    private JTextField criarCampoComPlaceholder(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(new Color(180, 180, 200));
                    g.setFont(getFont().deriveFont(Font.ITALIC));
                    g.drawString(placeholder, 8, getHeight() / 2 + getFont().getSize() / 2 - 2);
                }
            }
        };
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(200, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)));
        return campo;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(cor); }
        });
        return btn;
    }

    private void aplicarFiltros() {
        String nome   = txtBusca.getText().trim();
        String perfil = cbFiltroPerfil.getSelectedItem().equals("Todos") ? "" : (String) cbFiltroPerfil.getSelectedItem();
        String pais   = txtFiltroPais.getText().trim();
        String status = cbFiltroStatus.getSelectedItem().equals("Todos") ? "" : (String) cbFiltroStatus.getSelectedItem();
        carregarTabela(nome, perfil, pais, status);
    }

    private void carregarTabela(String nome, String perfil, String pais, String status) {
        modeloTabela.setRowCount(0);
        List<Usuario> lista = controller.pesquisar(nome, perfil, pais, status);
        for (Usuario u : lista) {
            modeloTabela.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getEmail(),
                    u.getLogin(), u.getFuncao(), u.getPais(), u.getStatus()
            });
        }
    }

    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        txtId.setText(String.valueOf(modeloTabela.getValueAt(linha, 0)));
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEmail.setText((String) modeloTabela.getValueAt(linha, 2));
        txtLogin.setText((String) modeloTabela.getValueAt(linha, 3));
        txtSenha.setText("");
        txtCpf.setText("");
        txtPais.setText((String) modeloTabela.getValueAt(linha, 5));
        cbFuncao.setSelectedItem(modeloTabela.getValueAt(linha, 4));
        cbStatus.setSelectedItem(modeloTabela.getValueAt(linha, 6));
    }

    private void salvarUsuario() {
        try {
            String nome   = txtNome.getText().trim();
            String email  = txtEmail.getText().trim();
            String login  = txtLogin.getText().trim();
            String senha  = new String(txtSenha.getPassword()).trim();
            String cpf    = txtCpf.getText().trim();
            String pais   = txtPais.getText().trim();
            String funcao = (String) cbFuncao.getSelectedItem();
            String status = (String) cbStatus.getSelectedItem();

            String idTexto = txtId.getText().trim();
            boolean editando = !idTexto.isEmpty() && !idTexto.equals("(gerado automaticamente)");

            if (nome.isEmpty() || email.isEmpty() || login.isEmpty() || pais.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, email, login e país são obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (editando && senha.isEmpty()) {
                int id = Integer.parseInt(idTexto);
                for (Usuario u : controller.listarUsuarios()) {
                    if (u.getId() == id) { senha = u.getSenha(); break; }
                }
            }
            if (editando && cpf.isEmpty()) {
                int id = Integer.parseInt(idTexto);
                for (Usuario u : controller.listarUsuarios()) {
                    if (u.getId() == id) { cpf = u.getCpf(); break; }
                }
            }
            if (!editando && (senha.isEmpty() || cpf.isEmpty())) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios no cadastro!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = editando ? Integer.parseInt(idTexto) : 0;
            Usuario usuario;
            switch (funcao) {
                case "Administrador": usuario = new Administrador(id, nome, email, login, senha, cpf, pais); break;
                case "Organizador":   usuario = new Organizador(id, nome, email, login, senha, cpf, pais); break;
                case "Operador":      usuario = new Operador(id, nome, email, login, senha, cpf, pais); break;
                default:              usuario = new Organizador(id, nome, email, login, senha, cpf, pais); break;
            }
            usuario.setStatus(status);

            if (editando) {
                controller.editarUsuario(usuarioLogado, usuario);
                JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
            } else {
                controller.cadastrarUsuario(usuarioLogado, usuario);
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            }
            limparFormulario();
            carregarTabela("", "", "", "");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o usuário '" + nome + "'?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                controller.excluirUsuario(usuarioLogado, id);
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                limparFormulario();
                carregarTabela("", "", "", "");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limparFormulario() {
        txtId.setText("(gerado automaticamente)");
        txtNome.setText("");
        txtEmail.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtCpf.setText("");
        txtPais.setText("");
        cbFuncao.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        tabela.clearSelection();
    }
}