package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.view.aluno2.GerenciarJogadorView;
import br.unb.cic.copa.view.aluno2.GerenciarSelecaoView;
import br.unb.cic.copa.view.aluno3.GerenciarArbitroView;
import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;
import br.unb.cic.copa.view.aluno4.PartidaView;
import br.unb.cic.copa.view.aluno5.VendaIngressoView;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.view.aluno4.RegistrarResultadoView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuarioLogado;

    public MenuPrincipalView() {
        this(SessaoUsuario.getInstancia().getUsuarioLogado());
    }

    private static final Color COR_FUNDO = new Color(245, 248, 250);
    private static final Color COR_HEADER = Color.BLACK;
    private static final Color COR_BOTAO = new Color(52, 58, 64);    // cinza escuro
    private static final Color COR_BOTAO_HOVER = new Color(33, 37, 41); // mais escuro
    private static final Color COR_SAIR = new Color(108, 117, 125);   // cinza médio
    private static final Color COR_SAIR_HOVER = new Color(73, 80, 87);
    private static final Color COR_TEXTO_BTN = Color.WHITE;

    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_USUARIO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 14);

    public MenuPrincipalView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Sistema de Gestão da Copa 2026");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());


        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
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

        JLabel titulo = new JLabel("Sistema de Gestão da Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);


        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa_3.png");
            if (imgUrl != null) {
                ImageIcon icone = new ImageIcon(imgUrl);
                Image img = icone.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        JLabel lblUsuario = new JLabel(usuarioLogado.getNome() + "  |  " + usuarioLogado.getFuncao());
        lblUsuario.setFont(FONTE_USUARIO);
        lblUsuario.setForeground(new Color(200, 220, 255));
        header.add(lblUsuario, BorderLayout.EAST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel grade = new JPanel(new GridLayout(4, 3, 30, 30));
        grade.setBackground(COR_FUNDO);
        grade.setOpaque(false);

        JButton btnUsuarios   = criarBotao("Gestão de Usuários", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnSelecoes   = criarBotao("Gerenciar Seleções", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnJogadores  = criarBotao("Gerenciar Jogadores", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnEstadios   = criarBotao("Gerenciar Estádios", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnArbitros   = criarBotao("Gerenciar Árbitros", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnPartidas   = criarBotao("Gerenciar Partidas", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnResultados   = criarBotao("Gerenciar Resultados", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnIngressos  = criarBotao("Gerenciar Ingressos", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnRelatorios = criarBotao("Relatórios", COR_BOTAO, COR_BOTAO_HOVER);
        JButton btnSair       = criarBotao("Sair / Logout", COR_SAIR, COR_SAIR_HOVER);

        configurarAcesso(btnUsuarios, btnSelecoes, btnJogadores,
                btnEstadios, btnArbitros, btnPartidas,
                btnIngressos, btnRelatorios);


        btnUsuarios.addActionListener(evt -> {
            new GerenciarUsuariosView(usuarioLogado).setVisible(true);
            dispose();
        });
        btnSelecoes.addActionListener(evt -> {
            new GerenciarSelecaoView().setVisible(true);
            dispose();
        });
        btnJogadores.addActionListener(evt -> {
            new GerenciarJogadorView().setVisible(true);
            dispose();
        });
        btnEstadios.addActionListener(evt -> {
            new GerenciarEstadioView().setVisible(true);
            dispose();
        });
        btnArbitros.addActionListener(evt -> {
            new GerenciarArbitroView().setVisible(true);
            dispose();
        });
        btnPartidas.addActionListener(evt -> {
            new PartidaView().setVisible(true);
            dispose();
        });
        btnIngressos.addActionListener(evt -> {
            new VendaIngressoView().setVisible(true);
            dispose();
        });
        btnRelatorios.addActionListener(evt -> {
            new RelatorioView(usuarioLogado).setVisible(true);
            dispose();
        });
        btnResultados.addActionListener(evt -> {
            new RegistrarResultadoView().setVisible(true);
            dispose();
        });

        btnSair.addActionListener(evt -> {
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair do sistema?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                SessaoUsuario.getInstancia().encerrarSessao();
                new LoginView().setVisible(true);
                dispose();
            }
        });

        grade.add(btnUsuarios);
        grade.add(btnSelecoes);
        grade.add(btnJogadores);
        grade.add(btnEstadios);
        grade.add(btnArbitros);
        grade.add(btnPartidas);
        grade.add(btnIngressos);
        grade.add(btnRelatorios);
        grade.add(btnResultados);
        grade.add(new JLabel());
        grade.add(btnSair);
        grade.add(new JLabel());

        corpo.add(grade);
        return corpo;
    }

    private JButton criarBotao(String texto, Color cor, Color corHover) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(15, 25, 15, 25));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(corHover); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(cor); }
        });
        return btn;
    }

    private void configurarAcesso(JButton btnUsuarios, JButton btnSelecoes,
                                  JButton btnJogadores, JButton btnEstadios,
                                  JButton btnArbitros, JButton btnPartidas,
                                  JButton btnIngressos, JButton btnRelatorios) {
        btnUsuarios.setEnabled(false);
        btnSelecoes.setEnabled(false);
        btnJogadores.setEnabled(false);
        btnEstadios.setEnabled(false);
        btnArbitros.setEnabled(false);
        btnPartidas.setEnabled(false);
        btnIngressos.setEnabled(false);
        btnRelatorios.setEnabled(false);

        if (usuarioLogado instanceof Administrador) {
            btnUsuarios.setEnabled(true);
            btnSelecoes.setEnabled(true);
            btnJogadores.setEnabled(true);
            btnEstadios.setEnabled(true);
            btnArbitros.setEnabled(true);
            btnPartidas.setEnabled(true);
            btnIngressos.setEnabled(true);
            btnRelatorios.setEnabled(true);
        } else if (usuarioLogado instanceof Organizador) {
            btnSelecoes.setEnabled(true);
            btnJogadores.setEnabled(true);
            btnEstadios.setEnabled(true);
            btnPartidas.setEnabled(true);
        } else if (usuarioLogado instanceof Operador) {
            btnIngressos.setEnabled(true);
        } else {
            btnPartidas.setEnabled(true);
        }
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JLabel lblInfo = new JLabel(usuarioLogado.getNome() + "  |  Perfil: " + usuarioLogado.getFuncao());
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 130));
        rodape.add(lblInfo);
        return rodape;
    }
}