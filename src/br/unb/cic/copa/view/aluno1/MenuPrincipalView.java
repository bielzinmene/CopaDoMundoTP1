package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.view.aluno2.GerenciarJogadorView;
import br.unb.cic.copa.view.aluno2.GerenciarSelecaoView;
import br.unb.cic.copa.view.aluno3.GerenciarArbitroView;
import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    public MenuPrincipalView() {
        setTitle("Sistema de Gestão da Copa 2026");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelNorte = new JPanel();
        painelNorte.setBackground(new Color(235, 235, 235));
        painelNorte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel titulo = new JLabel("MENU PRINCIPAL");
        titulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        painelNorte.add(titulo);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 3, 20, 20));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JButton btnUsuarios = new JButton("Gestão de Usuários");
        JButton btnSelecoes = new JButton("Gerenciar Seleções");
        JButton btnArbitros = new JButton("Gerenciar Arbitros");
        JButton btnEstadios = new JButton("Gerenciar Estádios");
        JButton btnJogadores = new JButton("Gerenciar Jogadores");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair / Logout");

        btnUsuarios.addActionListener(e -> {
            new GerenciarUsuariosView().setVisible(true);
            dispose();
        });

        btnSelecoes.addActionListener(e -> {
            new GerenciarSelecaoView().setVisible(true);
            dispose();
        });

        btnArbitros.addActionListener(e->{
            new GerenciarArbitroView().setVisible(true);
            dispose();
        });

        btnEstadios.addActionListener(e -> {
            new GerenciarEstadioView().setVisible(true);
            dispose();
        });

        btnJogadores.addActionListener(e -> {
            new GerenciarJogadorView().setVisible(true);
            dispose();
        });

        btnRelatorios.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Relatórios em desenvolvimento");
        });

        btnSair.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });

        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnSelecoes);
        painelBotoes.add(btnArbitros);
        painelBotoes.add(btnEstadios);
        painelBotoes.add(btnJogadores);
        painelBotoes.add(btnRelatorios);
        painelBotoes.add(new JLabel(""));
        painelBotoes.add(btnSair);

        add(painelNorte, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }
}