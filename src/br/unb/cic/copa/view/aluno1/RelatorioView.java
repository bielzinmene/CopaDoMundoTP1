package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.controller.aluno1.RelatorioController;
import br.unb.cic.copa.model.aluno1.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RelatorioView extends JFrame {

    private final Usuario usuarioLogado;
    private final RelatorioController controller = RelatorioController.getInstancia();
    private JTextArea areaTexto;

    private static final Color COR_FUNDO      = new Color(245, 245, 250);
    private static final Color COR_HEADER     = Color.BLACK;
    private static final Color COR_BOTAO      = Color.BLACK;
    private static final Color COR_CANCELAR   = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN  = Color.WHITE;
    private static final Font  FONTE_TITULO   = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONTE_TEXTO    = new Font("Consolas", Font.PLAIN, 13);
    private static final Font  FONTE_BOTAO    = new Font("Segoe UI", Font.BOLD, 13);

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
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 0));
        corpo.setBackground(COR_FUNDO);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelBotoes.setBackground(new Color(230, 233, 240));
        painelBotoes.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
                new Color(200, 200, 220)));

        JLabel lblMenu = new JLabel("Selecione o relatorio:");
        lblMenu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMenu.setForeground(new Color(50, 50, 80));

        JButton btnInicio      = criarBotao("Inicio", Color.BLACK);
        JButton btnUsuarios    = criarBotao("Usuarios do Sistema", COR_BOTAO);
        JButton btnConsolidado = criarBotao("Relatorio Consolidado", COR_BOTAO);

        btnInicio.addActionListener(e -> {
            mostrarPaginaInicial();
            destacarBotao(btnInicio, btnUsuarios, btnConsolidado);
        });

        btnUsuarios.addActionListener(e -> {
            areaTexto.setText(controller.gerarRelatorioUsuarios());
            areaTexto.setCaretPosition(0);
            destacarBotao(btnUsuarios, btnInicio, btnConsolidado);
        });

        btnConsolidado.addActionListener(e -> {
            areaTexto.setText(controller.gerarRelatorioConsolidado());
            areaTexto.setCaretPosition(0);
            destacarBotao(btnConsolidado, btnInicio, btnUsuarios);
        });

        painelBotoes.add(lblMenu);
        painelBotoes.add(btnInicio);
        painelBotoes.add(btnUsuarios);
        painelBotoes.add(btnConsolidado);


        areaTexto = new JTextArea();
        areaTexto.setFont(FONTE_TEXTO);
        areaTexto.setEditable(false);
        areaTexto.setBackground(Color.WHITE);
        areaTexto.setBorder(new EmptyBorder(15, 15, 15, 15));
        areaTexto.setLineWrap(false);

        mostrarPaginaInicial();
        destacarBotao(btnInicio, btnUsuarios, btnConsolidado);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        corpo.add(painelBotoes, BorderLayout.NORTH);
        corpo.add(scroll, BorderLayout.CENTER);

        return corpo;
    }

    private void mostrarPaginaInicial() {
        areaTexto.setText(

                        "---------------------------------------------\n\n" +
                        "  USUARIOS DO SISTEMA\n" +
                        "  Exibe todos os usuarios cadastrados no\n" +
                        "  sistema com seus perfis e status.\n" +
                        "  Inclui um resumo com totais por perfil.\n\n" +
                        "---------------------------------------------\n\n" +
                        "  RELATORIO CONSOLIDADO\n" +
                        "  Exibe um panorama geral da competicao:\n\n" +
                        "    - Usuarios do sistema\n" +
                        "    - Selecoes e jogadores cadastrados\n" +
                        "    - Partidas (total, finalizadas, agendadas)\n" +
                        "    - Total de gols da competicao\n" +
                        "    - Desempenho de cada selecao\n" +
                        "      (vitorias, empates, derrotas, gols, pontos)\n" +
                        "    - Ingressos vendidos e arrecadacao total\n\n"

        );
        areaTexto.setCaretPosition(0);
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