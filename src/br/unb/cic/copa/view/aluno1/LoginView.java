package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.UsuarioController;
import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;

    // Cores e fontes no mesmo padrão da Aluno 3
    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = new Color(30, 60, 120);
    private static final Color COR_ENTRAR    = new Color(34, 139, 34);
    private static final Color COR_SAIR      = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public LoginView() {
        setTitle("Copa do Mundo 2026 — Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("⚽  Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 5, 8, 5);
        gc.anchor = GridBagConstraints.WEST;

        // Campo Login
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(FONTE_LABEL);
        lblLogin.setForeground(new Color(50, 50, 80));

        txtLogin = new JTextField();
        txtLogin.setFont(FONTE_CAMPO);
        txtLogin.setPreferredSize(new Dimension(250, 32));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));

        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.3;
        formulario.add(lblLogin, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(txtLogin, gc);

        // Campo Senha
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(FONTE_LABEL);
        lblSenha.setForeground(new Color(50, 50, 80));

        txtSenha = new JPasswordField();
        txtSenha.setFont(FONTE_CAMPO);
        txtSenha.setPreferredSize(new Dimension(250, 32));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));

        gc.gridx = 0; gc.gridy = 1; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        formulario.add(lblSenha, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(txtSenha, gc);

        corpo.add(formulario);
        return corpo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnSair   = criarBotao("Sair", COR_SAIR);
        JButton btnEntrar = criarBotao("Acessar", COR_ENTRAR);

        btnSair.addActionListener(e -> System.exit(0));

        btnEntrar.addActionListener(e -> realizarLogin());

        // Permite logar pressionando Enter na senha
        txtSenha.addActionListener(e -> realizarLogin());

        rodape.add(btnSair);
        rodape.add(btnEntrar);
        return rodape;
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha login e senha!",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuarioLogado = UsuarioController.getInstancia()
                .autenticar(login, senha);

        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this,
                    "Login ou senha incorretos!",
                    "Erro de autenticação",
                    JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
        } else {
            // NOVO: salva na sessão global para todas as telas acessarem
            SessaoUsuario.getInstancia().setUsuarioLogado(usuarioLogado);
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        }
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