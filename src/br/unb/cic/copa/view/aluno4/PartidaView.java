package br.unb.cic.copa.view.aluno4;

import br.unb.cic.copa.view.aluno1.MenuPrincipalView;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.model.aluno4.exception.PartidaInvalidaException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PartidaView extends JFrame {

    private PartidaController partidaController;

    private final Color corAzulEscuro = new Color(28, 56, 121);
    private final Color corVerde = new Color(39, 174, 96);
    private final Color corVermelha = new Color(192, 57, 43);

    public PartidaView() {
        partidaController = new PartidaController();

        setTitle("Gestão de Partidas - Copa do Mundo 2026");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel bannerTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bannerTop.setBackground(corAzulEscuro);
        bannerTop.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel tituloLabel = new JLabel("⚽ Gestão de Partidas");
        tituloLabel.setForeground(Color.WHITE);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        bannerTop.add(tituloLabel);

        add(bannerTop, BorderLayout.NORTH);


        JPanel painelCentral = new JPanel(new BorderLayout(0, 20));
        painelCentral.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel cadastro = new JPanel(new GridLayout(6, 2, 10, 15));

        JTextField campoSelecao1 = new JTextField();
        JTextField campoSelecao2 = new JTextField();
        JComboBox<String> comboFase = new JComboBox<>(new String[]{"Grupos", "Oitavas", "Quartas", "Semi-Final", "Final"});
        JTextField campoData = new JTextField();
        JTextField campoEstadio = new JTextField();
        JTextField campoIdArbitro = new JTextField();

        cadastro.add(new JLabel("Seleção 1 (ID ou Nome):"));
        cadastro.add(campoSelecao1);
        cadastro.add(new JLabel("Seleção 2 (ID ou Nome):"));
        cadastro.add(campoSelecao2);
        cadastro.add(new JLabel("Fase:"));
        cadastro.add(comboFase);
        cadastro.add(new JLabel("Data:"));
        cadastro.add(campoData);
        cadastro.add(new JLabel("Estádio (ID ou Nome):"));
        cadastro.add(campoEstadio);
        cadastro.add(new JLabel("ID do Árbitro:"));
        cadastro.add(campoIdArbitro);

        painelCentral.add(cadastro, BorderLayout.NORTH);


        String[] colunas = {"Seleção 1", "Seleção 2", "Fase", "Data", "Estádio"};
        DefaultTableModel modeloTabela = new DefaultTableModel(new Object[][]{}, colunas);

        // Passando o modelo para a tabela
        JTable tabela = new JTable(modeloTabela);

        tabela.getTableHeader().setBackground(corAzulEscuro);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabela.setRowHeight(25);

        painelCentral.add(new JScrollPane(tabela), BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);


        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBorder(new EmptyBorder(0, 20, 20, 20));

        JPanel painelGols = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelGols.add(new JLabel("Gols 1:"));
        painelGols.add(new JTextField(3));
        painelGols.add(new JLabel("Gols 2:"));
        painelGols.add(new JTextField(3));
        JButton btnRegistrarResultado = new JButton("Registrar Resultado");
        painelGols.add(btnRegistrarResultado);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton btnVoltar = new JButton("Cancelar");
        btnVoltar.setBackground(corVermelha);
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setPreferredSize(new Dimension(100, 35));

        JButton btnCadastrar = new JButton("Salvar");
        btnCadastrar.setBackground(corVerde);
        btnCadastrar.setForeground(Color.WHITE);
        btnCadastrar.setFocusPainted(false);
        btnCadastrar.setPreferredSize(new Dimension(100, 35));

        painelAcoes.add(btnVoltar);
        painelAcoes.add(btnCadastrar);

        painelInferior.add(painelGols, BorderLayout.WEST);
        painelInferior.add(painelAcoes, BorderLayout.EAST);

        add(painelInferior, BorderLayout.SOUTH);


        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sel1 = campoSelecao1.getText();
                    String sel2 = campoSelecao2.getText();
                    String data = campoData.getText();
                    String fase = comboFase.getSelectedItem().toString();
                    String estadio = campoEstadio.getText();
                    int idArbitro = Integer.parseInt(campoIdArbitro.getText());



                    JOptionPane.showMessageDialog(null, "Partida cadastrada com sucesso!");


                    modeloTabela.addRow(new Object[]{sel1, sel2, fase, data, estadio});

                    // Limpa os campos para o próximo cadastro
                    campoSelecao1.setText("");
                    campoSelecao2.setText("");
                    campoData.setText("");
                    campoEstadio.setText("");
                    campoIdArbitro.setText("");

                } catch (PartidaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID do árbitro deve ser um número!", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}