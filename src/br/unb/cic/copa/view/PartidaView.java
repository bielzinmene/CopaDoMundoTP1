package CopaDoMundoTP1.src.br.unb.cic.copa.view;

import javax.swing.*;
import java.awt.*;

public class PartidaView extends JFrame {

    public PartidaView() {
        setTitle("Gestão de Partidas");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel cadastro = new JPanel(new GridLayout(6, 2));

        cadastro.add(new JLabel("Seleção 1:"));
        cadastro.add(new JTextField());

        cadastro.add(new JLabel("Seleção 2:"));
        cadastro.add(new JTextField());

        cadastro.add(new JLabel("Fase:"));
        cadastro.add(new JComboBox<>(new String[]{"Grupos", "Oitavas", "Quartas", "Semi-Final", "Final"}));

        cadastro.add(new JLabel("Data:"));
        cadastro.add(new JTextField());

        cadastro.add(new JLabel("Estádio:"));
        cadastro.add(new JTextField());

        cadastro.add(new JLabel(""));
        cadastro.add(new JButton("Cadastrar"));

        JTable tabela = new JTable(
                new Object[][]{},
                new String[]{"Seleção 1", "Seleção 2", "Fase"}
        );

        add(cadastro, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel resultado = new JPanel();

        resultado.add(new JLabel("Gols 1:"));
        resultado.add(new JTextField(3));

        resultado.add(new JLabel("Gols 2:"));
        resultado.add(new JTextField(3));

        resultado.add(new JButton("Registrar Resultado"));

        add(resultado, BorderLayout.SOUTH);
    }
}