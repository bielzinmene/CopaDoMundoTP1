package br.unb.cic.copa.view.aluno1;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;

    public LoginView() {
        setTitle("Copa do Mundo 2026");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelNorte = new JPanel();
        painelNorte.setBackground(new Color(235, 235, 235));
        painelNorte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel lblTitulo = new JLabel("LOGIN - COPA DO MUNDO");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        painelNorte.add(lblTitulo);

        JPanel painelContainer = new JPanel(new GridBagLayout());

        JPanel formulario = new JPanel(new GridLayout(2, 2, 10, 20));

        formulario.add(new JLabel("Login:"));
        txtLogin = new JTextField(15);
        formulario.add(txtLogin);

        formulario.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField(15);
        formulario.add(txtSenha);

        painelContainer.add(formulario);

        JPanel painelSul = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        JButton btnEntrar = new JButton("Acessar");
        JButton btnSair = new JButton("Sair");

        btnEntrar.setPreferredSize(new Dimension(100, 30));
        btnSair.setPreferredSize(new Dimension(100, 30));

        btnEntrar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            this.dispose();
        });

        btnSair.addActionListener(e -> System.exit(0));

        painelSul.add(btnEntrar);
        painelSul.add(btnSair);

        add(painelNorte, BorderLayout.NORTH);
        add(painelContainer, BorderLayout.CENTER);
        add(painelSul, BorderLayout.SOUTH);
    }
}