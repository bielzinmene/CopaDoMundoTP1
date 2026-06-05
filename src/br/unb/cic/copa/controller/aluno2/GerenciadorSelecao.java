package br.unb.cic.copa.controller.aluno2;

import br.unb.cic.copa.model.aluno2.*;
import br.unb.cic.copa.model.aluno2.exception.*;
import br.unb.cic.copa.model.aluno2.repository.SelecaoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorSelecao {
    private List<Selecao> selecoes;
    private final SelecaoRepository repositorio;

    public GerenciadorSelecao() {
        this.repositorio = new SelecaoRepository("src/dados/selecoes.json");
        try {
            selecoes = repositorio.carregarTodas();
            // Atualiza o contador de IDs da classe Selecao
            int maxId = selecoes.stream().mapToInt(Selecao::getId).max().orElse(0);
            Selecao.setUltimoId(maxId);
        } catch (IOException e) {
            selecoes = new ArrayList<>();
            System.err.println("Erro ao carregar seleções: " + e.getMessage());
        }
    }

    private void salvar() throws CopaException {
        try {
            repositorio.salvarTodas(selecoes);
        } catch (IOException e) {
            throw new CopaException("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // ------ CRUD Seleção ------
    public void adicionarSelecao(Selecao s) throws CopaException {
        if (buscarSelecaoPorNome(s.getNome()) != null) {
            throw new CopaException("Já existe seleção com o nome " + s.getNome());
        }
        selecoes.add(s);
        salvar();
    }

    public void removerSelecao(String nome) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nome);
        if (s == null) throw new CopaException("Seleção não encontrada: " + nome);
        // Desassociar jogadores
        for (Jogador j : s.getJogadores()) {
            j.setSelecao(null);
        }
        selecoes.remove(s);
        salvar();
    }

    public void editarSelecao(String nomeAntigo, String novoNome, String novoGrupo, String novoTecnico) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeAntigo);
        if (s == null) throw new CopaException("Seleção não encontrada: " + nomeAntigo);
        if (!nomeAntigo.equalsIgnoreCase(novoNome) && buscarSelecaoPorNome(novoNome) != null) {
            throw new CopaException("Já existe seleção com o nome " + novoNome);
        }
        s.setNome(novoNome);
        s.setGrupo(novoGrupo);
        s.setTecnico(novoTecnico);
        salvar();
    }

    public List<Selecao> listarTodas() {
        return new ArrayList<>(selecoes);
    }

    public Selecao buscarSelecaoPorNome(String nome) {
        if (nome == null || selecoes == null) return null;
        for (Selecao s : selecoes) {
            if (s != null && nome.equalsIgnoreCase(s.getNome())) return s;
        }
        return null;
    }

    public List<Selecao> buscarSelecoesPorGrupo(String grupo) {
        List<Selecao> resultado = new ArrayList<>();
        for (Selecao s : selecoes) {
            if (s.getGrupo().equalsIgnoreCase(grupo)) resultado.add(s);
        }
        return resultado;
    }

    // ------ Operações com jogadores ------
    public void adicionarJogador(String nomeSelecao, Jogador j) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");
        if (j.getSelecao() != null) {
            throw new CopaException("Jogador já pertence a outra seleção.");
        }
        s.adicionarJogador(j);
        salvar();
    }

    public void removerJogador(String nomeSelecao, String nomeJogador) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");
        s.removerJogadorPorNome(nomeJogador);
        salvar();
    }

    public void editarJogador(String nomeSelecao, String nomeJogadorAtual,
                              String novoNome, int novaNum, Posicao novaPos,
                              boolean novoTitular, StatusJogador novoStatus) throws CopaException {
        Selecao s = buscarSelecaoPorNome(nomeSelecao);
        if (s == null) throw new CopaException("Seleção não encontrada");

        Jogador j = null;
        for (Jogador jog : s.getJogadores()) {
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
            j.setNumeracao(novaNum);
        } catch (NumeroCamisaInvalidoException e) {
            throw new CopaException(e.getMessage());
        }
        j.setPosicao(novaPos);
        j.setTitular(novoTitular);
        j.setStatus(novoStatus);
        salvar();
    }
}