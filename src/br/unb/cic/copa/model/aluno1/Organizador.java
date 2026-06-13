package br.unb.cic.copa.model.aluno1;

public class Organizador extends Usuario {

    public Organizador(int id, String nome, String email, String login, String senha, String cpf, String pais) {
        super(id, nome, email, login, senha, cpf, pais, "Organizador");
    }
}