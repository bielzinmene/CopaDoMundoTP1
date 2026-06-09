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
    private JButton btnAtivo; // guarda qual botão está selecionado

    private static final Color COR_FUNDO      = new Color(245, 245, 250);
    private static final Color COR_HEADER     = new Color(30, 60, 120);
    private static final Color COR_BOTAO      = new Color(30, 100, 180);
    private static final Color COR_BOTAO_ATIVO = new Color(20, 70, 130);
    private static final Color COR_CANCELAR   = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN  = Color.WHITE;
    private static final Font  FONTE_TITULO   = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONTE_TEXTO    = new Font("Courier New", Font.PLAIN, 12);
    private static final Font  FONTE_BOTAO    = new Font("Segoe UI", Font.BOLD, 13);

    public RelatorioView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Relatorios - Copa do Mundo 2026");
        setSize(950, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Relatorios da Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 0));
        corpo.setBackground(COR_FUNDO);

        // Painel lateral esquerdo com os botões
        JPanel painelLateral = new JPanel();
        painelLateral.setLayout(new BoxLayout(painelLateral, BoxLayout.Y_AXIS));
        painelLateral.setBackground(new Color(230, 233, 240));
        painelLateral.setBorder(new EmptyBorder(15, 10, 15, 10));
        painelLateral.setPreferredSize(new Dimension(200, 0));

        JLabel lblMenu = new JLabel("Relatorios");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMenu.setForeground(new Color(50, 50, 80));
        lblMenu.setBorder(new EmptyBorder(0, 5, 10, 0));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnInicio    = criarBotaoLateral("Inicio");
        JButton btnUsuarios  = criarBotaoLateral("Usuarios do Sistema");
        JButton btnConsolidado = criarBotaoLateral("Relatorio Consolidado");

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

        painelLateral.add(lblMenu);
        painelLateral.add(Box.createVerticalStrut(5));
        painelLateral.add(btnInicio);
        painelLateral.add(Box.createVerticalStrut(8));
        painelLateral.add(btnUsuarios);
        painelLateral.add(Box.createVerticalStrut(8));
        painelLateral.add(btnConsolidado);
        painelLateral.add(Box.createVerticalGlue());

        // Área de texto
        areaTexto = new JTextArea();
        areaTexto.setFont(FONTE_TEXTO);
        areaTexto.setEditable(false);
        areaTexto.setBackground(Color.WHITE);
        areaTexto.setBorder(new EmptyBorder(15, 15, 15, 15));
        areaTexto.setLineWrap(false);

        mostrarPaginaInicial();
        destacarBotao(btnInicio, btnUsuarios, btnConsolidado);

        JScrollPane scroll = new JScrollPane(areaTexto);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
                new Color(200, 200, 220)));
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        corpo.add(painelLateral, BorderLayout.WEST);
        corpo.add(scroll, BorderLayout.CENTER);

        return corpo;
    }

    // Exibe a página inicial explicativa
    private void mostrarPaginaInicial() {
        areaTexto.setText(


                        "Selecione um relatorio no menu ao lado:\n\n" +
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
                        "    - Ingressos vendidos e arrecadacao total\n\n" +
                        "---------------------------------------------"
        );
        areaTexto.setCaretPosition(0);
    }

    // Destaca o botão selecionado e normaliza os outros
    private void destacarBotao(JButton ativo, JButton... outros) {
        ativo.setBackground(COR_BOTAO_ATIVO);
        ativo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, Color.WHITE),
                new EmptyBorder(8, 10, 8, 10)));
        for (JButton btn : outros) {
            btn.setBackground(COR_BOTAO);
            btn.setBorder(new EmptyBorder(8, 13, 8, 10));
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
        btnVoltar.setPreferredSize(new Dimension(110, 36));
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e -> {
            new MenuPrincipalView(usuarioLogado).setVisible(true);
            dispose();
        });

        rodape.add(btnVoltar);
        return rodape;
    }

    private JButton criarBotaoLateral(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setBackground(COR_BOTAO);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(8, 13, 8, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        return btn;
    }
}