package br.unb.cic.copa.model.aluno1;

public abstract class Usuario {
    protected int id;
    protected String nome;
    protected String email;
    protected String login;
    protected String senha;
    protected String cpf;
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

    
}