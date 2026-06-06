package br.unb.cic.copa.model.aluno1;

// Classe abstrata — não pode ser instanciada diretamente
// Serve como base para Administrador, Organizador e Operador
// "abstract" significa que toda conta do sistema TEM que ser um tipo específico
public abstract class Usuario {

    // private = só esta classe acessa diretamente
    // as subclasses usam os getters/setters para acessar
    private int id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private String cpf;
    private String pais;
    private String funcao;  // "Administrador", "Organizador" ou "Operador"
    private String status;  // "Ativo" ou "Inativo"

    // Construtor chamado pelas subclasses via super()
    public Usuario(int id, String nome, String email, String login, String senha, String cpf, String pais, String funcao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
        this.pais = pais;
        this.funcao = funcao;
        this.status = "Ativo"; // todo usuário começa ativo
    }

    // Getters e Setters — únicos pontos de acesso aos atributos privados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public String getFuncao() { return funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // toString usado para exibir o usuário na tabela e nos relatórios
    @Override
    public String toString() {
        return id + " | " + nome + " | " + email + " | " + funcao + " | " + status;
    }
}