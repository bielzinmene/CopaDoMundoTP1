package br.unb.cic.copa.model.aluno1;

public class Administrador extends Usuario {
    public Administrador(int id, String nome, String email, String login, String senha, String cpf, String pais) {
        super(id, nome, email, login, senha, cpf, pais, "Administrador");
    }
}