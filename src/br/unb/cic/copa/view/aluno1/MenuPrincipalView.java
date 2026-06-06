// Define o pacote onde a interface gráfica do Menu Principal está localizada
package br.unb.cic.copa.view.aluno1;

// Importa as classes do modelo do Aluno 1 (Usuario, Administrador, Organizador, Operador)
import br.unb.cic.copa.model.aluno1.*;
// Importa as telas desenvolvidas pelos outros integrantes do grupo para permitir a integração
import br.unb.cic.copa.view.aluno2.GerenciarJogadorView;
import br.unb.cic.copa.view.aluno2.GerenciarSelecaoView;
import br.unb.cic.copa.view.aluno3.GerenciarArbitroView;
import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;
import br.unb.cic.copa.view.aluno4.PartidaView;
import br.unb.cic.copa.view.aluno5.VendaIngressoView;
import br.unb.cic.copa.view.aluno3.ConsultarEscalaArbitroView;
import br.unb.cic.copa.model.aluno1.SessaoUsuario;

// Importa a biblioteca padrão do Java Swing para construção de interfaces gráficas
import javax.swing.*;
// Importa classes para definição de bordas vazias (espaçamentos)
import javax.swing.border.EmptyBorder;
// Importa a biblioteca Abstract Window Toolkit (AWT) para gerenciar cores, fontes e layouts
import java.awt.*;

/**
 * Classe MenuPrincipalView
 * Representa a janela principal do sistema após a autenticação bem-sucedida.
 * Utiliza herança da classe JFrame para se tornar uma janela Swing.
 */



public class MenuPrincipalView extends JFrame {

    // Atributo encapsulado (private e final) que guarda o usuário autenticado na sessão atual.
    // Essencial para aplicar as regras de controle de acesso aos recursos do sistema.
    // CORREÇÃO: removido o construtor vazio que deixava este campo sem inicialização,
    // causando o aviso "Field might not have been initialized".
    private final Usuario usuarioLogado;

    public MenuPrincipalView() {
        this(SessaoUsuario.getInstancia().getUsuarioLogado());
    }

    // Definição de constantes de cores para manter o padrão visual Clean/Moderno da Copa 2026
    private static final Color COR_FUNDO     = new Color(245, 245, 250); // Azul acinzentado bem claro
    private static final Color COR_HEADER    = new Color(30, 60, 120);   // Azul escuro corporativo
    private static final Color COR_BOTAO     = new Color(30, 100, 180);  // Azul Royal para ações padrão
    private static final Color COR_SAIR      = new Color(180, 40, 40);   // Vermelho para botões de saída/alerta
    private static final Color COR_TEXTO_BTN = Color.WHITE;              // Texto do botão em branco

    // Definição de fontes padronizadas usando a família Segoe UI
    private static final Font FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONTE_USUARIO = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Único construtor da classe — recebe obrigatoriamente o usuário autenticado.
     * Não existe construtor vazio pois o menu nunca deve ser aberto sem um usuário logado.
     * @param usuarioLogado Objeto contendo o usuário que acabou de logar no sistema.
     */
    public MenuPrincipalView(Usuario usuarioLogado) {
        // Recebe o usuário validado e armazena no campo final da classe
        this.usuarioLogado = usuarioLogado;

        // Configurações básicas do JFrame (Janela)
        setTitle("Sistema de Gestão da Copa 2026"); // Título da barra da janela
        setSize(850, 600);                          // Define a resolução da tela (Largura x Altura)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Encerra o processo ao fechar a janela
        setLocationRelativeTo(null);                // Centraliza a janela no meio do monitor
        setResizable(false);                        // Impede o usuário de redimensionar e quebrar o layout
        getContentPane().setBackground(COR_FUNDO);  // Aplica a cor de fundo no container principal

        // Aplica o gerenciador de layout BorderLayout (divide a tela em Norte, Sul, Leste, Oeste e Centro)
        setLayout(new BorderLayout());

        // Adiciona os componentes visuais divididos por regiões de responsabilidade
        add(criarHeader(), BorderLayout.NORTH);  // Cabeçalho informativo no topo
        add(criarCorpo(), BorderLayout.CENTER);  // Painel com a grade de botões no meio
        add(criarRodape(), BorderLayout.SOUTH);  // Barra de status no rodapé
    }

