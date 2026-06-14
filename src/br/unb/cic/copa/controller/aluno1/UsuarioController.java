package br.unb.cic.copa.controller.aluno1;

import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno1.exception.*;
import br.unb.cic.copa.model.aluno1.repository.UsuarioRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioController {

    private static UsuarioController instancia;
    private List<Usuario> usuarios;
    private final UsuarioRepository repository;

    private UsuarioController() {
        repository = new UsuarioRepository("src/dados/usuarios.json");
        try {
            usuarios = repository.listarTodos();
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

    public static UsuarioController getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioController();
        }
        return instancia;
    }

    public void recarregarUsuarios() {
        try {
            usuarios = repository.listarTodos();
        } catch (IOException e) {
            System.out.println("Erro ao recarregar usuários: " + e.getMessage());
        }
    }

    private void validarSenha(String senha) throws SenhaFracaException {
        if (senha == null || senha.length() < 8) throw new SenhaFracaException();
        boolean temLetra = false, temNumero = false;
        for (char c : senha.toCharArray()) {
            if (Character.isLetter(c)) temLetra = true;
            if (Character.isDigit(c)) temNumero = true;
        }
        if (!temLetra || !temNumero) throw new SenhaFracaException();
    }

    private void validarEmail(String email) throws EmailInvalidoException {
        if (email == null || email.isEmpty()) throw new EmailInvalidoException();
        if (!email.contains("@")) throw new EmailInvalidoException();
        String[] partes = email.split("@");
        if (partes.length != 2) throw new EmailInvalidoException();
        if (!partes[1].contains(".")) throw new EmailInvalidoException();
    }

    private void validarCpf(String cpf) throws CpfInvalidoException {
        if (cpf == null || cpf.isEmpty()) throw new CpfInvalidoException();
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))
            throw new CpfInvalidoException();
    }


    private void verificarEmailDuplicado(String email, int idIgnorar)
            throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getId() != idIgnorar) {
                throw new UsuarioDuplicadoException();
            }
        }
    }


    private void verificarLoginDuplicado(String login, int idIgnorar)
            throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getLogin().equalsIgnoreCase(login) && u.getId() != idIgnorar) {
                throw new UsuarioDuplicadoException();
            }
        }
    }


    private void verificarCpfDuplicado(String cpf, int idIgnorar)
            throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getCpf().equals(cpf) && u.getId() != idIgnorar) {
                throw new UsuarioDuplicadoException();
            }
        }
    }

    private void verificarPermissao(Usuario solicitante) throws AcessoNegadoException {
        if (!(solicitante instanceof Administrador)) {
            throw new AcessoNegadoException();
        }
    }


    public void cadastrarUsuario(Usuario solicitante, Usuario novoUsuario)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException,
            CpfInvalidoException, UsuarioDuplicadoException, IOException {

        verificarPermissao(solicitante);

        validarEmail(novoUsuario.getEmail());
        validarCpf(novoUsuario.getCpf());
        validarSenha(novoUsuario.getSenha());

        verificarEmailDuplicado(novoUsuario.getEmail(), -1);
        verificarLoginDuplicado(novoUsuario.getLogin(), -1);
        verificarCpfDuplicado(novoUsuario.getCpf(), -1);

        novoUsuario.setId(gerarNovoId());
        repository.salvar(novoUsuario);
        usuarios = repository.listarTodos();
    }

    public void editarUsuario(Usuario solicitante, Usuario usuarioEditado)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException,
            CpfInvalidoException, UsuarioNaoEncontradoException, UsuarioDuplicadoException,
            IOException {

        verificarPermissao(solicitante);

        boolean encontrado = false;
        for (Usuario u : usuarios) {
            if (u.getId() == usuarioEditado.getId()) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) throw new UsuarioNaoEncontradoException();

        validarEmail(usuarioEditado.getEmail());
        validarCpf(usuarioEditado.getCpf());
        validarSenha(usuarioEditado.getSenha());

        verificarEmailDuplicado(usuarioEditado.getEmail(), usuarioEditado.getId());
        verificarLoginDuplicado(usuarioEditado.getLogin(), usuarioEditado.getId());
        verificarCpfDuplicado(usuarioEditado.getCpf(), usuarioEditado.getId());

        repository.salvar(usuarioEditado);
        usuarios = repository.listarTodos();
    }

    public void excluirUsuario(Usuario solicitante, int id)
            throws AcessoNegadoException, UsuarioNaoEncontradoException, IOException {

        verificarPermissao(solicitante);

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

    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario autenticar(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.getSenha().equals(senha)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> pesquisar(String nome, String funcao, String pais, String status) {
        List<Usuario> resultado = new ArrayList<>();
        for (Usuario u : usuarios) {
            boolean ok = true;
            if (nome != null && !nome.isEmpty() &&
                    !u.getNome().toLowerCase().contains(nome.toLowerCase())) ok = false;
            if (funcao != null && !funcao.isEmpty() &&
                    !u.getFuncao().equalsIgnoreCase(funcao)) ok = false;
            if (pais != null && !pais.isEmpty() &&
                    !u.getPais().toLowerCase().contains(pais.toLowerCase())) ok = false;
            if (status != null && !status.isEmpty() &&
                    !u.getStatus().equalsIgnoreCase(status)) ok = false;
            if (ok) resultado.add(u);
        }
        return resultado;
    }


    private int gerarNovoId() {
        int maior = 0;
        for (Usuario u : usuarios) {
            if (u.getId() > maior) maior = u.getId();
        }
        return maior + 1;
    }
}