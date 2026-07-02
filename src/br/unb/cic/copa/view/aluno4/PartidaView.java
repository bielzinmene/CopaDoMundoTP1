package br.unb.cic.copa.view.aluno4;

import br.unb.cic.copa.controller.aluno3.ArbitroController;
import br.unb.cic.copa.controller.aluno3.EstadioController;
import br.unb.cic.copa.controller.aluno4.PartidaController;
import br.unb.cic.copa.model.aluno1.Administrador;
import br.unb.cic.copa.model.aluno1.Organizador;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.model.aluno1.Usuario;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno4.Fase;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PartidaView extends JFrame {
// aqui é usado a herança.

    private JTextField campoSelecao1;
    private JTextField campoSelecao2;
    private JComboBox<Fase> comboFase;
    private JTextField campoData;
    private JComboBox<String> comboEstadio;
    private JComboBox<String> comboNomeArbitro;
    private JTextField campoBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private TableRowSorter<DefaultTableModel> sorter;

    private final Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();

    private final PartidaController partidaController = new PartidaController();
    private final ArbitroController arbitroController = new ArbitroController();
    private final EstadioController estadioController = new EstadioController();

    private static final Color COR_FUNDO     = Color.WHITE;
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(30, 100, 180);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public PartidaView() {
        setTitle("Gerenciar Partidas - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);
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
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Gerenciar Partidas");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        try {
            ImageIcon iconeOrig = new ImageIcon("src/resources/copa2026.jpg");
            Image imagemRedimensionada = iconeOrig.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(imagemRedimensionada));
            header.add(logo, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Aviso: Não foi possível carregar a logo em src/resources/copa2026.jpg");
        }

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        corpo.add(criarFormulario(), BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        campoSelecao1  = criarCampoComPlaceholder("Ex: Brasil");
        campoSelecao2  = criarCampoComPlaceholder("Ex: Argentina");

        comboFase      = new JComboBox<>(Fase.values());
        comboFase.setFont(FONTE_CAMPO);
        comboFase.setPreferredSize(new Dimension(250, 32));

        campoData      = criarCampoComPlaceholder("dd/mm/aaaa");

        try {
            List<Estadio> listaEstadios = estadioController.listarTodos();
            if (listaEstadios == null || listaEstadios.isEmpty()) {
                comboEstadio = new JComboBox<>(new String[]{"Estádio de Toronto", "Estádio Guadalajara", "Estádio Monterrey"});
            } else {
                String[] nomesEstadios = new String[listaEstadios.size()];
                for (int i = 0; i < listaEstadios.size(); i++) {
                    nomesEstadios[i] = listaEstadios.get(i).getNome();
                }
                comboEstadio = new JComboBox<>(nomesEstadios);
            }
        } catch (Exception e) {
            comboEstadio = new JComboBox<>(new String[]{"Estádio de Toronto", "Estádio Guadalajara", "Estádio Monterrey"});
        }

        comboEstadio.setFont(FONTE_CAMPO);
        comboEstadio.setPreferredSize(new Dimension(250, 32));

        try {
            List<Arbitro> listaArbitros = arbitroController.listarTodos();
            if (listaArbitros == null || listaArbitros.isEmpty()) {
                comboNomeArbitro = new JComboBox<>(new String[]{"joao", "Árbitro Teste"});
            } else {
                String[] nomesArbitros = new String[listaArbitros.size()];
                for (int i = 0; i < listaArbitros.size(); i++) {
                    nomesArbitros[i] = listaArbitros.get(i).getNome();
                }
                comboNomeArbitro = new JComboBox<>(nomesArbitros);
            }
        } catch (Exception e) {
            comboNomeArbitro = new JComboBox<>(new String[]{"joao", "Árbitro Teste"});
        }

        comboNomeArbitro.setFont(FONTE_CAMPO);
        comboNomeArbitro.setPreferredSize(new Dimension(250, 32));

        adicionarLinha(painel, gc, 0, "Seleção 1:",       campoSelecao1);
        adicionarLinha(painel, gc, 1, "Seleção 2:",       campoSelecao2);
        adicionarLinhaCombo(painel, gc, 2, "Fase:",       comboFase);
        adicionarLinha(painel, gc, 3, "Data:",            campoData);
        adicionarLinhaCombo(painel, gc, 4, "Estádio:",    comboEstadio);
        adicionarLinhaCombo(painel, gc, 5, "Árbitro:",    comboNomeArbitro);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.setBackground(COR_FUNDO);
        JLabel lblBusca = new JLabel("Buscar por Seleção:");
        lblBusca.setFont(FONTE_LABEL);
        lblBusca.setForeground(Color.BLACK);
        campoBusca = criarCampoComPlaceholder("Ex: Brasil...");
        campoBusca.setPreferredSize(new Dimension(300, 32));

        painelBusca.add(lblBusca);
        painelBusca.add(campoBusca);

        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Seleção 1", "Seleção 2", "Data", "Fase", "Status", "ID Árbitro"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionBackground(new Color(220, 220, 220));

        sorter = new TableRowSorter<>(modeloTabela);
        tabela.setRowSorter(sorter);

        campoBusca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = campoBusca.getText().trim();
                if (texto.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    // O "?i" faz ignorar maiúsculas/minúsculas. Os índices 1 e 2 são as colunas das seleções.
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 1, 2));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 200));

        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));

        if (!(usuarioLogado instanceof Administrador)) {
            btnExcluir.setEnabled(false);
        }

        btnExcluir.addActionListener(e -> excluirPartida());

        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        painelExcluir.setBackground(COR_FUNDO);
        painelExcluir.add(btnExcluir);

        // Adicionando a busca, a tabela e o botão excluir na tela
        painel.add(painelBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelExcluir, BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnVoltar = criarBotao("Voltar", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        // aqui é utilizado a herança para o controle de acesso
        if (!(usuarioLogado instanceof Administrador) && !(usuarioLogado instanceof Organizador)) {
            btnSalvar.setEnabled(false);
            btnSalvar.setToolTipText("Você não tem permissão para criar partidas.");
        }

        btnVoltar.addActionListener(e -> {
            try {
                new MenuPrincipalView().setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir o menu: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalvar.addActionListener(e -> salvarPartida());

        rodape.add(btnVoltar);
        rodape.add(btnSalvar);
        return rodape;
    }

    private void salvarPartida() {
        try {
            String nomeSel1    = campoSelecao1.getText().trim();
            String nomeSel2    = campoSelecao2.getText().trim();
            String data        = campoData.getText().trim();
            String nomeEstadio = comboEstadio.getSelectedItem().toString();
            String nomeArbitroSel = comboNomeArbitro.getSelectedItem().toString();
            Fase fase          = (Fase) comboFase.getSelectedItem();

            if (nomeSel1.isEmpty() || nomeSel2.isEmpty() || data.isEmpty() || nomeEstadio.isEmpty() || nomeArbitroSel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataPartida = LocalDate.parse(data, formatter);

                LocalDate inicioCopa = LocalDate.of(2026, 6, 11);
                LocalDate fimCopa = LocalDate.of(2026, 7, 19);

                if (dataPartida.isBefore(inicioCopa) || dataPartida.isAfter(fimCopa)) {
                    JOptionPane.showMessageDialog(this, "A data deve estar no período da Copa (11/06/2026 a 19/07/2026).", "Data Inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Data inválida! Use o padrão dd/mm/aaaa (Ex: 15/06/2026).", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idArbitro = 1;
            try {
                List<Arbitro> listaArbitros = arbitroController.listarTodos();
                if (listaArbitros != null) {
                    for (Arbitro a : listaArbitros) {
                        if (a.getNome().equalsIgnoreCase(nomeArbitroSel)) {
                            idArbitro = a.getId();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                if ("joao".equalsIgnoreCase(nomeArbitroSel)) idArbitro = 1;
                else idArbitro = 99;
            }

            Selecao sel1 = new Selecao(nomeSel1, "", "");
            Selecao sel2 = new Selecao(nomeSel2, "", "");
            Localizacao loc = new Localizacao("", "", PaisSede.ESTADOS_UNIDOS, "");
            Estadio estadio = new Estadio(0, nomeEstadio, loc, 1);

            Partida novaPartida = new Partida(sel1, sel2, data, estadio, fase);
            novaPartida.setId(partidaController.gerarNovoId());

            Arbitro arbitro = arbitroController.buscarPorId(idArbitro);
            if (!arbitro.validarNacionalidade(novaPartida)) {
                JOptionPane.showMessageDialog(this,
                        "O árbitro " + arbitro.getNome() + " não pode apitar esta partida pois sua nacionalidade ("
                                + arbitro.getNacionalidade() + ") coincide com uma das seleções.",
                        "Árbitro Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Partida existente : partidaController.listarTodas()) {
                String estadioExistente = existente.getEstadio() != null ? existente.getEstadio().getNome() : "";
                if (estadioExistente.equalsIgnoreCase(nomeEstadio) && existente.getData().equals(data)) {
                    JOptionPane.showMessageDialog(this,
                            "Já existe uma partida no estádio '" + nomeEstadio + "' na data " + data + ".",
                            "Conflito de Estádio", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            novaPartida.setArbitroId(idArbitro);
            partidaController.salvarPartida(novaPartida);

            JOptionPane.showMessageDialog(this, "Partida salva com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do árbitro deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPartida() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma partida na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Quando a tabela é filtrada, a linha clicada não é a mesma do modelo original.
        // O convertRowIndexToModel corrige isso pra gente não excluir a partida errada!
        int linhaModel = tabela.convertRowIndexToModel(linha);
        int id = (int) modeloTabela.getValueAt(linhaModel, 0);
        String sel1 = (String) modeloTabela.getValueAt(linhaModel, 1);
        String sel2 = (String) modeloTabela.getValueAt(linhaModel, 2);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir a partida '" + sel1 + " x " + sel2 + "'?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            partidaController.excluirPartida(id);
            JOptionPane.showMessageDialog(this, "Partida excluída com sucesso!");
            carregarTabela();
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        List<Partida> lista = partidaController.listarTodas();
        for (Partida p : lista) {
            modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getSelecao1() != null ? p.getSelecao1().getNome() : "-",
                    p.getSelecao2() != null ? p.getSelecao2().getNome() : "-",
                    p.getData(),
                    p.getFase(),
                    p.getStatus(),
                    p.getArbitroId()
            });
        }
    }

    private void limparFormulario() {
        campoSelecao1.setText("");
        campoSelecao2.setText("");
        campoData.setText("");
        if (campoBusca != null) campoBusca.setText("");
        if (sorter != null) sorter.setRowFilter(null);
        comboFase.setSelectedIndex(0);
        if (comboEstadio.getItemCount() > 0) comboEstadio.setSelectedIndex(0);
        if (comboNomeArbitro.getItemCount() > 0) comboNomeArbitro.setSelectedIndex(0);
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JTextField campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(Color.BLACK);

        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private void adicionarLinhaCombo(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JComboBox<?> combo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(Color.BLACK);

        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(combo, gc);
    }

    private JTextField criarCampoComPlaceholder(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    g.setColor(new Color(180, 180, 200));
                    g.setFont(getFont().deriveFont(java.awt.Font.ITALIC));
                    g.drawString(placeholder, 8, getHeight() / 2 + getFont().getSize() / 2 - 2);
                }
            }
        };
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(250, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return campo;
    }

    private JButton criarBotao(String texto, Color col) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(col);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}