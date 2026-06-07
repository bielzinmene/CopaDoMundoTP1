package br.unb.cic.copa.model.aluno1;

// Subclasse de Usuario — representa uma conta que gerencia ingressos
// Acesso restrito: só acessa o módulo de ingressos e registros
public class Operador extends Usuario {

    public Operador(int id, String nome, String email, String login, String senha, String cpf, String pais) {
        super(id, nome, email, login, senha, cpf, pais, "Operador");
    }
}