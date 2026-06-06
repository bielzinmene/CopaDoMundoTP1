package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.UsuarioController;
import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno1.exception.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
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
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // Usuário logado — passado pelo menu
    private final Usuario usuarioLogado;
    private final UsuarioController controller = UsuarioController.getInstancia();

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = new Color(30, 60, 120);
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
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarTabela("", "");
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("👤  Gestão de Usuários");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

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
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;

        txtId     = criarCampo();
        txtNome   = criarCampo();
        txtEmail  = criarCampo();
        txtLogin  = criarCampo();
        txtSenha  = new JPasswordField();
        txtSenha.setFont(FONTE_CAMPO);
        txtSenha.setPreferredSize(new Dimension(250, 32));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)));
        txtCpf    = criarCampo();
        txtPais   = criarCampo();

        cbFuncao = new JComboBox<>(new String[]{
                "Administrador", "Organizador", "Operador", "Arbitro"
        });
        cbFuncao.setFont(FONTE_CAMPO);
        cbFuncao.setPreferredSize(new Dimension(250, 32));

        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo"});
        cbStatus.setFont(FONTE_CAMPO);
        cbStatus.setPreferredSize(new Dimension(250, 32));

        // ID fica desabilitado — gerado automaticamente
        txtId.setEnabled(false);
        txtId.setText("(gerado automaticamente)");

        adicionarLinha(painel, gc, 0, "ID:", txtId);
        adicionarLinha(painel, gc, 1, "Nome:", txtNome);
        adicionarLinha(painel, gc, 2, "Email:", txtEmail);
        adicionarLinha(painel, gc, 3, "Login:", txtLogin);
        adicionarLinha(painel, gc, 4, "Senha:", txtSenha);
        adicionarLinha(painel, gc, 5, "CPF:", txtCpf);
        adicionarLinha(painel, gc, 6, "País:", txtPais);
        adicionarLinhaCombo(painel, gc, 7, "Perfil:", cbFuncao);
        adicionarLinhaCombo(painel, gc, 8, "Status:", cbStatus);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Linha de busca
        JPanel linhaBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel lblBusca = new JLabel("Buscar:");
        lblBusca.setFont(FONTE_LABEL);
        lblBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo();
        txtBusca.setPreferredSize(new Dimension(180, 32));

        cbFiltroPerfil = new JComboBox<>(new String[]{
                "Todos", "Administrador", "Organizador", "Operador", "Arbitro"
        });
        cbFiltroPerfil.setFont(FONTE_CAMPO);
        cbFiltroPerfil.setPreferredSize(new Dimension(150, 32));

        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabela(
                txtBusca.getText().trim(),
                cbFiltroPerfil.getSelectedItem().equals("Todos") ? "" :
                        (String) cbFiltroPerfil.getSelectedItem()
        ));

        // Busca em tempo real ao digitar
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela(
                        txtBusca.getText().trim(),
                        cbFiltroPerfil.getSelectedItem().equals("Todos") ? "" :
                                (String) cbFiltroPerfil.getSelectedItem()
                );
            }
        });

        linhaBusca.add(lblBusca);
        linhaBusca.add(txtBusca);
        linhaBusca.add(cbFiltroPerfil);
        linhaBusca.add(btnBuscar);

        // Tabela de usuários
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
        tabela.setSelectionBackground(new Color(200, 220, 255));

        // Clique na tabela preenche o formulário para edição
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                preencherFormulario();
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 180));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirUsuario());

        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        painelExcluir.setBackground(COR_FUNDO);
        painelExcluir.add(btnExcluir);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelExcluir, BorderLayout.SOUTH);

        return painel;
    }

    // Carrega a tabela com filtros de nome e perfil
    private void carregarTabela(String nome, String perfil) {
        modeloTabela.setRowCount(0);
        List<Usuario> lista = controller.pesquisar(nome, perfil, null, null);
        for (Usuario u : lista) {
            modeloTabela.addRow(new Object[]{
                    u.getId(), u.getNome(), u.getEmail(),
                    u.getLogin(), u.getFuncao(), u.getPais(), u.getStatus()
            });
        }
    }

    // Preenche o formulário com os dados do usuário selecionado na tabela
    private void preencherFormulario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;

        txtId.setText(String.valueOf(modeloTabela.getValueAt(linha, 0)));
        txtNome.setText((String) modeloTabela.getValueAt(linha, 1));
        txtEmail.setText((String) modeloTabela.getValueAt(linha, 2));
        txtLogin.setText((String) modeloTabela.getValueAt(linha, 3));
        txtSenha.setText("");
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

            if (nome.isEmpty() || email.isEmpty() || login.isEmpty() ||
                    senha.isEmpty() || cpf.isEmpty() || pais.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos os campos são obrigatórios!",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Verifica se é edição ou cadastro novo
            String idTexto = txtId.getText().trim();
            boolean editando = !idTexto.isEmpty() &&
                    !idTexto.equals("(gerado automaticamente)");

            // PADRÃO FACTORY — cria o tipo certo baseado na função escolhida
            Usuario usuario;
            int id = editando ? Integer.parseInt(idTexto) : 0;
            switch (funcao) {
                case "Administrador":
                    usuario = new Administrador(id, nome, email, login, senha, cpf, pais);
                    break;
                case "Organizador":
                    usuario = new Organizador(id, nome, email, login, senha, cpf, pais);
                    break;
                case "Operador":
                    usuario = new Operador(id, nome, email, login, senha, cpf, pais);
                    break;
                default:
                    usuario = new Organizador(id, nome, email, login, senha, cpf, pais);
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
            carregarTabela("", "");

        } catch (AcessoNegadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Acesso Negado", JOptionPane.ERROR_MESSAGE);
        } catch (SenhaFracaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Senha Inválida", JOptionPane.ERROR_MESSAGE);
        } catch (EmailInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Email Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (CpfInvalidoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "CPF Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioDuplicadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Usuário Duplicado", JOptionPane.ERROR_MESSAGE);
        } catch (UsuarioNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirUsuario() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o usuário '" + nome + "'?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                controller.excluirUsuario(usuarioLogado, id);
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso!");
                limparFormulario();
                carregarTabela("", "");
            } catch (AcessoNegadoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Acesso Negado", JOptionPane.ERROR_MESSAGE);
            } catch (UsuarioNaoEncontradoException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
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

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                new Color(200, 200, 220)));

        JButton btnLimpar   = criarBotao("Limpar", COR_BUSCA);
        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        btnLimpar.addActionListener(e -> limparFormulario());

        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });

        btnSalvar.addActionListener(e -> salvarUsuario());

        rodape.add(btnLimpar);
        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc,
                                int linha, String labelTxt, JComponent campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));

        gc.gridx = 0; gc.gridy = linha;
        gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7;
        gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private void adicionarLinhaCombo(JPanel painel, GridBagConstraints gc,
                                     int linha, String labelTxt, JComboBox<?> combo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));

        gc.gridx = 0; gc.gridy = linha;
        gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7;
        gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(combo, gc);
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
}