package br.unb.cic.copa.model.aluno3.repository;

import br.unb.cic.copa.model.aluno3.Estadio;
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

// Persiste estádios em arquivo JSON usando Gson
public class EstadioRepository implements Repositorio<Estadio> {

    private final String caminhoArquivo;
    private final Gson gson;

    public EstadioRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        // setPrettyPrinting deixa o JSON formatado
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salvar(Estadio estadio) throws IOException {
        // carrega todos os estádios já existentes no arquivo
        List<Estadio> lista = listarTodos();

        boolean atualizado = false;
        // procura na lista um estádio com o mesmo id do que está sendo salvo
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == estadio.getId()) {
                //se encontrou substitui pelo atual
                lista.set(i, estadio);
                atualizado = true;
                break;
            }
        }
        //cadastro novo, add ao final da lista
        if (!atualizado) {
            lista.add(estadio);
        }
        //grava no json de novo
        escreverJson(lista);
    }

    @Override
    public Estadio buscarPorId(int id) throws IOException {
        //percorre todos os estadios procurando o id
        for (Estadio e : listarTodos()) {
            if (e.getId() == id) return e;
        }
        //se não achou lança uma exceçao
        throw new IOException("Estádio com id " + id + " não encontrado.");
    }

    @Override
    public List<Estadio> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        //verifica se arquivo existe se não existe retorna lista vazia
        if (!arquivo.exists()) return new ArrayList<>();

        //lê o conteudo e verifica se está cadastrado
        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo))).trim();
        if (json.isEmpty() || json.equals("[]")) return new ArrayList<>();

        // TypeToken é necessário porque o Gson precisa saber o tipo genérico exato para converter
        Type tipoLista = new TypeToken<ArrayList<Estadio>>() {}.getType();
        //converte json para lista de objetos
        List<Estadio> lista = gson.fromJson(json, tipoLista);
        //se json era null, retorna lista vazia
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public void remover(int id) throws IOException {
        //pega todos os estadios cadastrados
        List<Estadio> lista = listarTodos();
        //se o id for igual ao informado remove da lista e retorna true
        boolean removido = lista.removeIf(e -> e.getId() == id);
        if (!removido) throw new IOException("Estádio com id " + id + " não encontrado.");
        escreverJson(lista);
    }


    //converte a lista para json na lista e sobrescreve o conteudo anterior
    private void escreverJson(List<Estadio> lista) throws IOException {
        String json = gson.toJson(lista);
        File arquivo = new File(caminhoArquivo);
        //garante a existencia das pastas no caminho
        arquivo.getParentFile().mkdirs();
        Files.write(Paths.get(caminhoArquivo), json.getBytes());
    }
}