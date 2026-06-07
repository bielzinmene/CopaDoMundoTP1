package br.unb.cic.copa.view.aluno1;

import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.view.aluno2.GerenciarJogadorView;
import br.unb.cic.copa.view.aluno2.GerenciarSelecaoView;
import br.unb.cic.copa.view.aluno3.GerenciarArbitroView;
import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;
import br.unb.cic.copa.view.aluno4.PartidaView;
import br.unb.cic.copa.view.aluno5.VendaIngressoView;
import br.unb.cic.copa.view.aluno3.ConsultarEscalaArbitroView;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuPrincipalView extends JFrame {

    private final Usuario usuarioLogado;

    public MenuPrincipalView() {
        this(SessaoUsuario.getInstancia().getUsuarioLogado());
    }

    //Cores
    private static final Color COR_FUNDO     = new Color(240, 242, 245); // cinza azulado suave
    private static final Color COR_HEADER    = new Color(26, 53, 98);    // azul mais escuro
    private static final Color COR_BOTAO     = new Color(41, 128, 185);  // azul mais vibrante
    private static final Color COR_SAIR      = new Color(192, 57, 43);   // vermelho mais elegante
    private static final Color COR_TEXTO_BTN = Color.WHITE;

    private static final Font FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_USUARIO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_BOTAO   = new Font("Segoe UI", Font.BOLD, 13);

    public MenuPrincipalView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setTitle("Sistema de Gestão da Copa 2026");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // TELA CHEIA
        setLocationRelativeTo(null);
        // setResizable(true) por padrão (não foi chamado setResizable(false))
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

        JLabel titulo = new JLabel("  Sistema de Gestão da Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);

        JLabel lblUsuario = new JLabel(
                usuarioLogado.getNome() + " — " + usuarioLogado.getFuncao() + "  "
        );
        lblUsuario.setFont(FONTE_USUARIO);
        lblUsuario.setForeground(new Color(200, 220, 255));

        header.add(titulo, BorderLayout.WEST);
        header.add(lblUsuario, BorderLayout.EAST);
        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel grade = new JPanel(new GridLayout(3, 3, 25, 25));
        grade.setBackground(COR_FUNDO);

        JButton btnUsuarios   = criarBotao(" Gestão de Usuários", COR_BOTAO);
        JButton btnSelecoes   = criarBotao(" Gerenciar Seleções", COR_BOTAO);
        JButton btnJogadores  = criarBotao(" Gerenciar Jogadores", COR_BOTAO);
        JButton btnEstadios   = criarBotao(" Gerenciar Estádios", COR_BOTAO);
        JButton btnArbitros   = criarBotao(" Gerenciar Árbitros", COR_BOTAO);
        JButton btnPartidas   = criarBotao(" Gerenciar Partidas", COR_BOTAO);
        JButton btnIngressos  = criarBotao(" Gerenciar Ingressos", COR_BOTAO);
        JButton btnRelatorios = criarBotao(" Relatórios", COR_BOTAO);
        JButton btnSair       = criarBotao(" Sair / Logout", COR_SAIR);

        configurarAcesso(btnUsuarios, btnSelecoes, btnJogadores,
                btnEstadios, btnArbitros, btnPartidas,
                btnIngressos, btnRelatorios);

        // Listeners
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
            JOptionPane.showMessageDialog(this,
                    "O módulo de Relatórios Gerais está em fase de modelagem\n" +
                            "e será implementado na etapa seguinte.",
                    "Módulo em Desenvolvimento",
                    JOptionPane.INFORMATION_MESSAGE);
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
        grade.add(btnSair);

        corpo.add(grade);
        return corpo;
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
            btnArbitros.setEnabled(true);
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

        JLabel lblInfo = new JLabel(
                " " + usuarioLogado.getNome() + "  |  Perfil: " + usuarioLogado.getFuncao()
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 130));
        rodape.add(lblInfo);
        return rodape;
    }

    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setDisabledIcon(null);
        btn.setOpaque(true);
        btn.setBorder(new EmptyBorder(12, 20, 12, 20)); // padding interno para botões maiores

        // Efeito hover (muda cor do fundo levemente)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(cor.equals(COR_BOTAO) ? new Color(31, 97, 141) : new Color(153, 45, 34));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(cor);
                }
            }
        });

        return btn;
    }
}