package br.unb.cic.copa.view.aluno3;

import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno3.repository.ArbitroRepository;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GerenciarArbitroView extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtNacionalidade;
    private JTextField txtExperiencia;
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    private final ArbitroRepository repositorio = new ArbitroRepository("src/dados/arbitros.json");

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = new Color(30, 60, 120);
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    public GerenciarArbitroView() {
        setTitle("Gerenciar Árbitro - Copa do Mundo 2026");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        carregarTabela("");
        setVisible(true);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("⚽  Gerenciar Árbitros");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        return header;
    }

    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        corpo.add(criarFormulario(), BorderLayout.NORTH);
        corpo.add(criarPainelBusca(), BorderLayout.CENTER);

        return corpo;
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        txtId            = criarCampo();
        txtNome          = criarCampo();
        txtNacionalidade = criarCampo();
        txtExperiencia   = criarCampo();

        adicionarLinha(painel, gc, 0, "ID:",                 txtId);
        adicionarLinha(painel, gc, 1, "Nome:",               txtNome);
        adicionarLinha(painel, gc, 2, "Nacionalidade:",      txtNacionalidade);
        adicionarLinha(painel, gc, 3, "Experiência (anos):", txtExperiencia);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Campo de busca
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar por nome:");
        labelBusca.setFont(FONTE_LABEL);
        labelBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo();
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA);
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabela(txtBusca.getText().trim()));

        // Busca em tempo real ao digitar
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela(txtBusca.getText().trim());
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Tabela de resultados
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Nacionalidade", "Experiência"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 180));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirArbitro());

        JPanel painelExcluir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 6));
        painelExcluir.setBackground(COR_FUNDO);
        painelExcluir.add(btnExcluir);

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelExcluir, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarTabela(String filtro) {
        modeloTabela.setRowCount(0);
        try {
            List<Arbitro> lista = repositorio.listarTodos();
            for (Arbitro a : lista) {
                if (filtro.isEmpty() || a.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getNacionalidade(), a.getExperiencia()});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar árbitros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JTextField campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));

        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_CAMPO);
        campo.setPreferredSize(new Dimension(250, 32));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 210)),
                new EmptyBorder(4, 8, 4, 8)
        ));
        return campo;
    }

    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        btnCancelar.addActionListener(e -> {
            new MenuPrincipalView().setVisible(true);
            dispose();
        });

        btnSalvar.addActionListener(e -> salvarArbitro());

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
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

    private void excluirArbitro() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um árbitro na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o árbitro '" + nome + "'?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                repositorio.remover(id);
                JOptionPane.showMessageDialog(this, "Árbitro excluído com sucesso!");
                carregarTabela(txtBusca.getText().trim());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarArbitro() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nome = txtNome.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();
            int experiencia = Integer.parseInt(txtExperiencia.getText().trim());

            if (nome.isEmpty() || nacionalidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome e nacionalidade são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Arbitro arbitro = new Arbitro(id, nome, nacionalidade, experiencia);
            repositorio.salvar(arbitro);

            JOptionPane.showMessageDialog(this, "Árbitro salvo com sucesso!");
            carregarTabela("");
            txtBusca.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID e experiência devem ser números inteiros.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ExperienciaInvalidaException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}