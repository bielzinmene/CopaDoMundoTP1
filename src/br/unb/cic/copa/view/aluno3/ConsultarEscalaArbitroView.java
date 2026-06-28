package br.unb.cic.copa.view.aluno3;

import br.unb.cic.copa.controller.aluno3.ArbitroController;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno4.Partida;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

// Tela para o árbitro consultar as partidas para as quais foi designado
public class ConsultarEscalaArbitroView extends JFrame {

    //label que mostra o nome do árbitro logado no topo da tela
    private JLabel labelNomeArbitro;
    //tabela que vai mostrar a escala de partidas
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    //controllers que fazem a ponte com os dados (árbitro e partida)
    private final ArbitroController controller = new ArbitroController();
    private final PartidaController partidaController = new PartidaController();

    //cores e fontes usadas na tela, deixadas como constantes pra reaproveitar
    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    //construtor: monta a janela e já carrega automaticamente a escala do árbitro logado
    public ConsultarEscalaArbitroView() {
        setTitle("Minha Escala - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //abre maximizada
        setResizable(true);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout()); //divide a tela em norte/centro/sul

        //coloca o ícone da janela
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        //monta as 3 partes da tela: topo, meio e rodapé
        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        //carrega a escala assim que a tela abre, sem precisar de nenhuma ação do árbitro
        carregarEscalaDoArbitroLogado();
        setVisible(true); //exibe a janela
    }

    //monta o cabeçalho preto com o título e uma imagem do lado direito
    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Minha Escala de Partidas");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        //carrega e redimensiona a imagem do header
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa_3.png");
            if (imgUrl != null) {
                ImageIcon icone = new ImageIcon(imgUrl);
                Image img = icone.getImage().getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    //monta o corpo da tela: nome do árbitro em cima, tabela de partidas embaixo
    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 15));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(20, 20, 10, 20));

        //label que vai mostrar o nome do árbitro logado, preenchido em carregarEscalaDoArbitroLogado()
        labelNomeArbitro = new JLabel(" ");
        labelNomeArbitro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelNomeArbitro.setForeground(COR_HEADER);

        corpo.add(labelNomeArbitro, BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        return corpo;
    }

    //monta a tabela que vai exibir as partidas da escala do árbitro
    private JScrollPane criarPainelTabela() {
        //define as colunas da tabela e impede edição das células
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

        //zebra striping: pinta linhas pares de branco e ímpares de cinza claro
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 248));
                }
                return c;
            }
        });

        //coloca a tabela dentro de um scroll, pra rolar quando tiver muitas linhas
        return new JScrollPane(tabela);
    }

    //busca o árbitro pelo login da sessão ativa e preenche a tabela com suas partidas
    //assim o árbitro só vê a própria escala, sem precisar digitar nada
    private void carregarEscalaDoArbitroLogado() {
        try {
            //pega o login do usuário que está logado na sessão atual
            String login = SessaoUsuario.getInstancia().getUsuarioLogado().getLogin();

            //busca o árbitro correspondente a esse login
            Arbitro arbitro = controller.buscarPorLogin(login);

            //mostra o nome do árbitro na tela e limpa a tabela
            labelNomeArbitro.setText("Árbitro: " + arbitro.getNome());
            modeloTabela.setRowCount(0);

            //busca todas as partidas designadas pra esse árbitro
            List<Partida> escala = partidaController.listarPorArbitro(arbitro.getId());

            //se não tem nenhuma partida, avisa e para por aqui
            if (escala.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Você não possui partidas designadas.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            //percorre as partidas e adiciona uma linha na tabela pra cada uma
            for (Partida p : escala) {
                modeloTabela.addRow(new Object[]{
                        p.getSelecao1() != null ? p.getSelecao1().getNome() : "-",
                        p.getSelecao2() != null ? p.getSelecao2().getNome() : "-",
                        p.getData(),
                        p.getEstadio() != null ? p.getEstadio().getNome() : "-",
                        p.getStatus()
                });
            }

        } catch (IOException e) {
            //se der erro ao ler os arquivos, mostra a mensagem de erro
            JOptionPane.showMessageDialog(this, "Erro ao carregar escala: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //monta o rodapé com o botão "Voltar"
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        //botão fecha a janela atual e volta para onde estava
        JButton btnVoltar = criarBotao("Voltar", COR_CANCELAR);
        btnVoltar.addActionListener(e -> dispose());

        rodape.add(btnVoltar);
        return rodape;
    }

    //metodo auxiliar pra criar botões com a mesma aparência (cor, fonte, cursor) e efeito hover
    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //escurece o botão quando o mouse passa por cima e volta ao normal quando sai
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(cor); }
        });
        return btn;
    }
}