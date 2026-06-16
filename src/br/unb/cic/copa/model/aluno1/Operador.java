package br.unb.cic.copa.model.aluno1;

public class Operador extends Usuario {

    public Operador(int id, String nome, String email, String login, String senha, String cpf, String pais) {
        super(id, nome, email, login, senha, cpf, pais, "Operador");
    }
}