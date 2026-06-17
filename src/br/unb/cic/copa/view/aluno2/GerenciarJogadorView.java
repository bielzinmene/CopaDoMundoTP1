package br.unb.cic.copa.view.aluno2;

import br.unb.cic.copa.model.aluno2.*;
import br.unb.cic.copa.controller.aluno2.SelecaoController;
import br.unb.cic.copa.model.aluno2.exception.CopaException;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Image;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GerenciarJogadorView extends JFrame { //declarando a tela de jogadores

    private JComboBox<String> comboSelecao;      //selecionar a seleção
    private JTextField txtNome;                   //nome do jogador
    private JTextField txtNumero;                 //número da camisa
    private JComboBox<Posicao> comboPosicao;      //posição do jogador
    private JComboBox<StatusJogador> comboStatus; //status
    private JCheckBox chkTitular;                 //é titular ou  n
    private JTable tabela;                        //tabela que mostra os jogadores
    private DefaultTableModel modeloTabela;       //dados da tabela
    private JTextField txtBusca;                  //buscar jogador por nome

    private final SelecaoController gerenciador = new SelecaoController(); //controlador das seleções
    private Selecao selecaoAtual;                 //selecao selecionada no combo

    private static final Color COR_FUNDO     = new Color(245, 245, 250);
    private static final Color COR_HEADER    = Color.BLACK;              //cabeçalho
    private static final Color COR_SALVAR    = new Color(34, 139, 34);
    private static final Color COR_CANCELAR  = new Color(180, 40, 40);
    private static final Color COR_BUSCA     = new Color(30, 100, 180);
    private static final Color COR_TEXTO_BTN = Color.WHITE;              //cor do texto dos botões
    private static final Font  FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13); //fonte  labels
    private static final Font  FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13); //fonte  texto
    private static final Font  FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);  //fonte  títulos

    public GerenciarJogadorView() { //cononstrutor da tela
        setTitle("Gerenciar Jogadores - Copa do Mundo 2026"); // titulo da janela
        setExtendedState(JFrame.MAXIMIZED_BOTH);              //maximiza a janela (tela cheia)
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);    //ao fechar, nao encerra o programa
        setLocationRelativeTo(null);                          //centraliza a janela na tela
        setResizable(false);                                  //impede redimensionar
        getContentPane().setBackground(COR_FUNDO);            //define a cor de fundo do painel principal
        setLayout(new BorderLayout());                        //usa BorderLayout para organizar os componentes

        //tenta carregar o ícone da janela da taça
        try{
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa2026.jpg");
            if(imgURL != null) setIconImage(new ImageIcon(imgURL).getImage()); // Aplica o ícone
        }catch (Exception ignored){} //se falhar, ignora o erro - não impede a execução

        add(criarHeader(), BorderLayout.NORTH);   //adiciona o cabeçalho na parte de cima
        add(criarCorpo(), BorderLayout.CENTER);   //adiciona o corpo no centro
        add(criarRodape(), BorderLayout.SOUTH);   //adiciona o rodapé (botões) embaixo

        carregarComboSelecoes(); //carrega os nomes das seleções na listagem
        setVisible(true);        //torna visivel
    }


    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel titulo = new JLabel("Gerenciar Jogadores"); //título "Gerenciar Jogadores"
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);//cor do texto branco
        header.add(titulo, BorderLayout.WEST);//adiciona o título à esquerda


        // Tenta carregar a imagem da taça
        try{
            java.net.URL imgURL = getClass().getClassLoader().getResource("resources/copa_3.png");
            if(imgURL != null){
                ImageIcon icone = new ImageIcon(imgURL);
                Image img = icone.getImage().getScaledInstance(50,60,Image.SCALE_SMOOTH);
                JLabel lblImagem = new JLabel(new ImageIcon((img)));
                header.add(lblImagem, BorderLayout.EAST);
            }
        } catch (Exception ignored) {}

        return header; //retorna esse painel
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
        gc.insets = new Insets(6, 5, 6, 5); //margens
        gc.anchor = GridBagConstraints.WEST; //alinha a esquerda

        comboSelecao = new JComboBox<>(); //combo para selecionar a seleção
        comboSelecao.setFont(FONTE_CAMPO);
        comboSelecao.setPreferredSize(new Dimension(250, 32)); //largura e altura
        comboSelecao.addActionListener(e -> carregarTabelaJogadores()); //ca/recarrega a tabela

        txtNome   = criarCampo(); //ome do jogador
        txtNumero = criarCampo(); //número da camisa
        comboPosicao = new JComboBox<>(Posicao.values()); //posições possíveis
        comboPosicao.setFont(FONTE_CAMPO);
        comboPosicao.setPreferredSize(new Dimension(250, 32));
        comboStatus = new JComboBox<>(StatusJogador.values());
        comboStatus.setFont(FONTE_CAMPO);
        comboStatus.setPreferredSize(new Dimension(250, 32));
        chkTitular = new JCheckBox("Titular");
        chkTitular.setFont(FONTE_LABEL);
        chkTitular.setBackground(COR_FUNDO);

        adicionarLinhaCombo(painel, gc, 0, "Seleção:", comboSelecao);
        adicionarLinha(painel, gc, 1, "Nome do Jogador:", txtNome);
        adicionarLinha(painel, gc, 2, "Número da Camisa:", txtNumero);
        adicionarLinhaCombo(painel, gc, 3, "Posição:", comboPosicao);
        adicionarLinhaCombo(painel, gc, 4, "Status:", comboStatus);
        adicionarCheckBox(painel, gc, 5, chkTitular);

        return painel;
    }

    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(COR_FUNDO);

        // Busca por nome
        JPanel linhaBusca = new JPanel(new BorderLayout(8, 0));
        linhaBusca.setBackground(COR_FUNDO);

        JLabel labelBusca = new JLabel("Buscar jogador por nome:");
        labelBusca.setFont(FONTE_LABEL);
        labelBusca.setForeground(new Color(50, 50, 80));

        txtBusca = criarCampo(); //cria o campo de texto para digitar o nome
        JButton btnBuscar = criarBotao("Buscar", COR_BUSCA); //
        btnBuscar.setPreferredSize(new Dimension(85, 32));
        btnBuscar.addActionListener(e -> carregarTabelaJogadores()); // Ao clicar, recarrega a tabela com o filtro

        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() { //listener para teclas
            public void keyReleased(java.awt.event.KeyEvent e) { //quando uma tecla é solta
                carregarTabelaJogadores(); //carrega tabela a cada digitação
            }
        });

        linhaBusca.add(labelBusca, BorderLayout.WEST);
        linhaBusca.add(txtBusca, BorderLayout.CENTER);
        linhaBusca.add(btnBuscar, BorderLayout.EAST);


        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Número", "Posição", "Titular", "Status"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tabela = new JTable(modeloTabela);
        tabela.setFont(FONTE_CAMPO);
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabela.getTableHeader().setBackground(COR_HEADER);
        tabela.getTableHeader().setForeground(Color.WHITE);
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if(!isSelected){
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240,240,248)); //linhas zebradas: branco e cinza
                }
                return c;
            }
        });
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarJogadorSelecionado(); //quando seleciona uma linha, carrega os dados no formulário
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 200));

        // Botão excluir
        JButton btnExcluir = criarBotao("Excluir", COR_CANCELAR);
        btnExcluir.setPreferredSize(new Dimension(110, 32));
        btnExcluir.addActionListener(e -> excluirJogador()); //ao clicar, exclui o jogador selecionado

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        painelBotoes.setBackground(COR_FUNDO);
        painelBotoes.add(btnExcluir); //adiciona o botão excluir

        painel.add(linhaBusca, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);

        return painel;
    }

    private void carregarComboSelecoes() {
        comboSelecao.removeAllItems(); // limpa as opções anteriores do combo
        for (Selecao s : gerenciador.listarTodas()) { // percorre todas as seleções via controller
            comboSelecao.addItem(s.getNome()); // adiciona o nome da seleção ao combo
        }
        if (comboSelecao.getItemCount() > 0) { // verifica se há pelo menos uma seleção
            comboSelecao.setSelectedIndex(0); // seleciona a primeira
            carregarTabelaJogadores(); // carrega os jogadores da primeira seleção
        }
    }

    private void carregarTabelaJogadores() {
        modeloTabela.setRowCount(0);//limpa todas as linhas da tabela que exibe os jogadores.
        String nomeSelecao = (String) comboSelecao.getSelectedItem();//pega o nome da seleção que o usuário escolheu
        if (nomeSelecao == null) return;
        selecaoAtual = gerenciador.buscarSelecaoPorNome(nomeSelecao);//procura na lista em memória e retorna o objeto Selecao correspondente.
        if (selecaoAtual == null) return;

        String filtro = txtBusca.getText().trim(); //pega o texto de busca (nome do jogador)
        List<Jogador> jogadoresFiltrados;

        if (filtro.isEmpty()) {
            jogadoresFiltrados = selecaoAtual.getJogadores(); // todos os jogadores da seleção
        } else {
            jogadoresFiltrados = selecaoAtual.buscarJogadoresPorNome(filtro); // aplica filtro pelo metodo
        }

        for (Jogador j : jogadoresFiltrados) { // percorre a lista de jogadores
            modeloTabela.addRow(new Object[]{
                    j.getNome(),
                    j.getNumeracao(),
                    j.getPosicao(),
                    j.isTitular() ? "Sim" : "Não",
                    j.getStatus()
            });
        }
    }

    private void carregarJogadorSelecionado() {
        int linha = tabela.getSelectedRow(); //pega a linha selecionada na tabela
        if (linha < 0 || selecaoAtual == null) return;

        String nome = (String) modeloTabela.getValueAt(linha, 0); // nome do jogador na linha
        Jogador j = null;
        for (Jogador jog : selecaoAtual.getJogadores()) { // busca o objeto Jogador na lista da seleção
            if (jog.getNome().equals(nome)) {
                j = jog;
                break;
            }
        }
        if (j != null) { //preenche o formulário com os dados do jogador encontrado
            txtNome.setText(j.getNome());
            txtNumero.setText(String.valueOf(j.getNumeracao()));
            comboPosicao.setSelectedItem(j.getPosicao());
            chkTitular.setSelected(j.isTitular());
            comboStatus.setSelectedItem(j.getStatus());
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

    private void adicionarLinhaCombo(JPanel painel, GridBagConstraints gc, int linha, String labelTxt, JComboBox<?> combo) {
        JLabel label = new JLabel(labelTxt);
        label.setFont(FONTE_LABEL);
        label.setForeground(new Color(50, 50, 80));
        gc.gridx = 0; gc.gridy = linha; gc.weightx = 0.3; gc.fill = GridBagConstraints.NONE;
        painel.add(label, gc);
        gc.gridx = 1; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(combo, gc);
    }

    private void adicionarCheckBox(JPanel painel, GridBagConstraints gc, int linha, JCheckBox chk) {
        gc.gridx = 1; gc.gridy = linha; gc.weightx = 0.7; gc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(chk, gc);
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
        JButton btnLimpar = criarBotao("Limpar", Color.BLUE);
        JButton btnSalvarJog   = criarBotao("Salvar", COR_SALVAR);

        getRootPane().setDefaultButton(btnSalvarJog); // define que pressionar Enter aciona o botão Salvar

        btnCancelar.addActionListener(e -> { // ação do botão Cancelar
            new MenuPrincipalView().setVisible(true); // abre o menu principal
            dispose(); // fecha a janela atual
        });

        btnSalvarJog.addActionListener(e -> salvarJogador()); // ação do botão Salvar: chama o o metodo salvarJogador
        btnLimpar.addActionListener(e -> limparFormulario());

        rodape.add(btnCancelar);
        rodape.add(btnLimpar);
        rodape.add(btnSalvarJog);
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

    private void excluirJogador() {
        int linha = tabela.getSelectedRow(); //pega a linha selecionada
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um jogador na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nomeJogador = (String) modeloTabela.getValueAt(linha, 0); // nome do jogador
        String nomeSelecao = (String) comboSelecao.getSelectedItem(); // nome da seleção atual
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Deseja excluir o jogador '" + nomeJogador + "' da seleção " + nomeSelecao + "?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) { // usuário confirmou exclusão
            try {
                gerenciador.removerJogador(nomeSelecao, nomeJogador); // remove via controller
                JOptionPane.showMessageDialog(this, "Jogador excluído com sucesso!");
                carregarTabelaJogadores(); // recarrega a tabela
                limparFormulario(); // limpa os campos do formulário
            } catch (CopaException e) { // captura exceção de negócio
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void salvarJogador() {
        String nomeSelecao = (String) comboSelecao.getSelectedItem(); // seleção atual
        if (nomeSelecao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma seleção.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = txtNome.getText().trim(); // nome digitado
        String numStr = txtNumero.getText().trim(); // número da camisa (string)
        Posicao posicao = (Posicao) comboPosicao.getSelectedItem(); // posição escolhida
        StatusJogador status = (StatusJogador) comboStatus.getSelectedItem(); // status escolhido
        boolean titular = chkTitular.isSelected(); // se é titular

        if (nome.isEmpty() || numStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e número são obrigatórios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numStr); // converte número para inteiro
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Número deve ser inteiro.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int linha = tabela.getSelectedRow(); // verifica se há linha selecionada (modo edição)
        try {
            if (linha >= 0) {
                // Editar jogador existente
                String nomeAntigo = (String) modeloTabela.getValueAt(linha, 0);
                gerenciador.editarJogador(nomeSelecao, nomeAntigo, nome, numero, posicao, titular, status);
            } else {
                // Adicionar novo jogador
                Jogador novoJogador = new Jogador(nome, numero, posicao, titular);
                novoJogador.setStatus(status);
                gerenciador.adicionarJogador(nomeSelecao, novoJogador);
            }
            JOptionPane.showMessageDialog(this, "Jogador salvo com sucesso!");
            carregarTabelaJogadores(); // recarrega a tabela
            limparFormulario(); // limpa o formulário
        } catch (CopaException e) { // captura exceções de regras de negócio
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { // captura outras exceções inesperadas
            JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtNumero.setText("");
        comboPosicao.setSelectedIndex(0);
        chkTitular.setSelected(false);
        comboStatus.setSelectedIndex(0);
        tabela.clearSelection();
    }
}