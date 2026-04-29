package br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class VendaIngressoView extends JFrame {

    private JTextField txtComprador;
    private JComboBox<String> cbPartida;
    private JComboBox<String> cbCategoria;
    private JTextField txtQuantidade;

    public VendaIngressoView() {
        setTitle("Venda de Ingressos - Copa 2026");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        // Usuário (dps tem que relacionar ccom o cadastro, mas por enquanto vou deixar assim)
        add(new JLabel("Comprador:"));
        txtComprador = new JTextField();
        add(txtComprador);

        // Partida (depois eu vou fazer algo para preencher isso de acordo  com o cadastro das partidas)
        add(new JLabel("Partida:"));
        cbPartida = new JComboBox<>(new String[] {
                "Brasil x Argentina",
                "França x Alemanha"
        });
        add(cbPartida);

        // Categorias
        add(new JLabel("Categoria:"));
        cbCategoria = new JComboBox<>(new String[] {
                "VIP",
                "Padrão",
                "Meia"
        });
        add(cbCategoria);

        // Quantidade
        add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        add(txtQuantidade);

        // Botões
        JButton botaoVender = new JButton("Vender");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoCancelar.addActionListener(e -> dispose());

        add(botaoVender);
        add(botaoCancelar);

        // Ação do botão vender (simples por enquanto. Em breve a lógica vai ser implementada)
        botaoVender.addActionListener(e -> venderIngresso());
    }

    private void venderIngresso() {
        try {
            String comprador = txtComprador.getText();
            String partida = (String) cbPartida.getSelectedItem();
            String categoria = (String) cbCategoria.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            JOptionPane.showMessageDialog(this,
                    "Venda realizada!\n" +
                            "Comprador: " + comprador +
                            "\nPartida: " + partida +
                            "\nCategoria: " + categoria +
                            "\nQuantidade: " + quantidade
            );

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade inválida!",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}