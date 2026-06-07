package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.UsuarioController;
import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.view.aluno3.ConsultarEscalaArbitroView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;


    private static final Color COR_FUNDO     = new Color(240, 242, 245);
    private static final Color COR_HEADER    = new Color(26, 53, 98);
    private static final Color COR_ENTRAR    = new Color(34, 139, 34);
    private static final Color COR_ENTRAR_HOVER = new Color(28, 115, 28);
    private static final Color COR_SAIR      = new Color(180, 40, 40);
    private static final Color COR_SAIR_HOVER = new Color(150, 30, 30);
    private static final Color COR_TEXTO_BTN = Color.WHITE;

    private static final Font FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 18);

    public LoginView() {
        setTitle("Copa do Mundo 2026 — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // TELA CHEIA
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("  Copa do Mundo 2026  ");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel central = new JPanel(new GridBagLayout());
        central.setBackground(COR_FUNDO);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(COR_FUNDO);
        formulario.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 8, 10, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;

        // Login
        JLabel lblLogin = new JLabel("Login:");
        lblLogin.setFont(FONTE_LABEL);
        lblLogin.setForeground(new Color(50, 50, 80));

        txtLogin = new JTextField();
        txtLogin.setFont(FONTE_CAMPO);
        txtLogin.setPreferredSize(new Dimension(250, 34));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 10, 4, 10)
        ));

        gc.gridx = 0; gc.gridy = 0;
        formulario.add(lblLogin, gc);
        gc.gridx = 1;
        formulario.add(txtLogin, gc);

        // Senha
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(FONTE_LABEL);
        lblSenha.setForeground(new Color(50, 50, 80));

        txtSenha = new JPasswordField();
        txtSenha.setFont(FONTE_CAMPO);
        txtSenha.setPreferredSize(new Dimension(250, 34));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 10, 4, 10)
        ));

        gc.gridx = 0; gc.gridy = 1;
        formulario.add(lblSenha, gc);
        gc.gridx = 1;
        formulario.add(txtSenha, gc);

        central.add(formulario);
        return central;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnSair   = criarBotao("Sair", COR_SAIR, COR_SAIR_HOVER);
        JButton btnEntrar = criarBotao("Acessar", COR_ENTRAR, COR_ENTRAR_HOVER);

        btnSair.addActionListener(e -> System.exit(0));
        btnEntrar.addActionListener(e -> realizarLogin());
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
            SessaoUsuario.getInstancia().setUsuarioLogado(usuarioLogado);

            if ("Arbitro".equals(usuarioLogado.getFuncao())) {
                new ConsultarEscalaArbitroView().setVisible(true);
            } else {
                new MenuPrincipalView(usuarioLogado).setVisible(true);
            }
            dispose();
        }
    }

    private JButton criarBotao(String texto, Color corNormal, Color corHover) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(corNormal);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 38));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(8, 15, 8, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(corHover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) btn.setBackground(corNormal);
            }
        });

        return btn;
    }
}