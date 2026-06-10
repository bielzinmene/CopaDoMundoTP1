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
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salvar(Estadio estadio) throws IOException {
        List<Estadio> lista = listarTodos();

        boolean atualizado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == estadio.getId()) {
                lista.set(i, estadio);
                atualizado = true;
                break;
            }
        }
        if (!atualizado) {
            lista.add(estadio);
        }

        escreverJson(lista);
    }

    @Override
    public Estadio buscarPorId(int id) throws IOException {
        for (Estadio e : listarTodos()) {
            if (e.getId() == id) return e;
        }
        throw new IOException("Estádio com id " + id + " não encontrado.");
    }

    @Override
    public List<Estadio> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo))).trim();
        if (json.isEmpty() || json.equals("[]")) return new ArrayList<>();

        Type tipoLista = new TypeToken<ArrayList<Estadio>>() {}.getType();
        List<Estadio> lista = gson.fromJson(json, tipoLista);
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public void remover(int id) throws IOException {
        List<Estadio> lista = listarTodos();
        boolean removido = lista.removeIf(e -> e.getId() == id);
        if (!removido) throw new IOException("Estádio com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    private void escreverJson(List<Estadio> lista) throws IOException {
        String json = gson.toJson(lista);
        File arquivo = new File(caminhoArquivo);
        arquivo.getParentFile().mkdirs();
        Files.write(Paths.get(caminhoArquivo), json.getBytes());
    }
}