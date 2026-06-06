package br.unb.cic.copa.model.aluno1;

// Subclasse de Usuario — representa uma conta com acesso total ao sistema
// Herda todos os atributos e métodos de Usuario
// Só define a funcao como "Administrador" automaticamente
public class Administrador extends Usuario {

    // Chama o construtor pai (Usuario) via super()
    // passando "Administrador" como funcao fixo
    public Administrador(int id, String nome, String email, String login, String senha, String cpf, String pais) {
        super(id, nome, email, login, senha, cpf, pais, "Administrador");
    }
}