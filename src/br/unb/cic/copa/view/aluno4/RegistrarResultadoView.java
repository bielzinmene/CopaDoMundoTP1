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

    private static final Color COR_VIEW_FUNDO     = Color.WHITE;          // Fundo principal da janela
    private static final Color COR_HEADER_FUNDO   = Color.BLACK;          // Fundo do header (preto como na imagem)
    private static final Color COR_HEADER_TEXTO   = Color.WHITE;          // Texto do header (branco)
    private static final Color COR_CONTEUDO_FUNDO = Color.WHITE;          // Fundo das áreas de conteúdo e lista (branco como na imagem)
    private static final Color COR_BORDA_GERAL    = new Color(200, 200, 200); // Cinza claro para bordas e linhas
    private static final Color COR_FUNDO_CAMPO    = Color.WHITE;          // Fundo dos campos de texto (branco)
    private static final Color COR_TEXTO_CAMPO    = Color.BLACK;          // Texto dos campos (preto)
    private static final Color COR_TEXTO_NORMAL   = Color.BLACK;          // Texto geral

    // Cores profissionais para botões de ação e cancelamento, baseadas no azul da imagem
    private static final Color COR_BOTAO_ACAO     = new Color(30, 100, 180); // Azul profissional para 'Registrar'
    private static final Color COR_BOTAO_VOLTAR   = new Color(180, 40, 40);  // Vermelho sóbrio para 'Voltar'
    private static final Color COR_TEXTO_BOTAO   = Color.WHITE;          // Texto dos botões (branco)

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
        getContentPane().setBackground(COR_VIEW_FUNDO);
        setLayout(new BorderLayout());

        // Carregar ícone da aplicação, agora usando copa2026.png
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.png");
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
        header.setBackground(COR_HEADER_FUNDO);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Título sem o emoji, para ser mais limpo como na imagem
        JLabel titulo = new JLabel("Registrar Resultado de Partida");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_HEADER_TEXTO);
        header.add(titulo, BorderLayout.WEST);

        // Adicionar o ícone no canto superior direito (troféu)
        ImageIcon icon = null;
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.png");
            if (imgUrl != null) icon = new ImageIcon(imgUrl);
        } catch (Exception ignored) {}
        if (icon != null) {
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setBorder(new EmptyBorder(0, 15, 0, 0)); // Espaçamento à esquerda
            header.add(lblIcon, BorderLayout.EAST);
        }

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setBackground(COR_CONTEUDO_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        painelLista = new JPanel();
        painelLista.setLayout(new BoxLayout(painelLista, BoxLayout.Y_AXIS));
        painelLista.setBackground(COR_CONTEUDO_FUNDO);

        JScrollPane scroll = new JScrollPane(painelLista);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(COR_CONTEUDO_FUNDO); // Garantir o fundo branco no viewport

        corpo.add(scroll, BorderLayout.CENTER);
        return corpo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_CONTEUDO_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA_GERAL));

        // Usar a nova cor sóbria para o botão Voltar
        JButton btnVoltar = criarBotao("Voltar", COR_BOTAO_VOLTAR);
        btnVoltar.addActionListener(e -> {
            dispose();
            new MenuPrincipalView(SessaoUsuario.getInstancia().getUsuarioLogado()).setVisible(true);
        });

        rodape.add(btnVoltar);
        return rodape;
    }

    // Lógica intocada, apenas paleta de cores
    private void carregarLista() {
        painelLista.removeAll();

        List<Partida> lista = partidaController.listarTodas();
        for (Partida p : lista) {
            if (p.getStatus() == StatusPartida.FINALIZADA) continue;
            painelLista.add(criarLinhaPartida(p));
            painelLista.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        painelLista.revalidate();
        painelLista.repaint();
    }

    private JPanel criarLinhaPartida(Partida partida) {
        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setBackground(Color.WHITE); // Mantido branco para destacar o cartão
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA_GERAL),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        Fase fase = partida.getFase();
        JLabel lblFase = new JLabel(fase != null ? fase.toString() : "-", SwingConstants.CENTER);
        lblFase.setFont(FONTE_FASE);
        lblFase.setForeground(COR_TEXTO_NORMAL);

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        linha.setBackground(Color.WHITE);

        String nomeSel1 = partida.getSelecao1() != null ? partida.getSelecao1().getNome() : "-";
        String nomeSel2 = partida.getSelecao2() != null ? partida.getSelecao2().getNome() : "-";

        JLabel lblSel1 = new JLabel(nomeSel1);
        lblSel1.setFont(FONTE_TIME);
        lblSel1.setForeground(COR_TEXTO_NORMAL);

        JTextField campoGols1 = criarCampoPequeno();
        JLabel lblX = new JLabel("x");
        lblX.setFont(FONTE_TIME);
        lblX.setForeground(COR_TEXTO_NORMAL);
        JTextField campoGols2 = criarCampoPequeno();

        JLabel lblSel2 = new JLabel(nomeSel2);
        lblSel2.setFont(FONTE_TIME);
        lblSel2.setForeground(COR_TEXTO_NORMAL);

        // Usar a nova cor azul para o botão Registrar
        JButton btnRegistrar = criarBotao("Registrar", COR_BOTAO_ACAO);
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

    // Lógica intocada, apenas paleta de cores nos popups
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
        // Usar a nova cor de borda e fundo
        campo.setBackground(COR_FUNDO_CAMPO);
        campo.setForeground(COR_TEXTO_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA_GERAL),
                new EmptyBorder(4, 4, 4, 4)
        ));
        return campo;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BOTAO);
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