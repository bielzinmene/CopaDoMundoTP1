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


public class SelecaoRepository implements Repositorio<Selecao> {

    private final String caminhoArquivo;
    private final Gson gson;

    public SelecaoRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    // --------------------------------------------------------------
    // Métodos de persistência principais (salvar e carregar)
    // --------------------------------------------------------------

    /*
      salva a lista completa no Gson
     */
    public void salvarTodas(List<Selecao> selecoes) throws IOException {
        String json = gson.toJson(selecoes); // converte a lista de seleções para string JSON

        File arquivo = new File(caminhoArquivo);
        arquivo.getParentFile().mkdirs(); // cria os diretórios pais se não existirem
        Files.write(Paths.get(caminhoArquivo), json.getBytes()); // escreve o JSON no arquivo
    }

    /*
      Carrega a lista completa de seleções do arquivo JSON.
      Se o arquivo não existir, retorna uma lista vazia.
     */
    public List<Selecao> carregarTodas() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            return new ArrayList<>(); // arquivo não existe, retorna lista vazia
        }
        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo))); // lê o arquivo
        Type tipoLista = new TypeToken<ArrayList<Selecao>>() {}.getType(); // define o tipo para desserialização
        List<Selecao> selecoes = gson.fromJson(json, tipoLista); // converte JSON para lista de objetos
        // reconstroi a referência bidirecional jogador -> seleção
        if (selecoes != null) {
            for (Selecao s : selecoes) {
                for (Jogador j : s.getJogadores()) {
                    j.setSelecao(s); // associa cada jogador à sua seleção
                }
            }
        }
        return selecoes != null ? selecoes : new ArrayList<>();
    }

    // --------------------------------------------------------------
    // implementação dos métodos da interface Selecao
    // --------------------------------------------------------------

    @Override
    public void salvar(Selecao selecao) throws IOException {
        List<Selecao> todas = listarTodos(); // carrega todas as seleções existentes
        // Atualiza se já existir (pelo ID), senão adiciona
        boolean encontrado = false;
        for (int i = 0; i < todas.size(); i++) {
            if (todas.get(i).getId() == selecao.getId()) { // verifica se ID já existe
                todas.set(i, selecao); // substitui a seleção antiga pela nova
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            todas.add(selecao); // adiciona nova seleção à lista
        }
        salvarTodas(todas); // persiste a lista atualizada
    }

    @Override
    public Selecao buscarPorId(int id) throws IOException {
        return listarTodos().stream() // carrega todas as seleções
                .filter(s -> s.getId() == id) // filtra pelo ID
                .findFirst() // pega o primeiro (único) que atende
                .orElseThrow(() -> new IOException("Seleção com id " + id + " não encontrada.")); // lança exceção se não achar
    }

    @Override
    public List<Selecao> listarTodos() throws IOException {
        return carregarTodas(); // delega para o carregamento
    }

    @Override
    public void remover(int id) throws IOException {
        List<Selecao> todas = listarTodos(); // carrega todas as seleções
        boolean removido = todas.removeIf(s -> s.getId() == id); // remove se ID corresponder
        if (!removido) {
            throw new IOException("Seleção com id " + id + " não encontrada.");
        }
        salvarTodas(todas); // salva a lista após remoção
    }

}