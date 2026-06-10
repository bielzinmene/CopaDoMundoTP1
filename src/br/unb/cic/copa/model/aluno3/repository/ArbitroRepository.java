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
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void salvar(Arbitro arbitro) throws IOException {
        List<Arbitro> lista = listarTodos();

        boolean atualizado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == arbitro.getId()) {
                lista.set(i, arbitro);
                atualizado = true;
                break;
            }
        }
        if (!atualizado) {
            lista.add(arbitro);
        }

        escreverJson(lista);
    }

    @Override
    public Arbitro buscarPorId(int id) throws IOException {
        for (Arbitro a : listarTodos()) {
            if (a.getId() == id) return a;
        }
        throw new IOException("Árbitro com id " + id + " não encontrado.");
    }

    public Arbitro buscarPorLogin(String login) throws IOException {
        for (Arbitro a : listarTodos()) {
            if (a.getLogin().equalsIgnoreCase(login)) return a;
        }
        throw new IOException("Árbitro com login '" + login + "' não encontrado.");
    }

    @Override
    public List<Arbitro> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        String json = new String(Files.readAllBytes(Paths.get(caminhoArquivo))).trim();
        if (json.isEmpty() || json.equals("[]")) return new ArrayList<>();

        Type tipoLista = new TypeToken<ArrayList<Arbitro>>() {}.getType();
        List<Arbitro> lista = gson.fromJson(json, tipoLista);
        return lista != null ? lista : new ArrayList<>();
    }

    @Override
    public void remover(int id) throws IOException {
        List<Arbitro> lista = listarTodos();
        boolean removido = lista.removeIf(a -> a.getId() == id);
        if (!removido) throw new IOException("Árbitro com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    private void escreverJson(List<Arbitro> lista) throws IOException {
        String json = gson.toJson(lista);
        File arquivo = new File(caminhoArquivo);
        arquivo.getParentFile().mkdirs();
        Files.write(Paths.get(caminhoArquivo), json.getBytes());
    }
}