    /**
     * Cria e estiliza o painel superior (Cabeçalho) com título e dados do usuário logado.
     */
    private JPanel criarHeader() {
        // Cria o painel do cabeçalho com BorderLayout interno
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COR_HEADER); // Define a cor azul escura
        // Adiciona margem interna para os elementos não colarem nas bordas
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Título principal do sistema posicionado à esquerda
        JLabel titulo = new JLabel("  Sistema de Gestão da Copa do Mundo 2026");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(Color.WHITE);

        // Rótulo com o Nome e a Função do usuário logado (ex: "Ana — Administrador")
        JLabel lblUsuario = new JLabel(
                usuarioLogado.getNome() + " — " + usuarioLogado.getFuncao() + "  "
        );
        lblUsuario.setFont(FONTE_USUARIO);
        lblUsuario.setForeground(new Color(200, 220, 255)); // Azul claro para contraste

        // Organiza os componentes dentro do painel do cabeçalho
        header.add(titulo, BorderLayout.WEST);     // Título alinhado à esquerda
        header.add(lblUsuario, BorderLayout.EAST); // Dados do usuário alinhados à direita

        return header;
    }

    /**
     * Cria o miolo central da tela com a grade de botões do menu.
     */
    private JPanel criarCorpo() {
        // Painel base usando GridBagLayout apenas para centralizar a grade na tela
        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setBackground(COR_FUNDO);
        corpo.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Grade de 3 linhas x 3 colunas com espaçamento de 20px entre os botões
        JPanel grade = new JPanel(new GridLayout(3, 3, 20, 20));
        grade.setBackground(COR_FUNDO);

        // Criação dos botões usando o método utilitário criarBotao()
        JButton btnUsuarios   = criarBotao(" Gestão de Usuários", COR_BOTAO);
        JButton btnSelecoes   = criarBotao(" Gerenciar Seleções", COR_BOTAO);
        JButton btnJogadores  = criarBotao(" Gerenciar Jogadores", COR_BOTAO);
        JButton btnEstadios   = criarBotao(" Gerenciar Estádios", COR_BOTAO);
        JButton btnArbitros   = criarBotao(" Gerenciar Árbitros", COR_BOTAO);
        JButton btnPartidas   = criarBotao(" Gerenciar Partidas", COR_BOTAO);
        JButton btnIngressos  = criarBotao(" Gerenciar Ingressos", COR_BOTAO);
        JButton btnRelatorios = criarBotao(" Relatórios", COR_BOTAO);
        JButton btnSair       = criarBotao(" Sair / Logout", COR_SAIR);

        // Aplica as regras de controle de acesso — habilita ou desabilita botões
        // conforme o perfil do usuário logado (Administrador, Organizador ou Operador)
        configurarAcesso(btnUsuarios, btnSelecoes, btnJogadores,
                btnEstadios, btnArbitros, btnPartidas,
                btnIngressos, btnRelatorios);


        // MAPEAMENTO DE EVENTOS DE CLIQUE (LISTENERS)


        // Abre a tela de Gestão de Usuários (Módulo do Aluno 1)
        btnUsuarios.addActionListener(evt -> {
            new GerenciarUsuariosView(usuarioLogado).setVisible(true);
            dispose(); // Fecha o menu para liberar memória
        });

        // Abre o módulo de Seleções (Módulo do Aluno 2)
        btnSelecoes.addActionListener(evt -> {
            new GerenciarSelecaoView().setVisible(true);
            dispose();
        });

        // Abre o módulo de Jogadores (Módulo do Aluno 2)
        btnJogadores.addActionListener(evt -> {
            new GerenciarJogadorView().setVisible(true);
            dispose();
        });

        // Abre o módulo de Estádios (Módulo do Aluno 3)
        btnEstadios.addActionListener(evt -> {
            new GerenciarEstadioView().setVisible(true);
            dispose();
        });

        // Abre o módulo de Árbitros (Módulo do Aluno 3)
        btnArbitros.addActionListener(evt -> {
            new GerenciarArbitroView().setVisible(true);
            dispose();
        });

        // Abre o módulo de Partidas (Módulo do Aluno 4)
        btnPartidas.addActionListener(evt -> {
            new PartidaView().setVisible(true);
            dispose();
        });

        // Abre o módulo de Venda de Ingressos (Módulo do Aluno 5)
        btnIngressos.addActionListener(evt -> {
            new VendaIngressoView().setVisible(true);
            dispose();
        });

        // Relatórios: módulo ainda não implementado — exibe aviso informativo
        // Quando a RelatorioView estiver pronta, substitua pelo código abaixo:
        // btnRelatorios.addActionListener(evt -> { new RelatorioView(usuarioLogado).setVisible(true); dispose(); });
        btnRelatorios.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this,
                    "O módulo de Relatórios Gerais está em fase de modelagem\n" +
                            "e será implementado na etapa seguinte.",
                    "Módulo em Desenvolvimento",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Botão Sair: pede confirmação antes de encerrar a sessão
        btnSair.addActionListener(evt -> {
            int confirmacao = JOptionPane.showConfirmDialog(this,
                    "Deseja realmente sair do sistema?",
                    "Confirmar saída",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                // NOVO: limpa a sessão ao sair
                SessaoUsuario.getInstancia().encerrarSessao();
                new LoginView().setVisible(true);
                dispose();
            }
        });

        // Adiciona os botões na grade na ordem visual desejada
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

    /**
     * Controle de acesso baseado em polimorfismo (instanceof).
     * Todos os botões começam desabilitados e só são liberados conforme o perfil:
     *   - Administrador: acesso total
     *   - Organizador:   seleções, jogadores, estádios, árbitros e partidas
     *   - Operador:      apenas ingressos
     */
    private void configurarAcesso(JButton btnUsuarios, JButton btnSelecoes,
                                  JButton btnJogadores, JButton btnEstadios,
                                  JButton btnArbitros, JButton btnPartidas,
                                  JButton btnIngressos, JButton btnRelatorios) {

        // Política defensiva: desabilita tudo primeiro
        btnUsuarios.setEnabled(false);
        btnSelecoes.setEnabled(false);
        btnJogadores.setEnabled(false);
        btnEstadios.setEnabled(false);
        btnArbitros.setEnabled(false);
        btnPartidas.setEnabled(false);
        btnIngressos.setEnabled(false);
        btnRelatorios.setEnabled(false);

        // Libera apenas o que cada perfil pode acessar
        if (usuarioLogado instanceof Administrador) {
            // Administrador tem acesso irrestrito a tudo
            btnUsuarios.setEnabled(true);
            btnSelecoes.setEnabled(true);
            btnJogadores.setEnabled(true);
            btnEstadios.setEnabled(true);
            btnArbitros.setEnabled(true);
            btnPartidas.setEnabled(true);
            btnIngressos.setEnabled(true);
            btnRelatorios.setEnabled(true);

        } else if (usuarioLogado instanceof Organizador) {
            // Organizador gerencia o evento, mas não mexe em usuários, ingressos ou relatórios
            btnSelecoes.setEnabled(true);
            btnJogadores.setEnabled(true);
            btnEstadios.setEnabled(true);
            btnArbitros.setEnabled(true);
            btnPartidas.setEnabled(true);

        } else if (usuarioLogado instanceof Operador) {
            // Operador atua somente na bilheteria
            btnIngressos.setEnabled(true);

        } else {
            // Perfil desconhecido — acesso mínimo apenas para visualizar partidas
            btnPartidas.setEnabled(true);
        }
    }

    /**
     * Cria a barra inferior de status (Rodapé) com informações da sessão ativa.
     */
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        rodape.setBackground(COR_FUNDO);
        // Linha fina no topo do rodapé para separação visual
        rodape.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 220)));

        // Exibe nome e perfil do usuário logado na sessão atual
        JLabel lblInfo = new JLabel(
                "Logado como: " + usuarioLogado.getNome() +
                        " | Perfil: " + usuarioLogado.getFuncao()
        );
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(100, 100, 130));

        rodape.add(lblInfo);
        return rodape;
    }

    /**
     * Método utilitário (Factory Method simplificado) para criação padronizada de botões.
     * Centraliza toda a estilização para evitar repetição de código.
     * @param texto Rótulo exibido no botão
     * @param cor   Cor de fundo do botão
     * @return JButton estilizado e pronto para uso
     */
    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(cor);
        btn.setForeground(COR_TEXTO_BTN);
        btn.setFocusPainted(false);  // Remove o quadrado tracejado de foco do Windows
        btn.setBorderPainted(false); // Remove a borda 3D padrão do Swing
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Cursor de mãozinha
        btn.setDisabledIcon(null);   // Garante comportamento correto quando desabilitado
        btn.setOpaque(true);         // Necessário para a cor de fundo funcionar em todos os SO

        return btn;
    }
}