package br.unb.cic.copa.repository.aluno4;

import br.unb.cic.copa.model.aluno4.Partida;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PartidaRepository {

    private static final String ARQUIVO = "src/dados/partidas.json";

    public PartidaRepository() {
        File file = new File(ARQUIVO);
        file.getParentFile().mkdirs();
        try {
            if (!file.exists()) {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("[]");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Partida partida) {
        List<Partida> partidas = listarTodos();
        partidas.removeIf(p -> p.getId() == partida.getId());
        partidas.add(partida);
        salvarArquivo(partidas);
    }

    public Partida buscarPorId(int id) {
        for (Partida p : listarTodos()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public void remover(int id) {
        List<Partida> partidas = listarTodos();
        partidas.removeIf(p -> p.getId() == id);
        salvarArquivo(partidas);
    }

    private void salvarArquivo(List<Partida> partidas) {
        try (FileWriter writer = new FileWriter(ARQUIVO)) {
            writer.write("[\n");
            for (int i = 0; i < partidas.size(); i++) {
                Partida p = partidas.get(i);

                // Salvando apenas os IDs das selecoes e estadio para simplificar o JSON
                String json = "  {\n" +
                        "    \"id\": " + p.getId() + ",\n" +
                        "    \"fase\": \"" + p.getFase() + "\",\n" +
                        "    \"status\": \"" + p.getStatus() + "\",\n" +
                        "    \"data\": \"" + p.getData() + "\",\n" +
                        "    \"idSelecao1\": " + (p.getSelecao1() != null ? 1 : 0) + ",\n" +
                        "    \"idSelecao2\": " + (p.getSelecao2() != null ? 2 : 0) + "\n" +
                        "  }";
                writer.write(json);
                if (i < partidas.size() - 1) writer.write(",\n");
            }
            writer.write("\n]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Partida> listarTodos() {
        List<Partida> partidas = new ArrayList<>();
        // A lógica de leitura exata vai depender de como vocês estão mockando as seleções,
        // mas a estrutura base do BufferedReader entra aqui.
        return partidas;
    }
}