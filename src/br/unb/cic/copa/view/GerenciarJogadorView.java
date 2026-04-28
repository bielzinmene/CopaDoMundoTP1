package br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class GerenciarJogadorView extends JFrame {
    private JTextField txtNome;
    private JTextField txtNumero;
    private JComboBox<String> cbPosicao;
    private JTextField txtSelecao;

    public GerenciarJogadorView() {
        setTitle("Gerenciar Jogadores - Copa 2026");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("Número da Camisa:"));
        txtNumero = new JTextField();
        add(txtNumero);

        add(new JLabel("Posição:"));
        cbPosicao = new JComboBox<>(new String[] {"Goleiro", "Defensor", "Meio-campo", "Atacante"});
        add(cbPosicao);

        add(new JLabel("Seleção:"));
        txtSelecao = new JTextField();
        add(txtSelecao);

        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoCancelar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
                });
        botaoSalvar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });


        add(botaoSalvar);
        add(botaoCancelar);

    }
}
