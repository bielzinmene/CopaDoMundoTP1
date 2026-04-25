package br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("Sistema de Gestão da Copa 2026");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelNorte = new JPanel();
        painelNorte.setBackground(new Color(235, 235, 235));
        painelNorte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel titulo = new JLabel("MENU PRINCIPAL");
        titulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        painelNorte.add(titulo);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JButton btnUsuarios = new JButton("Gestão de Usuários");
        JButton btnSelecoes = new JButton("Gerenciar Seleções");
        JButton btnJogadores = new JButton("Gerenciar Jogadores");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair / Logout");

        btnSelecoes.addActionListener(e -> new GerenciarSelecaoView().setVisible(true));

        btnSair.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnSelecoes);
        painelBotoes.add(btnJogadores);
        painelBotoes.add(btnRelatorios);
        painelBotoes.add(new JLabel(""));
        painelBotoes.add(btnSair);

        add(painelNorte, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }
}