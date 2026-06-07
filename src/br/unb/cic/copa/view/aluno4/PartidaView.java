package br.unb.cic.copa.view.aluno4;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;
import br.unb.cic.copa.controller.aluno4.PartidaController;
// import br.unb.cic.copa.controller.aluno3.ArbitroController;
import br.unb.cic.copa.model.aluno4.Partida;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PartidaView extends JFrame {

    private PartidaController partidaController;
    // private ArbitroController arbitroController;

    public PartidaView() {
        partidaController = new PartidaController();
        // arbitroController = new ArbitroController();

        setTitle("Gestão de Partidas");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel cadastro = new JPanel(new GridLayout(7, 2));

        JTextField campoSelecao1 = new JTextField();
        JTextField campoSelecao2 = new JTextField();
        JComboBox<String> comboFase = new JComboBox<>(new String[]{"Grupos", "Oitavas", "Quartas", "Semi-Final", "Final"});
        JTextField campoData = new JTextField();
        JTextField campoEstadio = new JTextField();
        JTextField campoIdArbitro = new JTextField();
        JButton btnVoltar = new JButton("Voltar");
        JButton btnCadastrar = new JButton("Cadastrar");

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

        cadastro.add(btnVoltar);
        cadastro.add(btnCadastrar);

        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuPrincipalView().setVisible(true);
                dispose();
            }
        });

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String sel1 = campoSelecao1.getText();
                    String sel2 = campoSelecao2.getText();
                    String data = campoData.getText();
                    String estadioDigitado = campoEstadio.getText();
                    String fase = comboFase.getSelectedItem().toString();
                    int idArbitro = Integer.parseInt(campoIdArbitro.getText());

                    // Partida novaPartida = new Partida(s1, s2, data, est, Fase.valueOf(fase.toUpperCase()));
                    // partidaController.salvarPartida(novaPartida);

                    // Arbitro arbitro = arbitroController.buscarPorId(idArbitro);
                    // if (arbitro != null) {
                    //     arbitro.designarParaPartida(novaPartida);
                    //     arbitroController.salvar(arbitro);
                    // }

                    JOptionPane.showMessageDialog(null, "Partida cadastrada e escalação concluída com sucesso!");

                    campoSelecao1.setText("");
                    campoSelecao2.setText("");
                    campoData.setText("");
                    campoEstadio.setText("");
                    campoIdArbitro.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Por favor, digite um número válido para o ID do árbitro!", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

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