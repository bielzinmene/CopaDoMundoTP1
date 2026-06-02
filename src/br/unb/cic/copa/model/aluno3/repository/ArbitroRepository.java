package br.unb.cic.copa.model.aluno3.repository;

import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Persiste árbitros em arquivo JSON
public class ArbitroRepository implements Repositorio<Arbitro> {

    private final String caminhoArquivo;

    public ArbitroRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
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

    @Override
    public List<Arbitro> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        return parseJson(conteudo);
    }

    @Override
    public void remover(int id) throws IOException {
        List<Arbitro> lista = listarTodos();
        boolean removido = lista.removeIf(a -> a.getId() == id);
        if (!removido) throw new IOException("Árbitro com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    // Converte lista de árbitros para JSON e salva no arquivo
    private void escreverJson(List<Arbitro> lista) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < lista.size(); i++) {
            Arbitro a = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(a.getId()).append(",\n");
            sb.append("    \"nome\": \"").append(a.getNome()).append("\",\n");
            sb.append("    \"nacionalidade\": \"").append(a.getNacionalidade()).append("\",\n");
            sb.append("    \"experiencia\": ").append(a.getExperiencia()).append("\n");
            sb.append("  }");
            if (i < lista.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        //para garantir que a pasta dados exista
        new File(caminhoArquivo).getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write(sb.toString());
        }
    }

    // Lê o JSON e converte para lista de árbitros
    private List<Arbitro> parseJson(String json) throws IOException {
        List<Arbitro> lista = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) return lista;

        // Remove colchetes externos e divide por objeto
        json = json.substring(1, json.lastIndexOf("]")).trim();
        String[] objetos = json.split("\\},\\s*\\{");

        for (String obj : objetos) {
            obj = obj.replace("{", "").replace("}", "").trim();
            int id = 0;
            String nome = "", nacionalidade = "";
            int experiencia = 0;

            for (String linha : obj.split(",\n")) {
                linha = linha.trim();
                if (linha.startsWith("\"id\"")) {
                    id = Integer.parseInt(linha.split(":")[1].trim());
                } else if (linha.startsWith("\"nome\"")) {
                    nome = linha.split(":", 2)[1].trim().replace("\"", "");
                } else if (linha.startsWith("\"nacionalidade\"")) {
                    nacionalidade = linha.split(":", 2)[1].trim().replace("\"", "");
                } else if (linha.startsWith("\"experiencia\"")) {
                    experiencia = Integer.parseInt(linha.split(":")[1].trim());
                }
            }

            try {
                lista.add(new Arbitro(id, nome, nacionalidade, experiencia));
            } catch (ExperienciaInvalidaException e) {
                throw new IOException("Erro ao carregar árbitro: " + e.getMessage(), e);
            }
        }
        return lista;
    }
}