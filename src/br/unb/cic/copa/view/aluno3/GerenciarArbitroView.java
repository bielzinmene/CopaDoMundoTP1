package br.unb.cic.copa.view.aluno3;

import br.unb.cic.copa.controller.aluno3.ArbitroController;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class GerenciarArbitroView extends JFrame {

    //campos do formulário de cadastro/edição
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JTextField txtSenha;
    private JTextField txtCpf;
    private JTextField txtNacionalidade;
    private JTextField txtExperiencia;
    //campo de busca e tabela de árbitros já cadastrados
    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    //controller que faz a ponte com os dados dos árbitros
    private final ArbitroController controller = new ArbitroController();

    //cores e fontes usadas na tela
    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);

    //construtor só chama o init, que monta tudo
    public GerenciarArbitroView() {
        init();
    }

    //configura a janela e monta as 3 partes da tela (header, corpo, rodapé)
    private void init() {
        setTitle("Gerenciar Árbitro - Copa do Mundo 2026");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 720);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); //abre maximizada
        setResizable(true);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        //tenta colocar o ícone da janela
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if (imgUrl != null) setIconImage(new ImageIcon(imgUrl).getImage());
        } catch (Exception ignored) {}

        add(criarHeader(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);

        //já carrega a tabela com todos os árbitros ao abrir a tela
        carregarTabela("");
        setVisible(true);
    }

    //monta o cabeçalho preto com título e imagem
    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("  Gerenciar Árbitros");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);
        header.add(titulo, BorderLayout.WEST);

        //tenta carregar e redimensionar a imagem do header
        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if (imgUrl != null) {
                ImageIcon icone = new ImageIcon(imgUrl);
                Image img = icone.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon(img));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header;
    }

    //monta o corpo: formulário em cima, painel de busca/tabela embaixo
    private JPanel criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(0, 10));
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(15, 20, 10, 20));

        corpo.add(criarFormulario(), BorderLayout.NORTH);
        corpo.add(criarPainelBusca(), BorderLayout.CENTER);

        return corpo;
    }

    //monta o formulário de cadastro com todos os campos do árbitro
    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_FUNDO);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 5, 6, 5);
        gc.anchor = GridBagConstraints.WEST;

        //cria cada campo já com um texto de exemplo (placeholder)
        txtNome          = criarCampoComPlaceholder("Ex: João Silva");
        txtEmail         = criarCampoComPlaceholder("Ex: joao@email.com");
        txtLogin         = criarCampoComPlaceholder("Ex: joao.silva");
        txtSenha         = criarCampoComPlaceholder("Mín. 8 caracteres, letra e número");
        txtCpf           = criarCampoComPlaceholder("000.000.000-00");
        txtNacionalidade = criarCampoComPlaceholder("Ex: Brasileiro");
        txtExperiencia   = criarCampoComPlaceholder("Ex: 5");

        //adiciona cada campo no formulário, um por linha
        adicionarLinha(painel, gc, 0, "Nome:",               txtNome);
        adicionarLinha(painel, gc, 1, "Email:",              txtEmail);
        adicionarLinha(painel, gc, 2, "Login:",              txtLogin);
        adicionarLinha(painel, gc, 3, "Senha:",              txtSenha);
        adicionarLinha(painel, gc, 4, "CPF:",                txtCpf);
        adicionarLinha(painel, gc, 5, "País:",               txtNacionalidade);
        adicionarLinha(painel, gc, 6, "Experiência (anos):", txtExperiencia);

        return painel;
    }

    //monta o painel com busca, tabela de árbitros e botão excluir
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

        // Busca em tempo real ao digitar (sem precisar clicar no botão)
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                carregarTabela(txtBusca.getText().trim());
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);

        // Tabela de resultados com as colunas que vão aparecer
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Nacionalidade", "Experiência"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);

        //zebra striping: alterna cor de fundo das linhas pra facilitar leitura
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

        //quando o usuário seleciona uma linha da tabela, carrega os dados desse árbitro no formulário
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { //evita disparar duas vezes durante a seleção
                carregarArbitroSelecionado();
            }
        });

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

    //busca todos os árbitros e preenche a tabela, filtrando pelo nome se houver filtro
    private void carregarTabela(String filtro) {
        modeloTabela.setRowCount(0); //limpa a tabela antes de preencher
        try {
            List<Arbitro> lista = controller.listarTodos();
            for (Arbitro a : lista) {
                //se o filtro estiver vazio mostra todos, senão só os que contém o texto digitado
                if (filtro.isEmpty() || a.getNome().toLowerCase().contains(filtro.toLowerCase())) {
                    modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getNacionalidade(), a.getExperiencia()});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar árbitros: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //metodo auxiliar pra adicionar um label + campo numa linha do formulário (GridBagLayout)
    private void adicionarLinha(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JTextField campo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));

        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);

        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(campo, gc);
    }

    //cria um campo de texto simples (usado no campo de busca)
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

    //cria um campo de texto que mostra um texto cinza de exemplo quando está vazio
    private JTextField criarCampoComPlaceholder(String placeholder) {
        JTextField campo = new JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                //só desenha o placeholder se o campo estiver vazio
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

    //monta o rodapé com os botões limpar, cancelar e salvar
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rodape.setBackground(COR_FUNDO);
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        JButton btnLimpar   = criarBotao("Limpar", COR_BUSCA);
        JButton btnCancelar = criarBotao("Cancelar", COR_CANCELAR);
        JButton btnSalvar   = criarBotao("Salvar", COR_SALVAR);

        //limpar reseta todos os campos do formulário, sem precisar salvar
        btnLimpar.addActionListener(e -> limparFormulario());

        //cancelar fecha essa tela e volta pro menu principal
        btnCancelar.addActionListener(e -> {
            dispose();
            new MenuPrincipalView(SessaoUsuario.getInstancia().getUsuarioLogado()).setVisible(true);
        });

        //salvar dispara a validação e o cadastro do árbitro
        btnSalvar.addActionListener(e -> salvarArbitro());

        rodape.add(btnLimpar);
        rodape.add(btnCancelar);
        rodape.add(btnSalvar);
        return rodape;
    }

    //metodo auxiliar pra criar botões com a mesma aparência e efeito hover
    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //escurece o botão ao passar o mouse e volta ao normal ao sair
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(cor.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(cor); }
        });
        return btn;
    }

    //ação do botão "Excluir": remove o árbitro selecionado na tabela
    private void excluirArbitro() {
        int linha = tabela.getSelectedRow();
        //se nada estiver selecionado, avisa e não faz nada
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um árbitro na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //pega o id e o nome da linha selecionada
        int id = (int) modeloTabela.getValueAt(linha, 0);
        String nome = (String) modeloTabela.getValueAt(linha, 1);
        //pede confirmação antes de excluir
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o árbitro '" + nome + "'?", "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                controller.excluir(id);
                JOptionPane.showMessageDialog(this, "Árbitro excluído com sucesso!");
                //atualiza a tabela mantendo o filtro de busca atual
                carregarTabela(txtBusca.getText().trim());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //ação do botão "Salvar": valida os campos e cadastra o árbitro
    private void salvarArbitro() {
        try {
            //pega o texto digitado em cada campo
            String nome          = txtNome.getText().trim();
            String email         = txtEmail.getText().trim();
            String login         = txtLogin.getText().trim();
            String senha         = txtSenha.getText().trim();
            String cpf           = txtCpf.getText().trim();
            String nacionalidade = txtNacionalidade.getText().trim();
            int experiencia      = Integer.parseInt(txtExperiencia.getText().trim());

            //valida campos obrigatórios
            if (nome.isEmpty() || login.isEmpty() || senha.isEmpty() || nacionalidade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nome, login, senha e nacionalidade são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //valida regra da senha: mínimo 8 caracteres, com letra e número
            if (senha.length() < 8 || !senha.matches(".*[a-zA-Z].*") || !senha.matches(".*[0-9].*")) {
                JOptionPane.showMessageDialog(this, "Senha deve ter no mínimo 8 caracteres, com pelo menos uma letra e um número.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //valida formato do CPF (000.000.000-00)
            if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "CPF deve estar no formato 000.000.000-00.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            //se preencheu email, valida se tem "@" e um "." depois dele
            if (!email.isEmpty() && (!email.contains("@") || !email.substring(email.indexOf("@")).contains("."))) {
                JOptionPane.showMessageDialog(this, "Email inválido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ID gerado automaticamente pelo controller
            controller.cadastrar(nome, email, login, senha, cpf, nacionalidade, experiencia);

            JOptionPane.showMessageDialog(this, "Árbitro salvo com sucesso!");
            //limpa o formulário e recarrega a tabela já mostrando o novo árbitro
            limparFormulario();
            carregarTabela("");
            txtBusca.setText("");

        } catch (NumberFormatException e) {
            //cai aqui se o campo de experiência não tiver um número válido
            JOptionPane.showMessageDialog(this, "Experiência deve ser um número inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ExperienciaInvalidaException e) {
            //cai aqui se a experiência for negativa (regra de negócio do model Arbitro)
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    //quando o usuário clica numa linha da tabela, busca o árbitro pelo id e preenche o formulário com os dados dele
    private void carregarArbitroSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0) {
            try {
                int id = (int) modeloTabela.getValueAt(linha, 0);
                Arbitro a = controller.buscarPorId(id);
                txtNome.setText(a.getNome());
                txtEmail.setText(a.getEmail());
                txtLogin.setText(a.getLogin());
                txtSenha.setText(a.getSenha());
                txtCpf.setText(a.getCpf());
                txtNacionalidade.setText(a.getNacionalidade());
                txtExperiencia.setText(String.valueOf(a.getExperiencia()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar árbitro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //limpa todos os campos do formulário, deixando pronto pra um novo cadastro
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtCpf.setText("");
        txtNacionalidade.setText("");
        txtExperiencia.setText("");
    }
}