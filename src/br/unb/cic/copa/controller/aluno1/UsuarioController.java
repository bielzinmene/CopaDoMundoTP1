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
    private UsuarioRepository repository;

    private UsuarioController() {
        repository = new UsuarioRepository("dados/usuarios.json");
        try {
            usuarios = repository.listarTodos();
            if (usuarios.isEmpty()) {
                Usuario admin = new Administrador(1, "Admin", "admin@copa.com", "admin", "Admin123", "000.000.000-00", "Brasil");
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

    private void validarSenha(String senha) throws SenhaFracaException {
        if (senha.length() < 8) throw new SenhaFracaException();
        boolean temLetra = false, temNumero = false;
        for (char c : senha.toCharArray()) {
            if (Character.isLetter(c)) temLetra = true;
            if (Character.isDigit(c)) temNumero = true;
        }
        if (!temLetra || !temNumero) throw new SenhaFracaException();
    }

    private void validarEmail(String email) throws EmailInvalidoException {
        if (!email.contains("@") || !email.contains(".")) throw new EmailInvalidoException();
    }

    private void verificarEmailDuplicado(String email) throws UsuarioDuplicadoException {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) throw new UsuarioDuplicadoException();
        }
    }

    private void verificarPermissao(Usuario solicitante) throws AcessoNegadoException {
        if (!(solicitante instanceof Administrador)) throw new AcessoNegadoException();
    }

    public void cadastrarUsuario(Usuario solicitante, Usuario novoUsuario)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException, UsuarioDuplicadoException, IOException {
        verificarPermissao(solicitante);
        validarEmail(novoUsuario.getEmail());
        validarSenha(novoUsuario.getSenha());
        verificarEmailDuplicado(novoUsuario.getEmail());
        novoUsuario.setId(gerarNovoId());
        repository.salvar(novoUsuario);
        usuarios = repository.listarTodos();
    }

    public void editarUsuario(Usuario solicitante, Usuario usuarioEditado)
            throws AcessoNegadoException, SenhaFracaException, EmailInvalidoException, UsuarioNaoEncontradoException, IOException {
        verificarPermissao(solicitante);

        boolean encontrado = false;
        for (Usuario u : usuarios) {
            if (u.getId() == usuarioEditado.getId()) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new UsuarioNaoEncontradoException();
        }

        validarEmail(usuarioEditado.getEmail());
        validarSenha(usuarioEditado.getSenha());
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
        if (!encontrado) {
            throw new UsuarioNaoEncontradoException();
        }

        try {
            repository.remover(id);
            usuarios = repository.listarTodos();
        } catch (IOException e) {
            throw new UsuarioNaoEncontradoException();
        }
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
            if (nome != null && !nome.isEmpty() && !u.getNome().toLowerCase().contains(nome.toLowerCase())) ok = false;
            if (funcao != null && !funcao.isEmpty() && !u.getFuncao().equalsIgnoreCase(funcao)) ok = false;
            if (pais != null && !pais.isEmpty() && !u.getPais().toLowerCase().contains(pais.toLowerCase())) ok = false;
            if (status != null && !status.isEmpty() && !u.getStatus().equalsIgnoreCase(status)) ok = false;
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