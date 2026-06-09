package br.unb.cic.copa.model.aluno2.repository;

import br.unb.cic.copa.model.aluno2.Jogador;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.repository.Repositorio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório de Seleções usando Gson para persistência em JSON.
 * Gerencia o arquivo src/dados/selecoes.json
 */
public class SelecaoRepository implements Repositorio<Selecao> {

    private final String caminhoArquivo;
    private final Gson gson;

    public SelecaoRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        // Cria o Gson com formatação bonita (pretty printing) para facilitar leitura manual
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // --------------------------------------------------------------
    // Métodos de persistência principais (salvar e carregar)
    // --------------------------------------------------------------

    /**
     * Salva a lista completa de seleções no arquivo JSON.
     */
    public void salvarTodas(List<Selecao> selecoes) throws IOException {
        String json = gson.toJson(selecoes);

        File arquivo = new File(caminhoArquivo);
        arquivo.getParentFile().mkdirs(); // Garante que a pasta exista
        Files.write(Paths.get(caminhoArquivo), json.getBytes());
    }

    /**
     * Carrega a lista completa de seleções do arquivo JSON.
     * Se o arquivo não existir, retorna uma lista vazia.
     */
    public List<Selecao> carregarTodas() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        Type tipoLista = new TypeToken<ArrayList<Selecao>>() {}.getType();
        List<Selecao> selecoes = gson.fromJson(json, tipoLista);
        // Reconstroi a referência bidirecional jogador -> seleção (importante)
        if (selecoes != null) {
            for (Selecao s : selecoes) {
                for (Jogador j : s.getJogadores()) {
                    j.setSelecao(s);
                }
            }
        }
        return selecoes != null ? selecoes : new ArrayList<>();
    }

    // --------------------------------------------------------------
    // Implementação dos métodos da interface Repositorio<Selecao>
    // --------------------------------------------------------------

    @Override
    public void salvar(Selecao selecao) throws IOException {
        List<Selecao> todas = listarTodos();
        // Atualiza se já existir (pelo ID), senão adiciona
        boolean encontrado = false;
        for (int i = 0; i < todas.size(); i++) {
            if (todas.get(i).getId() == selecao.getId()) {
                todas.set(i, selecao);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            todas.add(selecao);
        }
        salvarTodas(todas);
    }

    @Override
    public Selecao buscarPorId(int id) throws IOException {
        return listarTodos().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IOException("Seleção com id " + id + " não encontrada."));
    }

    @Override
    public List<Selecao> listarTodos() throws IOException {
        return carregarTodas();
    }

    @Override
    public void remover(int id) throws IOException {
        List<Selecao> todas = listarTodos();
        boolean removido = todas.removeIf(s -> s.getId() == id);
        if (!removido) {
            throw new IOException("Seleção com id " + id + " não encontrada.");
        }
        salvarTodas(todas);
    }

    // --------------------------------------------------------------
    // Métodos auxiliares adicionais (para conveniência)
    // --------------------------------------------------------------

    public Selecao buscarPorNome(String nome) throws IOException {
        return listarTodos().stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    public void removerPorNome(String nome) throws IOException {
        List<Selecao> todas = listarTodos();
        boolean removido = todas.removeIf(s -> s.getNome().equalsIgnoreCase(nome));
        if (!removido) {
            throw new IOException("Seleção com nome " + nome + " não encontrada.");
        }
        salvarTodas(todas);
    }
}