package br.unb.cic.copa.controller.aluno3;

import br.unb.cic.copa.model.aluno1.repository.UsuarioRepository;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno3.repository.ArbitroRepository;

import java.io.IOException;
import java.util.List;

// Centraliza as operações de árbitro, fazendo a ponte entre a View e os Repositórios
public class ArbitroController {

    private final ArbitroRepository arbitroRepository;
    private final UsuarioRepository usuarioRepository;

    public ArbitroController() {
        this.arbitroRepository = new ArbitroRepository("src/dados/arbitros.json");
        this.usuarioRepository = new UsuarioRepository("src/dados/usuarios.json");
    }

    // Cadastra um árbitro com ID gerado automaticamente
    // País é preenchido igual à nacionalidade (são equivalentes para árbitros)
    public void cadastrar(String nome, String email, String login,
                          String senha, String cpf,
                          String nacionalidade, int experiencia)
            throws ExperienciaInvalidaException, IOException {

        int id = gerarNovoId();
        Arbitro arbitro = new Arbitro(id, nome, email, login, senha, cpf, nacionalidade, nacionalidade, experiencia);
        arbitroRepository.salvar(arbitro);
        usuarioRepository.salvar(arbitro);
    }

    // Busca árbitro pelo ID
    public Arbitro buscarPorId(int id) throws IOException {
        return arbitroRepository.buscarPorId(id);
    }

    // Retorna todos os árbitros cadastrados
    public List<Arbitro> listarTodos() throws IOException {
        return arbitroRepository.listarTodos();
    }

    // Remove o árbitro dos dois arquivos
    public void excluir(int id) throws IOException {
        arbitroRepository.remover(id);
        try {
            usuarioRepository.remover(id);
        } catch (IOException ignored) {
            // árbitro pode não estar em usuarios.json em registros antigos
        }
    }

    // Gera ID único: pega o maior ID existente e soma 1
    private int gerarNovoId() throws IOException {
        List<Arbitro> lista = arbitroRepository.listarTodos();
        int maior = 0;
        for (Arbitro a : lista) {
            if (a.getId() > maior) maior = a.getId();
        }
        return maior + 1;
    }
}