package br.unb.cic.copa.model.aluno1;

// Guarda o usuário logado durante toda a execução do programa
// Padrão Singleton — só existe uma sessão por vez
// Qualquer tela do projeto acessa via SessaoUsuario.getInstancia()
public class SessaoUsuario {

    private static SessaoUsuario instancia;
    private Usuario usuarioLogado;

    private SessaoUsuario() {}

    // Retorna a única instância da sessão
    public static SessaoUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SessaoUsuario();
        }
        return instancia;
    }

    // Salva quem acabou de logar
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    // Retorna quem está logado
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    // Limpa a sessão no logout
    public void encerrarSessao() {
        this.usuarioLogado = null;
    }
}