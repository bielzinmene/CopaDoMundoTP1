package br.unb.cic.copa.controller.aluno1;

import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno1.exception.*;
import br.unb.cic.copa.model.aluno1.repository.UsuarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// PADRÃO SINGLETON — garante que existe apenas UMA instância do controller
// durante toda a execução do programa
// Isso evita que dois lugares do código criem controllers diferentes
// e acabem com listas de usuários diferentes na memória
public class UsuarioController {

    // instancia única do controller — começa nula
    private static UsuarioController instancia;

    // lista de usuários carregada na memória
    private List<Usuario> usuarios;

    // repositório responsável por ler e escrever no arquivo JSON
    private final UsuarioRepository repository;

    // construtor PRIVADO — ninguém de fora pode criar um UsuarioController
    // só o próprio controller cria a si mesmo via getInstancia()
    private UsuarioController() {
        repository = new UsuarioRepository("src/dados/usuarios.json");
        try {
            usuarios = repository.listarTodos();

            // Se o arquivo estiver vazio, cria um admin padrão
            // Assim sempre existe pelo menos um admin para entrar no sistema
            if (usuarios.isEmpty()) {
                Usuario admin = new Administrador(
                        1, "Admin", "admin@copa.com",
                        "admin", "Admin123", "000.000.000-00", "Brasil"
                );
                repository.salvar(admin);
                usuarios = repository.listarTodos();
            }
        } catch (IOException e) {
            usuarios = new ArrayList<>();
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    // Método público para obter a instância única do controller
    // PADRÃO SINGLETON — se ainda não existe, cria. Se já existe, retorna a mesma
    public static UsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }


    // MÉTODOS DE VALIDAÇÃO — privados, usados internamente


    // Valida se a senha atende à política mínima de segurança
    // Regra: mínimo 8 caracteres, com pelo menos uma letra E um número
    private void validarSenha(String senha) throws SenhaFracaException {
        if (senha.length() < 8) throw new SenhaFracaException();

        boolean temLetra = false, temNumero = false;
        for (char c : senha.toCharArray()) {
            if (Character.isLetter(c)) temLetra = true;
            if (Character.isDigit(c)) temNumero = true;
        }
        if (!temLetra || !temNumero) throw new SenhaFracaException();
    }

    // Valida se o email tem formato correto
    // Regra: precisa ter @ e pelo menos um ponto depois do @
    private void validarEmail(String email) throws EmailInvalidoException {
        if (email == null || email.isEmpty()) throw new EmailInvalidoException();
        if (!email.contains("@")) throw new EmailInvalidoException();

        String[] partes = email.split("@");
        if (partes.length != 2) throw new EmailInvalidoException();
        if (!partes[1].contains(".")) throw new EmailInvalidoException();
    }

    // Valida se o CPF tem formato correto
    // Formato esperado: 000.000.000-00
    private void validarCpf(String cpf) throws CpfInvalidoException {
        if (cpf == null || cpf.isEmpty()) throw new CpfInvalidoException();

        // Verifica se segue o padrão 000.000.000-00
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            throw new CpfInvalidoException();
        }
    }

