package br.unb.cic.copa.model;

public class Organizador extends Usuario{
    public Organizador(String nome, String email, String login, String senha, String identificacao, String pais) {
        super(nome, email, login, senha, identificacao, pais, "Organizador");
    }
}

