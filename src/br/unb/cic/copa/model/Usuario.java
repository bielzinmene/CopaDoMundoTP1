package br.unb.cic.copa.model;

public abstract class Usuario {
    protected String nome;
    protected String email;
    protected String login;
    protected String senha;
    protected String identificacao;
    protected String pais;
    protected String funcao;
    protected String status;

    public Usuario(String nome, String email, String login, String senha, String identificacao, String pais, String funcao) {
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.identificacao = identificacao;
        this.pais = pais;
        this.funcao = funcao;
        this.status = "Ativo";
    }

    public String getNome() {
        return nome;
    }
    public String getEmail() {
        return email;
    }
    public String getLogin() {
        return login;
    }
    public String getSenha() {
        return senha;
    }
    public String getIdentificacao() {
        return identificacao;
    }
    public String getPais() {
        return pais;
    }
    public String getFuncao() {
        return funcao;
    }
    public String getStatus() {
        return status;
    }

}