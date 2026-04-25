package br.unb.cic.copa.model;

public class Administrador extends Usuario{
    public Administrador(String nome, String email, String login, String senha, String identificacao, String pais) {
        super(nome, email, login, senha, identificacao, pais, "Administrador");
    }
}
