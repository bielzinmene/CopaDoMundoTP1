package br.unb.cic.copa.view.aluno3;

import br.unb.cic.copa.controller.aluno3.ArbitroController;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno4.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

// Tela para o árbitro consultar as partidas para as quais foi designado
public class ConsultarEscalaArbitroView extends JFrame {

    private JTextField txtIdArbitro;
    private JLabel labelNomeArbitro;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final ArbitroController controller = new ArbitroController();

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = new Color(30, 60, 120);
    private static final Color COR_BUSCAR    = new Color(30, 100, 180);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public ConsultarEscalaArbitroView() {
        setTitle("Consultar Escala do Árbitro - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("⚽  Escala do Árbitro");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 15));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(20, 20, 10, 20));

        corpo.add(criarPainelBusca(), BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 5, 5, 5);
        gc.anchor = GridBagConstraints.WEST;

        // Campo ID
        JLabel labelId = new JLabel("ID do Árbitro:");
        labelId.setFont(FONTE_LABEL);
        labelId.setForeground(new Color(50, 50, 80));

        txtIdArbitro = new JTextField();
        txtIdArbitro.setFont(FONTE_CAMPO);
        txtIdArbitro.setPreferredSize(new Dimension(150, 32));
        txtIdArbitro.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JButton btnBuscar = criarBotao("Buscar", COR_BUSCAR);
        btnBuscar.setPreferredSize(new Dimension(90, 32));
        btnBuscar.addActionListener(e -> buscarEscala());

        // Nome do árbitro encontrado
        labelNomeArbitro = new JLabel(" ");
        labelNomeArbitro.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelNomeArbitro.setForeground(COR_HEADER);

        gc.gridx = 0; gc.gridy = 0;
        painel.add(labelId, gc);
        gc.gridx = 1;
        painel.add(txtIdArbitro, gc);
        gc.gridx = 2;
        painel.add(btnBuscar, gc);
        gc.gridx = 3; gc.weightx = 1.0;
        painel.add(labelNomeArbitro, gc);

        return painel;
    }

    private JScrollPane criarPainelTabela() {
        modeloTabela = new DefaultTableModel(
                new String[]{"Seleção 1", "Seleção 2", "Data", "Estádio", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionBackground(new Color(200, 220, 255));

        return new JScrollPane(tabela);
    }

    private void buscarEscala() {
        String idTexto = txtIdArbitro.getText().trim();
        if (idTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o ID do árbitro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            Arbitro arbitro = controller.buscarPorId(id);

            labelNomeArbitro.setText("Árbitro: " + arbitro.getNome());
            modeloTabela.setRowCount(0);

            List<Partida> escala = arbitro.consultarEscala();

            if (escala.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhuma partida designada para este árbitro.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Partida p : escala) {
                modeloTabela.addRow(new Object[]{
                        p.getSelecao1().getNome(),
                        p.getSelecao2().getNome(),
                        p.getData(),
                        p.getEstadio().getNome(),
                        p.getStatus()
                });
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnVoltar = criarBotao("FECHAR", COR_CANCELAR);
        btnVoltar.addActionListener(e -> dispose());

        rodape.add(btnVoltar);
        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}