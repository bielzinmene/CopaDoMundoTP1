package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.RelatorioController;
import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno4.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelatorioView extends JFrame {

    private final Usuario usuarioLogado;
    private final RelatorioController controller = RelatorioController.getInstancia();
    private JPanel painelConteudo;
    private JScrollPane scrollPrincipal;

    private static final Color COR_FUNDO        = new Color(245, 245, 250);
    private static final Color COR_HEADER       = Color.BLACK;
    private static final Color COR_BOTAO        = Color.BLACK;
    private static final Color COR_CANCELAR     = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN    = Color.WHITE;
    private static final Color COR_TITULO_GRUPO = new Color(30, 60, 120);
    private static final Color COR_HEADER_TAB   = new Color(50, 50, 50);
    private static final Color COR_LINHA_PAR    = Color.WHITE;
    private static final Color COR_LINHA_IMPAR  = new Color(240, 242, 248);

    private static final Font FONTE_TITULO    = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONTE_BOTAO     = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONTE_CABECALHO = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font FONTE_TABELA    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_RESUMO    = new Font("Segoe UI", Font.PLAIN, 13);

    public RelatorioView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Relatorios - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        try {
            java.net.URL imgUrl = getClass().getClassLoader()
                    .getResource("resources/copa2026.jpg");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Relatorios da Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        try {
            java.net.URL imgUrl = getClass().getClassLoader()
                    .getResource("resources/copa_3.png");
            if (imgUrl != null) {
                ImageIcon icone = new ImageIcon(imgUrl);
                Image img = icone.getImage()
                        .getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                header.add(new JLabel(new ImageIcon(img)), BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout());
        corpo.setBackground(COR_FUNDO);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(230, 233, 240));
        painelBotoes.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                new Color(200, 200, 220)));

        JLabel lblMenu = new JLabel("Selecione o relatorio:");
        lblMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMenu.setForeground(new Color(50, 50, 80));

        JButton btnInicio      = criarBotao("Inicio", COR_BOTAO);
        JButton btnUsuarios    = criarBotao("Usuarios do Sistema", COR_BOTAO);
        JButton btnConsolidado = criarBotao("Relatorio Consolidado", COR_BOTAO);

        btnInicio.addActionListener(e -> {
            mostrarPaginaInicial();
            destacarBotao(btnInicio, btnUsuarios, btnConsolidado);
        });
        btnUsuarios.addActionListener(e -> {
            mostrarRelatorioUsuarios();
            destacarBotao(btnUsuarios, btnInicio, btnConsolidado);
        });
        btnConsolidado.addActionListener(e -> {
            mostrarRelatorioConsolidado();
            destacarBotao(btnConsolidado, btnInicio, btnUsuarios);
        });

        painelBotoes.add(lblMenu);
        painelBotoes.add(btnInicio);
        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnConsolidado);

        painelConteudo = new JPanel();
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBackground(COR_FUNDO);
        painelConteudo.setBorder(new EmptyBorder(20, 30, 20, 30));

        scrollPrincipal = new JScrollPane(painelConteudo);
        scrollPrincipal.setBorder(null);
        scrollPrincipal.getVerticalScrollBar().setUnitIncrement(16);

        mostrarPaginaInicial();
        destacarBotao(btnInicio, btnUsuarios, btnConsolidado);

        corpo.add(painelBotoes, BorderLayout.NORTH);
        corpo.add(scrollPrincipal, BorderLayout.CENTER);

        return corpo;
    }



    private void mostrarPaginaInicial() {
        painelConteudo.removeAll();

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextArea txt = new JTextArea();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setEditable(false);
        txt.setBackground(COR_FUNDO);
        txt.setBorder(new EmptyBorder(10, 0, 10, 0));
        txt.setText(
                "Selecione um relatorio no menu acima:\n\n" +
                        "  USUARIOS DO SISTEMA\n" +
                        "  Exibe todos os usuarios cadastrados com perfis e status.\n\n" +
                        "  RELATORIO CONSOLIDADO\n" +
                        "  Exibe panorama geral: usuarios, selecoes, partidas,\n" +
                        "  desempenho de selecoes e ingressos vendidos."
        );
        painel.add(txt);
        painelConteudo.add(painel);
        atualizarConteudo();
    }

    private void mostrarRelatorioUsuarios() {
        painelConteudo.removeAll();

        painelConteudo.add(criarTituloSecao("USUARIOS DO SISTEMA"));
        painelConteudo.add(Box.createVerticalStrut(10));

        String[] colunas = {"ID", "Nome", "Email", "Login", "Perfil", "Pais", "Status"};
        List<Usuario> usuarios = controller.getUsuarios();
        Object[][] dados = new Object[usuarios.size()][7];
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            dados[i] = new Object[]{
                    u.getId(), u.getNome(), u.getEmail(),
                    u.getLogin(), u.getFuncao(), u.getPais(), u.getStatus()
            };
        }

        painelConteudo.add(criarPainelTabela(colunas, dados));
        painelConteudo.add(Box.createVerticalStrut(20));

        painelConteudo.add(criarTituloSecao("RESUMO"));
        painelConteudo.add(Box.createVerticalStrut(10));

        Map<String, Long> resumo = controller.getResumoUsuarios();
        String[] colunasResumo = {"Perfil", "Quantidade"};
        Object[][] dadosResumo = new Object[resumo.size()][2];
        int i = 0;
        for (Map.Entry<String, Long> entry : resumo.entrySet()) {
            dadosResumo[i++] = new Object[]{entry.getKey(), entry.getValue()};
        }
        painelConteudo.add(criarPainelTabela(colunasResumo, dadosResumo));

        atualizarConteudo();
    }

    private void mostrarRelatorioConsolidado() {
        painelConteudo.removeAll();

        painelConteudo.add(criarTituloSecao("1. USUARIOS DO SISTEMA"));
        painelConteudo.add(Box.createVerticalStrut(10));
        Map<String, Long> resumoUsuarios = controller.getResumoUsuarios();
        String[] colUsuarios = {"Perfil", "Quantidade"};
        Object[][] dadosUsuarios = new Object[resumoUsuarios.size()][2];
        int i = 0;
        for (Map.Entry<String, Long> e : resumoUsuarios.entrySet()) {
            dadosUsuarios[i++] = new Object[]{e.getKey(), e.getValue()};
        }
        painelConteudo.add(criarPainelTabela(colUsuarios, dadosUsuarios));
        painelConteudo.add(Box.createVerticalStrut(25));

        painelConteudo.add(criarTituloSecao("2. PARTIDAS"));
        painelConteudo.add(Box.createVerticalStrut(10));
        Map<String, Integer> resumoPartidas = controller.getResumoPartidas();
        String[] colPartidas = {"Informacao", "Valor"};
        Object[][] dadosPartidas = new Object[resumoPartidas.size()][2];
        int j = 0;
        for (Map.Entry<String, Integer> e : resumoPartidas.entrySet()) {
            dadosPartidas[j++] = new Object[]{e.getKey(), e.getValue()};
        }
        painelConteudo.add(criarPainelTabela(colPartidas, dadosPartidas));
        painelConteudo.add(Box.createVerticalStrut(25));

        painelConteudo.add(criarTituloSecao("3. DESEMPENHO DAS SELECOES"));
        painelConteudo.add(Box.createVerticalStrut(10));

        List<Object[]> desempenho = controller.getDesempenhoSelecoes();

        if (desempenho.isEmpty()) {
            JLabel lblSemDados = new JLabel("  Nenhuma selecao cadastrada.");
            lblSemDados.setFont(FONTE_RESUMO);
            lblSemDados.setAlignmentX(Component.LEFT_ALIGNMENT);
            painelConteudo.add(lblSemDados);
        } else {
            String[] colSelecoes = {"Selecao", "Gols Feitos", "Gols Sofridos"};
            Object[][] dadosSelecoes = desempenho.toArray(new Object[0][]);
            painelConteudo.add(criarPainelTabela(colSelecoes, dadosSelecoes));
        }

        painelConteudo.add(Box.createVerticalStrut(25));


        painelConteudo.add(criarTituloSecao("4. INGRESSOS E PUBLICO"));
        painelConteudo.add(Box.createVerticalStrut(10));
        Map<String, String> resumoIngressos = controller.getResumoIngressos();
        String[] colIngressos = {"Informacao", "Valor"};
        Object[][] dadosIngressos = new Object[resumoIngressos.size()][2];
        int k = 0;
        for (Map.Entry<String, String> e : resumoIngressos.entrySet()) {
            dadosIngressos[k++] = new Object[]{e.getKey(), e.getValue()};
        }
        painelConteudo.add(criarPainelTabela(colIngressos, dadosIngressos));

        atualizarConteudo();
    }


    private JLabel criarTituloSecao(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(COR_TITULO_GRUPO);
        lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_TITULO_GRUPO));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return lbl;
    }

    private JPanel criarPainelTabela(String[] colunas, Object[][] dados) {
        DefaultTableModel modelo = new DefaultTableModel(dados, colunas) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabela = new JTable(modelo);
        tabela.setFont(FONTE_TABELA);
        tabela.setRowHeight(30);
        tabela.setShowGrid(false);
        tabela.setIntercellSpacing(new Dimension(0, 0));
        tabela.setSelectionBackground(new Color(200, 220, 255));

        JTableHeader header = tabela.getTableHeader();
        header.setFont(FONTE_CABECALHO);
        header.setBackground(COR_HEADER_TAB);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 32));
        header.setBorder(null);

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? COR_LINHA_PAR : COR_LINHA_IMPAR);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        painel.add(header, BorderLayout.NORTH);
        painel.add(tabela, BorderLayout.CENTER);
        painel.setAlignmentX(Component.LEFT_ALIGNMENT);
        painel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                header.getPreferredSize().height + dados.length * 30 + 2));

        return painel;
    }

    private void atualizarConteudo() {
        painelConteudo.revalidate();
        painelConteudo.repaint();
        SwingUtilities.invokeLater(() ->
                scrollPrincipal.getVerticalScrollBar().setValue(0));
    }



    private void destacarBotao(JButton ativo, JButton... outros) {
        ativo.setBackground(new Color(20, 70, 130));
        ativo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 0, Color.WHITE),
                new EmptyBorder(6, 14, 6, 14)));
        for (JButton btn : outros) {
            btn.setBackground(COR_BOTAO);
            btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        }
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                new Color(200, 200, 220)));

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(FONTE_BOTAO);
        btnVoltar.setBackground(COR_CANCELAR);
        btnVoltar.setForeground(COR_TEXTO_BTN);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorderPainted(false);
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnVoltar.setBackground(COR_CANCELAR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnVoltar.setBackground(COR_CANCELAR);
            }
        });
        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });

        rodape.add(btnVoltar);
        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 14, 8, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(new Color(20, 70, 130))) {
                    btn.setBackground(cor.darker());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(new Color(20, 70, 130))) {
                    btn.setBackground(cor);
                }
            }
        });
        return btn;
    }
}