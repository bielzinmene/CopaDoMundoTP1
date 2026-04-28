package br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class GerenciarIngressoView extends JFrame{
    private JTextField txtNome;
    private JTextField txtLocalizacao;
    private JTextField txtCapacidade;

    public GerenciarIngressoView(){
        setTitle("Gerenciar Estádios - Copa do Mundo 2026");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("Localização:"));
        txtLocalizacao = new JTextField();
        add(txtLocalizacao);

        add(new JLabel("Capacidade:"));
        txtCapacidade = new JTextField();
        add(txtCapacidade);

        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoCancelar.addActionListener(e -> dispose());

        add(botaoSalvar);
        add(botaoCancelar);

    }
}
