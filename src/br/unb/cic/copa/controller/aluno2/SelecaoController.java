package br.unb.cic.copa.controller.aluno2;

import br.unb.cic.copa.model.aluno2.*;
import br.unb.cic.copa.model.aluno2.exception.*;
import br.unb.cic.copa.model.aluno2.repository.SelecaoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelecaoController {
    private List<Selecao> selecoes;
    private final SelecaoRepository repositorio;

    public SelecaoController() {
        this.repositorio = new SelecaoRepository("src/dados/selecoes.json");
        try {
            selecoes = repositorio.carregarTodas(); // carrega as seleções do arquivo JSON ao iniciar
        } catch (IOException e) {
            selecoes = new ArrayList<>(); // em caso de erro, começa com lista vazia
            System.err.println("Erro ao carregar seleções: " + e.getMessage());
        }
    }

    private void salvar() throws CopaException {
        try {
            repositorio.salvarTodas(selecoes); // persiste a lista inteira no JSON
        } catch (IOException e) {
            throw new CopaException("Erro ao salvar dados: " + e.getMessage()); // encapsula exceção de I/O
        }
    }

    private int gerarNovoId() {
        int max = selecoes.stream().mapToInt(Selecao::getId).max().orElse(0); // encontra o maior ID atual
        return max + 1; // próximo ID sequencial
    }

    // ------ CRUD Seleção ------

    public void adicionarSelecao(Selecao s) throws CopaException {
        if (buscarSelecaoPorNome(s.getNome()) != null) { // verifica se já existe seleção com o mesmo nome
            throw new CopaException("Já existe seleção com o nome " + s.getNome());
        }

        s.setId(gerarNovoId()); // atribui um novo ID antes de adicionar
        selecoes.add(s); // adiciona à lista em memória
        salvar(); // persiste a alteração
    }

    public void removerSelecao(String nome) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nome);
        if (s == null) throw new CopaException("Seleção não encontrada: " + nome);
        // Desassociar jogadores
        for (Jogador j : s.getJogadores()) {
            j.setSelecao(null); // remove a referência da seleção em cada jogador
        }
        selecoes.remove(s); // remove a seleção da lista
        salvar(); // persiste a alteração
    }

    public void editarSelecao(String nomeAntigo, String novoNome, String novoGrupo, String novoTecnico) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeAntigo);
        if (s == null) throw new CopaException("Seleção não encontrada: " + nomeAntigo);
        if (!nomeAntigo.equalsIgnoreCase(novoNome) && buscarSelecaoPorNome(novoNome) != null) { // se o nome for alterado, verifica se o novo nome já existe
            throw new CopaException("Já existe seleção com o nome " + novoNome);
        }
        s.setNome(novoNome);
        s.setGrupo(novoGrupo);
        s.setTecnico(novoTecnico);
        salvar(); // persiste a alteração
    }

    public List<Selecao> listarTodas() {
        return new ArrayList<>(selecoes); // retorna uma cópia da lista (evita modificações externas)
    }

    public Selecao buscarSelecaoPorNome(String nome) {
        if (nome == null || selecoes == null) return null;
        for (Selecao s : selecoes) {
            if (s != null && nome.equalsIgnoreCase(s.getNome())) return s; // busca case-insensitive
        }
        return null;
    }

    // ------ Operações com jogadores ------

    public void adicionarJogador(String nomeSelecao, Jogador j) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");
        if (j.getSelecao() != null) { // garante que o jogador não esteja vinculado a outra seleção
            throw new CopaException("Jogador já pertence a outra seleção.");
        }
        s.adicionarJogador(j); // delega a adição à seleção (verifica limites e duplicidade)
        salvar(); // persiste a alteração
    }

    public void removerJogador(String nomeSelecao, String nomeJogador) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");
        s.removerJogadorPorNome(nomeJogador); // delega a remoção à seleção
        salvar(); // persiste a alteração
    }

    public void editarJogador(String nomeSelecao, String nomeJogadorAtual, String novoNome, int novaNum, Posicao novaPos,
                              boolean novoTitular, StatusJogador novoStatus) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");

        Jogador j = null;
        for (Jogador jog : s.getJogadores()) { // localiza o jogador pelo nome atual
            if (jog.getNome().equalsIgnoreCase(nomeJogadorAtual)) {
                j = jog;
                break;
            }
        }
        if (j == null) throw new CopaException("Jogador não encontrado: " + nomeJogadorAtual);

        // Se a numeração mudou, verificar se já existe
        if (novaNum != j.getNumeracao() && s.numeroJaExiste(novaNum)) {
            throw new CopaException("A numeração " + novaNum + " já está em uso nesta seleção.");
        }

        j.setNome(novoNome);
        try {
            j.setNumeracao(novaNum); // pode lançar NumeroCamisaInvalidoException
        } catch (NumeroCamisaInvalidoException e) {
            throw new CopaException(e.getMessage()); // converte para exceção de negócio
        }
        j.setPosicao(novaPos);
        j.setTitular(novoTitular);
        j.setStatus(novoStatus);
        salvar(); // persiste a alteração
    }
}