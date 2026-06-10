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
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PartidaView extends JFrame {

    private JTextField campoSelecao1;
    private JTextField campoSelecao2;
    private JComboBox<Fase> comboFase;
    private JTextField campoData;
    private JComboBox<String> comboEstadio;
    private JComboBox<Arbitro> comboArbitro;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final Usuario usuarioLogado = SessaoUsuario.getInstancia().getUsuarioLogado();

    private final PartidaController  partidaController  = new PartidaController();
    private final ArbitroController  arbitroController  = new ArbitroController();
    private final EstadioController  estadioController  = new EstadioController();

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = new Color(30,  60, 120);
    private static final Color COR_SALVAR    = new Color(34, 139,  34);
    private static final Color COR_CANCELAR  = new Color(180,  40,  40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN,  13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN,  13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD,   16);

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
        add(criarCorpo(),  BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarTabela();
        setVisible(true);
    }
    private void recarregarArbitros() {
        Arbitro selecionadoAtual = (Arbitro) comboArbitro.getSelectedItem();
        comboArbitro.removeAllItems();

        try {
            List<Arbitro> lista = arbitroController.listarTodos();
            if (lista != null) {
                for (Arbitro a : lista) {
                    comboArbitro.addItem(a);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar árbitros: " + e.getMessage());
        }

        // tenta manter a seleção anterior
        if (selecionadoAtual != null) {
            for (int i = 0; i < comboArbitro.getItemCount(); i++) {
                if (comboArbitro.getItemAt(i).getId() == selecionadoAtual.getId()) {
                    comboArbitro.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("⚽  Gerenciar Partidas");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        corpo.add(criarFormulario(),    BorderLayout.NORTH);
        corpo.add(criarPainelTabela(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        campoSelecao1 = criarCampoComPlaceholder("Ex: Brasil");
        campoSelecao2 = criarCampoComPlaceholder("Ex: Argentina");

        comboFase = new JComboBox<>(Fase.values());
        comboFase.setFont(FONTE_CAMPO);
        comboFase.setPreferredSize(new Dimension(250, 32));

        campoData = criarCampoComPlaceholder("dd/mm/aaaa");
        try {
            List<Estadio> listaEstadios = estadioController.listarTodos();
            if (listaEstadios == null || listaEstadios.isEmpty()) {
                comboEstadio = new JComboBox<>(new String[]{"Estádio de Toronto", "Estádio Guadalajara", "Estádio Monterrey"});
            } else {
                String[] nomes = listaEstadios.stream()
                        .map(Estadio::getNome)
                        .toArray(String[]::new);
                comboEstadio = new JComboBox<>(nomes);
            }
        } catch (Exception e) {
            comboEstadio = new JComboBox<>(new String[]{"Estádio de Toronto", "Estádio Guadalajara", "Estádio Monterrey"});
        }
        comboEstadio.setFont(FONTE_CAMPO);
        comboEstadio.setPreferredSize(new Dimension(250, 32));
        comboArbitro = new JComboBox<>();
        comboArbitro.setFont(FONTE_CAMPO);
        comboArbitro.setPreferredSize(new Dimension(250, 32));
        comboArbitro.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Arbitro) {
                    setText(((Arbitro) value).getNome());
                }
                return this;
            }
        });
        recarregarArbitros();

        adicionarLinha     (painel, gc, 0, "Seleção 1:",  campoSelecao1);
        adicionarLinha     (painel, gc, 1, "Seleção 2:",  campoSelecao2);
        adicionarLinhaCombo(painel, gc, 2, "Fase:",       comboFase);
        adicionarLinha     (painel, gc, 3, "Data:",       campoData);
        adicionarLinhaCombo(painel, gc, 4, "Estádio:",    comboEstadio);
        adicionarLinhaCombo(painel, gc, 5, "Árbitro:",    comboArbitro);

        return painel;
    }

    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        JPanel painelResultado = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelResultado.setBackground(COR_FUNDO);
        painelResultado.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220)),
                "Registrar Resultado",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                COR_HEADER
        ));

        JTextField campoGols1 = criarCampoPequeno();
        JTextField campoGols2 = criarCampoPequeno();
        JButton btnResultado  = criarBotao("Registrar", COR_BUSCA);
        btnResultado.setPreferredSize(new Dimension(100, 32));

        JLabel lGols1 = new JLabel("Gols Seleção 1:");
        lGols1.setFont(FONTE_LABEL);
        lGols1.setForeground(new Color(50, 50, 80));
        JLabel lGols2 = new JLabel("Gols Seleção 2:");
        lGols2.setFont(FONTE_LABEL);
        lGols2.setForeground(new Color(50, 50, 80));

        painelResultado.add(lGols1);
        painelResultado.add(campoGols1);
        painelResultado.add(lGols2);
        painelResultado.add(campoGols2);
        painelResultado.add(btnResultado);

        modeloTabela = new DefaultTableModel(
                new String[]{"ID", "Seleção 1", "Seleção 2", "Data", "Fase", "Status", "Árbitro"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setSelectionBackground(new Color(200, 220, 255));

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

        btnResultado.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma partida na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int idPartida = (int) modeloTabela.getValueAt(linha, 0);
                int gols1 = Integer.parseInt(campoGols1.getText().trim());
                int gols2 = Integer.parseInt(campoGols2.getText().trim());

                Partida partida = partidaController.buscarPorId(idPartida);
                if (partida == null) {
                    JOptionPane.showMessageDialog(this, "Partida não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                partida.iniciarPartida();
                partida.finalizarPartida(new br.unb.cic.copa.model.aluno4.Resultado(gols1, gols2));
                partidaController.salvarPartida(partida);

                JOptionPane.showMessageDialog(this, "Resultado registrado com sucesso!");
                campoGols1.setText("");
                campoGols2.setText("");
                carregarTabela();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Gols devem ser números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        painel.add(painelResultado, BorderLayout.NORTH);
        painel.add(scroll,          BorderLayout.CENTER);
        painel.add(painelExcluir,   BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar",   COR_SALVAR);

        if (!(usuarioLogado instanceof Administrador) && !(usuarioLogado instanceof Organizador)) {
            btnSalvar.setEnabled(false);
            btnSalvar.setToolTipText("Você não tem permissão para criar partidas.");
        }

        btnCancelar.addActionListener(e -> {
            dispose();
            new MenuPrincipalView(SessaoUsuario.getInstancia().getUsuarioLogado()).setVisible(true);
        });

        btnSalvar.addActionListener(e -> salvarPartida());

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
    }

    private void salvarPartida() {
        try {
            String nomeSel1    = campoSelecao1.getText().trim();
            String nomeSel2    = campoSelecao2.getText().trim();
            String data        = campoData.getText().trim();
            String nomeEstadio = comboEstadio.getSelectedItem().toString();
            Fase   fase        = (Fase) comboFase.getSelectedItem();

            Arbitro arbitroSelecionado = (Arbitro) comboArbitro.getSelectedItem();

            if (nomeSel1.isEmpty() || nomeSel2.isEmpty() || data.isEmpty()
                    || nomeEstadio.isEmpty() || arbitroSelecionado == null) {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataPartida = LocalDate.parse(data, formatter);
                LocalDate inicioCopa  = LocalDate.of(2026, 6, 11);
                LocalDate fimCopa     = LocalDate.of(2026, 7, 19);

                if (dataPartida.isBefore(inicioCopa) || dataPartida.isAfter(fimCopa)) {
                    JOptionPane.showMessageDialog(this,
                            "A data deve estar no período da Copa (11/06/2026 a 19/07/2026).",
                            "Data Inválida", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Data inválida! Use o padrão dd/mm/aaaa (Ex: 15/06/2026).",
                        "Erro de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Selecao sel1    = new Selecao(nomeSel1, "", "");
            Selecao sel2    = new Selecao(nomeSel2, "", "");
            Localizacao loc = new Localizacao("", "", PaisSede.ESTADOS_UNIDOS, "");
            Estadio estadio = new Estadio(0, nomeEstadio, loc, 1);

            Partida novaPartida = new Partida(sel1, sel2, data, estadio, fase);
            novaPartida.setId(partidaController.gerarNovoId());

            if (!arbitroSelecionado.validarNacionalidade(novaPartida)) {
                JOptionPane.showMessageDialog(this,
                        "O árbitro " + arbitroSelecionado.getNome()
                                + " não pode apitar esta partida pois sua nacionalidade ("
                                + arbitroSelecionado.getNacionalidade()
                                + ") coincide com uma das seleções.",
                        "Árbitro Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (Partida existente : partidaController.listarTodas()) {
                String estadioExistente = existente.getEstadio() != null
                        ? existente.getEstadio().getNome() : "";
                if (estadioExistente.equalsIgnoreCase(nomeEstadio)
                        && existente.getData().equals(data)) {
                    JOptionPane.showMessageDialog(this,
                            "Já existe uma partida no estádio '" + nomeEstadio
                                    + "' na data " + data + ".",
                            "Conflito de Estádio", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            novaPartida.setArbitroId(arbitroSelecionado.getId());
            partidaController.salvarPartida(novaPartida);

            JOptionPane.showMessageDialog(this, "Partida salva com sucesso!");
            limparFormulario();
            carregarTabela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPartida() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma partida na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int    id   = (int)    modeloTabela.getValueAt(linha, 0);
        String sel1 = (String) modeloTabela.getValueAt(linha, 1);
        String sel2 = (String) modeloTabela.getValueAt(linha, 2);
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
        recarregarArbitros(); // mantém o combo atualizado com o JSON atual

        modeloTabela.setRowCount(0);
        List<Partida> lista = partidaController.listarTodas();
        for (Partida p : lista) {

            String nomeArbitro = resolverNomeArbitro(p.getArbitroId());

            modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getSelecao1() != null ? p.getSelecao1().getNome() : "-",
                    p.getSelecao2() != null ? p.getSelecao2().getNome() : "-",
                    p.getData(),
                    p.getFase(),
                    p.getStatus(),
                    nomeArbitro
            });
        }
    }

    private String resolverNomeArbitro(int idArbitro) {
        try {
            Arbitro a = arbitroController.buscarPorId(idArbitro);
            return (a != null) ? a.getNome() : "Desconhecido";
        } catch (Exception e) {
            return "Desconhecido";
        }
    }

    private void limparFormulario() {
        campoSelecao1.setText("");
        campoSelecao2.setText("");
        campoData.setText("");
        comboFase.setSelectedIndex(0);
        if (comboEstadio.getItemCount() > 0) comboEstadio.setSelectedIndex(0);
        if (comboArbitro.getItemCount()  > 0) comboArbitro.setSelectedIndex(0);
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc,
                                int linha, String labelTxt, JTextField campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private void adicionarLinhaCombo(JPanel painel, GridBagConstraints gc,
                                     int linha, String labelTxt, JComboBox<?> combo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
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

    private JTextField criarCampoPequeno() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(50, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
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
        return btn;
    }
}