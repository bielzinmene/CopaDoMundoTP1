package br.unb.cic.copa.view.aluno5;

import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.controller.aluno5.IngressosController;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno5.CategoriaIngresso;
import br.unb.cic.copa.model.aluno5.Venda;
import br.unb.cic.copa.model.aluno5.exception.VendaIngressoException;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VendaIngressoView extends JFrame {

    private JTextField txtId;
    private JTextField txtComprador;
    private JTextField txtQuantidade;
    private JTextField txtBusca;

    private JComboBox<Partida> cbPartida;
    private JComboBox<CategoriaIngresso> cbCategoria;

    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final IngressosController controller;
    private final PartidaController partidaController;

    private static final Color COR_FUNDO = new Color(245,245,250);
    private static final Color COR_HEADER = new Color(30,60,120);
    private static final Color COR_SALVAR = new Color(34,139,34);
    private static final Color COR_CANCELAR = new Color(180,40,40);
    private static final Color COR_BUSCA = new Color(30,100,180);

    public VendaIngressoView() {

        controller = new IngressosController();
        partidaController = new PartidaController();

        setTitle("Gerenciar Ingressos - Copa 2026");
        setSize(700,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarTabela();

        setVisible(true);
    }

    private JPanel criarHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(COR_HEADER);
        header.setBorder(
                new EmptyBorder(15,20,15,20)
        );

        JLabel titulo =
                new JLabel("🎟️ Gerenciar Ingressos");

        titulo.setForeground(Color.WHITE);
        titulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {

        JPanel corpo =
                new JPanel(new BorderLayout(0,10));

        corpo.setBackground(COR_FUNDO);

        corpo.setBorder(
                new EmptyBorder(15,20,10,20)
        );

        corpo.add(
                criarFormulario(),
                BorderLayout.NORTH
        );

        corpo.add(
                criarPainelTabela(),
                BorderLayout.CENTER
        );

        return corpo;
    }

    private JPanel criarFormulario() {

        JPanel painel =
                new JPanel(new GridBagLayout());

        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc =
                new GridBagConstraints();

        gc.insets =
                new Insets(6,5,6,5);

        gc.anchor =
                GridBagConstraints.WEST;

        txtId = criarCampo();
        txtId.setEditable(false);

        txtComprador = criarCampo();
        txtQuantidade = criarCampo();

        cbPartida = new JComboBox<>();

        carregarPartidas();

        cbCategoria =
                new JComboBox<>(
                        CategoriaIngresso.values()
                );

        adicionarLinha(
                painel,gc,0,
                "ID:",txtId
        );

        adicionarLinha(
                painel,gc,1,
                "Comprador:",txtComprador
        );

        JLabel lblPartida =
                new JLabel("Partida:");

        gc.gridx = 0;
        gc.gridy = 2;

        painel.add(lblPartida,gc);

        gc.gridx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;

        painel.add(cbPartida,gc);

        JLabel lblCategoria =
                new JLabel("Categoria:");

        gc.gridx = 0;
        gc.gridy = 3;

        painel.add(lblCategoria,gc);

        gc.gridx = 1;

        painel.add(cbCategoria,gc);

        adicionarLinha(
                painel,gc,4,
                "Quantidade:",txtQuantidade
        );

        return painel;
    }

    private JPanel criarPainelTabela() {

        JPanel painel =
                new JPanel(
                        new BorderLayout(0,8)
                );

        painel.setBackground(COR_FUNDO);

        JPanel busca =
                new JPanel(
                        new BorderLayout(8,0)
                );

        busca.setBackground(COR_FUNDO);

        txtBusca = criarCampo();

        JButton btnBuscar =
                criarBotao(
                        "Buscar",
                        COR_BUSCA
                );

        busca.add(
                new JLabel("Buscar:"),
                BorderLayout.WEST
        );

        busca.add(
                txtBusca,
                BorderLayout.CENTER
        );

        busca.add(
                btnBuscar,
                BorderLayout.EAST
        );

        modeloTabela =
                new DefaultTableModel(
                        new String[]{
                                "ID",
                                "Comprador",
                                "Qtd Ingressos",
                                "Valor Total"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int col
                    ) {
                        return false;
                    }
                };

        tabela = new JTable(modeloTabela);

        JScrollPane scroll =
                new JScrollPane(tabela);

        JButton btnExcluir =
                criarBotao(
                        "Excluir",
                        COR_CANCELAR
                );

        btnExcluir.addActionListener(
                e -> excluirIngresso()
        );

        JPanel painelExcluir =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        painelExcluir.setBackground(COR_FUNDO);

        painelExcluir.add(btnExcluir);

        painel.add(busca,BorderLayout.NORTH);
        painel.add(scroll,BorderLayout.CENTER);
        painel.add(painelExcluir,BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {

        JPanel rodape =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                15,
                                12
                        )
                );

        rodape.setBackground(COR_FUNDO);

        JButton btnCancelar =
                criarBotao(
                        "Cancelar",
                        COR_CANCELAR
                );

        JButton btnSalvar =
                criarBotao(
                        "Salvar",
                        COR_SALVAR
                );

        btnCancelar.addActionListener(
                e -> {
                    new MenuPrincipalView()
                            .setVisible(true);
                    dispose();
                }
        );

        btnSalvar.addActionListener(
                e -> salvarIngresso()
        );

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);

        return rodape;
    }

    private JTextField criarCampo() {

        JTextField campo =
                new JTextField();

        campo.setPreferredSize(
                new Dimension(
                        250,
                        32
                )
        );

        return campo;
    }

    private JButton criarBotao(
            String texto,
            Color cor
    ) {

        JButton btn =
                new JButton(texto);

        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setPreferredSize(
                new Dimension(
                        110,
                        36
                )
        );

        return btn;
    }

    private void carregarPartidas() {

        cbPartida.removeAllItems();

        for (Partida partida : partidaController.listarTodas()) {
            cbPartida.addItem(partida);
        }
    }

    private void salvarIngresso() {

        try {

            String comprador =
                    txtComprador.getText().trim();

            int quantidade =
                    Integer.parseInt(
                            txtQuantidade.getText().trim()
                    );

            Partida partida =
                    (Partida) cbPartida.getSelectedItem();

            CategoriaIngresso categoria =
                    (CategoriaIngresso) cbCategoria.getSelectedItem();

            controller.registrarVenda(
                    comprador,
                    partida,
                    categoria,
                    quantidade
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Venda registrada com sucesso!"
            );

            limparFormulario();

            carregarTabela();

        }
        catch (VendaIngressoException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Quantidade inválida.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirIngresso() {

        int linha = tabela.getSelectedRow();

        if (linha < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma venda."
            );

            return;
        }

        int id =
                (Integer) modeloTabela.getValueAt(
                        linha,
                        0
                );

        try {

            controller.excluirVenda(id);

            carregarTabela();

            limparFormulario();

            JOptionPane.showMessageDialog(
                    this,
                    "Venda removida com sucesso!"
            );

        }
        catch (VendaIngressoException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void carregarTabela() {

        modeloTabela.setRowCount(0);

        for (Venda venda : controller.listarVendas()) {

            modeloTabela.addRow(
                    new Object[] {
                            venda.getId(),
                            venda.getComprador(),
                            venda.getQuantidadeIngressos(),
                            venda.getValorTotal()
                    }
            );
        }
    }

    private void limparFormulario() {

        txtId.setText("");
        txtComprador.setText("");
        txtQuantidade.setText("");

        if (cbPartida.getItemCount() > 0) {
            cbPartida.setSelectedIndex(0);
        }

        cbCategoria.setSelectedIndex(0);

        tabela.clearSelection();
    }


    private void adicionarLinha(
            JPanel painel,
            GridBagConstraints gc,
            int linha,
            String texto,
            JTextField campo
    ) {

        gc.gridx = 0;
        gc.gridy = linha;
        gc.weightx = 0.3;
        gc.fill = GridBagConstraints.NONE;

        painel.add(
                new JLabel(texto),
                gc
        );

        gc.gridx = 1;
        gc.weightx = 0.7;
        gc.fill = GridBagConstraints.HORIZONTAL;

        painel.add(campo,gc);
    }
}