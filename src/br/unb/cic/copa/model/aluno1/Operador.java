package br.unb.cic.copa.model.aluno1;

public class Operador extends Usuario{
    public Operador(String nome, String email, String login, String senha, String identificacao, String pais) {
        super(nome, email, login, senha, identificacao, pais, "Operador");
    }
}
