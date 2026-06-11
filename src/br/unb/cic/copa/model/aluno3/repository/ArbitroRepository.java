package br.unb.cic.copa.model.aluno3.repository;

import br.unb.cic.copa.model.aluno3.Arbitro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Persiste árbitros em arquivo JSON usando Gson
public class ArbitroRepository implements Repositorio<Arbitro> {

    private final String caminhoArquivo;
    private final Gson gson;

    public ArbitroRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        // setPrettyPrinting deixa o JSON formatado
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salvar(Arbitro arbitro) throws IOException {
        // carrega todos os árbitros já existentes no arquivo
        List<Arbitro> lista = listarTodos();

        boolean atualizado = false;
        // procura na lista um árbitro com o mesmo id do que está sendo salvo
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == arbitro.getId()) {
                //se encontrou substitui pelo atual
                lista.set(i, arbitro);
                atualizado = true;
                break;
            }
        }
        //cadastro novo, add ao final da lista
        if (!atualizado) {
            lista.add(arbitro);
        }
        //grava no json de novo
        escreverJson(lista);
    }

    @Override
    public Arbitro buscarPorId(int id) throws IOException {
        //percorre todos os arbitros procurando o id
        for (Arbitro a : listarTodos()) {
            if (a.getId() == id) return a;
        }
        //se não achou lança uma exceçao
        throw new IOException("Árbitro com id " + id + " não encontrado.");
    }

    //percorre todos os arbitros procurando o login (ignorando maiúsculas/minúsculas)
    public Arbitro buscarPorLogin(String login) throws IOException {
        for (Arbitro a : listarTodos()) {
            if (a.getLogin().equalsIgnoreCase(login)) return a;
        }
        //se não achou lança uma exceçao
        throw new IOException("Árbitro com login '" + login + "' não encontrado.");
    }

    @Override
    public List<Arbitro> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        //verifica se arquivo existe se não existe retorna lista vazia
        if (!arquivo.exists()) return new ArrayList<>();

        //lê o conteudo e verifica se está cadastrado
        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo))).trim();
        if (json.isEmpty() || json.equals("[]")) return new ArrayList<>();

        // TypeToken é necessário porque o Gson precisa saber o tipo genérico exato para converter
        Type tipoLista = new TypeToken<ArrayList<Arbitro>>() {}.getType();
        //converte json para lista de objetos
        List<Arbitro> lista = gson.fromJson(json, tipoLista);
        //se json era null, retorna lista vazia
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public void remover(int id) throws IOException {
        //pega todos os arbitros cadastrados
        List<Arbitro> lista = listarTodos();
        //se o id for igual ao informado remove da lista e retorna true
        boolean removido = lista.removeIf(a -> a.getId() == id);
        if (!removido) throw new IOException("Árbitro com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    //converte a lista para json e sobrescreve o conteudo anterior
    private void escreverJson(List<Arbitro> lista) throws IOException {
        String json = gson.toJson(lista);
        File arquivo = new File(caminhoArquivo);
        //garante a existencia das pastas no caminho
        arquivo.getParentFile().mkdirs();
        Files.write(Paths.get(caminhoArquivo), json.getBytes());
    }
}