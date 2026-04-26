package br.unb.cic.copa.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GerenciarSelecaoView extends JFrame {

    private JTextField txtNome;
    private JTextField txtGrupo;
    private JTextField txtTecnico;
    private JTable tabelaSelecoes;
    private DefaultTableModel modelTabela;

    public GerenciarSelecaoView() {

        setTitle("Gerenciar Seleções - Copa 2026");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel painelInputs = new JPanel(new GridLayout(4, 2, 5, 5));
        painelInputs.setBorder(BorderFactory.createTitledBorder("Dados da Seleção"));

        painelInputs.add(new JLabel(" Nome do País:"));
        txtNome = new JTextField();
        painelInputs.add(txtNome);

        painelInputs.add(new JLabel(" Grupo:"));
        txtGrupo = new JTextField();
        painelInputs.add(txtGrupo);

        painelInputs.add(new JLabel(" Técnico:"));
        txtTecnico = new JTextField();
        painelInputs.add(txtTecnico);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnCancelar.addActionListener(e ->{
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        btnSalvar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        painelInputs.add(btnSalvar);
        painelInputs.add(btnCancelar);

        String[] colunas = {"País", "Grupo", "Técnico"};

        modelTabela = new DefaultTableModel(colunas, 0);
        tabelaSelecoes = new JTable(modelTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaSelecoes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Seleções Cadastradas"));

        add(painelInputs, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

}