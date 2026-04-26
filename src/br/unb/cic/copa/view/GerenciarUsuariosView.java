package br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class GerenciarUsuariosView extends JFrame {
    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbTipo;

    public GerenciarUsuariosView() {
        setTitle("Gestão de Usuários - Copa 2026");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));

        add(new JLabel(" Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel(" Login:"));
        txtLogin = new JTextField();
        add(txtLogin);

        add(new JLabel(" Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel(" Tipo:"));
        cbTipo = new JComboBox<>(new String[] {"Administrador", "Operador", "Organizador"});
        add(cbTipo);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        // Volta para o menu
        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        add(btnSalvar);
        add(btnCancelar);
    }
}