    // Verifica se já existe um usuário com o mesmo email cadastrado
    // Regra: cada email deve ser único no sistema
    private void verificarEmailDuplicado(String email) throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new UsuarioDuplicadoException();
            }
        }
    }

    // Verifica se o solicitante tem permissão de Administrador
    // Regra: só Admin pode cadastrar, editar ou excluir usuários
    private void verificarPermissao(Usuario solicitante) throws AcessoNegadoException {
        if (!(solicitante instanceof Administrador)) {
            throw new AcessoNegadoException();
        }
    }


    // MÉTODOS PÚBLICOS — operações do sistema


    // Cadastra um novo usuário no sistema
    // Só Admin pode chamar esse método
    // Valida email, CPF, senha e verifica se email já existe
    public void cadastrarUsuario(Usuario solicitante, Usuario novoUsuario)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException,
            CpfInvalidoException, UsuarioDuplicadoException, IOException {

        // 1. Verifica se quem está cadastrando é Admin
        verificarPermissao(solicitante);

        // 2. Valida os dados do novo usuário
        validarEmail(novoUsuario.getEmail());
        validarCpf(novoUsuario.getCpf());
        validarSenha(novoUsuario.getSenha());

        // 3. Verifica se email já está cadastrado
        verificarEmailDuplicado(novoUsuario.getEmail());

        // 4. Gera ID automático e salva
        novoUsuario.setId(gerarNovoId());
        repository.salvar(novoUsuario);
        usuarios = repository.listarTodos();
    }

    // Edita um usuário existente no sistema
    // Só Admin pode chamar esse método
    // Verifica se o usuário existe antes de editar
    public void editarUsuario(Usuario solicitante, Usuario usuarioEditado)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException,
            CpfInvalidoException, UsuarioNaoEncontradoException, IOException {

        // 1. Verifica permissão
        verificarPermissao(solicitante);

        // 2. Verifica se o usuário existe
        boolean encontrado = false;
        for (Usuario u : usuarios) {
            if (u.getId() == usuarioEditado.getId()) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) throw new UsuarioNaoEncontradoException();

        // 3. Valida os novos dados
        validarEmail(usuarioEditado.getEmail());
        validarCpf(usuarioEditado.getCpf());
        validarSenha(usuarioEditado.getSenha());

        // 4. Salva as alterações
        repository.salvar(usuarioEditado);
        usuarios = repository.listarTodos();
    }

    // Exclui um usuário do sistema pelo ID
    // Só Admin pode chamar esse método
    // Verifica se o usuário existe antes de excluir
    public void excluirUsuario(Usuario solicitante, int id)
            throws AcessoNegadoException, UsuarioNaoEncontradoException, IOException {

        // 1. Verifica permissão
        verificarPermissao(solicitante);

        // 2. Verifica se existe e exclui
        boolean encontrado = false;
        for (Usuario u : usuarios) {
            if (u.getId() == id) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) throw new UsuarioNaoEncontradoException();

        repository.remover(id);
        usuarios = repository.listarTodos();
    }

    // Retorna todos os usuários cadastrados
    // Retorna uma cópia da lista para não permitir modificações externas
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    // Autentica um usuário no sistema
    // Verifica se existe um usuário com o login e senha informados
    // Retorna o objeto Usuario se encontrar, null se não encontrar
    // A tela de login usa esse método para saber quem está entrando
    public Usuario autenticar(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.getSenha().equals(senha)) {
                return u; // retorna o usuário logado com seu tipo correto
            }
        }
        return null; // login ou senha incorretos
    }

    // Pesquisa usuários por múltiplos critérios
    // Qualquer campo pode ser nulo ou vazio — só filtra os que foram informados
    // Ex: pesquisar("Ana", null, "Brasil", null) → todos os Ana do Brasil
    public List<Usuario> pesquisar(String nome, String funcao, String pais, String status) {
        List<Usuario> resultado = new ArrayList<>();

        for (Usuario u : usuarios) {
            boolean ok = true;

            // Filtra por nome (busca parcial — "An" encontra "Ana")
            if (nome != null && !nome.isEmpty() &&
                    !u.getNome().toLowerCase().contains(nome.toLowerCase())) ok = false;

            // Filtra por função exata
            if (funcao != null && !funcao.isEmpty() &&
                    !u.getFuncao().equalsIgnoreCase(funcao)) ok = false;

            // Filtra por país (busca parcial)
            if (pais != null && !pais.isEmpty() &&
                    !u.getPais().toLowerCase().contains(pais.toLowerCase())) ok = false;

            // Filtra por status exato
            if (status != null && !status.isEmpty() &&
                    !u.getStatus().equalsIgnoreCase(status)) ok = false;

            if (ok) resultado.add(u);
        }
        return resultado;
    }

    // Gera relatório de usuários cadastrados
    // Retorna uma string formatada com todos os usuários
    // Usado na tela de relatórios
    public String gerarRelatorioUsuarios() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE USUÁRIOS ===\n\n");
        sb.append(String.format("%-5s %-20s %-30s %-15s %-10s\n",
                "ID", "Nome", "Email", "Função", "Status"));
        sb.append("-".repeat(80)).append("\n");

        for (Usuario u : usuarios) {
            sb.append(String.format("%-5d %-20s %-30s %-15s %-10s\n",
                    u.getId(), u.getNome(), u.getEmail(),
                    u.getFuncao(), u.getStatus()));
        }

        sb.append("\nTotal de usuários: ").append(usuarios.size());
        return sb.toString();
    }

    // Gera ID único para novo usuário
    // Pega o maior ID existente e soma 1
    private int gerarNovoId() {
        int maior = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maior) maior = u.getId();
        }
        return maior + 1;
    }
}