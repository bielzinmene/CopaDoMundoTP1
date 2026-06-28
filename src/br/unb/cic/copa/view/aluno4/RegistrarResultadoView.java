package br.unb.cic.copa.view.aluno4;

import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.model.aluno4.Fase;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.Resultado;
import br.unb.cic.copa.model.aluno4.StatusPartida;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class RegistrarResultadoView extends JFrame {

    private JPanel painelLista;

    private final PartidaController partidaController = new PartidaController();

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_FASE    = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font  FONTE_TIME    = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public RegistrarResultadoView() {
        setTitle("Registrar Resultado - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarLista();
        setVisible(true);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("⚽  Registrar Resultado de Partida");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa_3.png");
            if (imgURL != null) {
                ImageIcon icone = new ImageIcon(imgURL);
                Image img = icone.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
        painelLista.setBackground(COR_FUNDO);

        JScrollPane scroll = new JScrollPane(painelLista);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        corpo.add(scroll, BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnVoltar = criarBotao("Voltar", COR_CANCELAR);
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipalView(SessaoUsuario.getInstancia().getUsuarioLogado()).setVisible(true);
        });

        rodape.add(btnVoltar);
        return rodape;
    }

    // Monta a lista de cards, um para cada partida que ainda não foi finalizada
    private void carregarLista() {
        painelLista.removeAll();

        List<Partida> lista = partidaController.listarTodas();
        for (Partida p : lista) {
            if (p.getStatus() == StatusPartida.FINALIZADA) continue;
            painelLista.add(criarLinhaPartida(p));
            painelLista.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        if (painelLista.getComponentCount() == 0) {
            JLabel lblVazio = new JLabel("Nenhuma partida agendada para registrar resultado.");
            lblVazio.setFont(FONTE_LABEL);
            lblVazio.setForeground(Color.GRAY);
            lblVazio.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelLista.add(lblVazio);
        }

        painelLista.revalidate();
        painelLista.repaint();
    }

    // Cria um card com as duas seleções, a fase e os campos para lançar o placar
    private JPanel criarLinhaPartida(Partida partida) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 225)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        Fase fase = partida.getFase();
        JLabel lblFase = new JLabel(fase != null ? fase.toString() : "-", SwingConstants.CENTER);
        lblFase.setFont(FONTE_FASE);
        lblFase.setForeground(Color.BLACK);

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        linha.setBackground(Color.WHITE);

        String nomeSel1 = partida.getSelecao1() != null ? partida.getSelecao1().getNome() : "-";
        String nomeSel2 = partida.getSelecao2() != null ? partida.getSelecao2().getNome() : "-";

        JLabel lblSel1 = new JLabel(nomeSel1);
        lblSel1.setFont(FONTE_TIME);
        lblSel1.setForeground(Color.BLACK);

        JTextField campoGols1 = criarCampoPequeno();
        JLabel lblX = new JLabel("x");
        lblX.setFont(FONTE_TIME);
        lblX.setForeground(Color.BLACK);
        JTextField campoGols2 = criarCampoPequeno();

        JLabel lblSel2 = new JLabel(nomeSel2);
        lblSel2.setFont(FONTE_TIME);
        lblSel2.setForeground(Color.BLACK);

        JButton btnRegistrar = criarBotao("Registrar", Color.BLACK);
        btnRegistrar.setPreferredSize(new Dimension(110, 32));
        btnRegistrar.addActionListener(e -> registrarResultado(partida, campoGols1, campoGols2));

        linha.add(lblSel1);
        linha.add(campoGols1);
        linha.add(lblX);
        linha.add(campoGols2);
        linha.add(lblSel2);
        linha.add(btnRegistrar);

        card.add(lblFase, BorderLayout.NORTH);
        card.add(linha, BorderLayout.CENTER);

        return card;
    }

    private void registrarResultado(Partida partida, JTextField campoGols1, JTextField campoGols2) {
        try {
            int gols1 = Integer.parseInt(campoGols1.getText().trim());
            int gols2 = Integer.parseInt(campoGols2.getText().trim());

            partida.iniciarPartida();
            partida.finalizarPartida(new Resultado(gols1, gols2));
            partidaController.salvarPartida(partida);

            JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!");
            carregarLista();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Gols devem ser números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField criarCampoPequeno() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_LABEL);
        campo.setHorizontalAlignment(JTextField.CENTER);
        campo.setPreferredSize(new Dimension(45, 32));
        campo.setMaximumSize(new Dimension(45, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 4, 4, 4)
        ));
        return campo;
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
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(cor); }
        });
        return btn;
    }
}