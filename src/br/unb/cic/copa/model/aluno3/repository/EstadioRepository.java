package br.unb.cic.copa.model.aluno3.repository;

import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Persiste estádios em arquivo JSON
public class EstadioRepository implements Repositorio<Estadio> {

    private final String caminhoArquivo;

    public EstadioRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
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

        String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        return parseJson(conteudo);
    }

    @Override
    public void remover(int id) throws IOException {
        List<Estadio> lista = listarTodos();
        boolean removido = lista.removeIf(e -> e.getId() == id);
        if (!removido) throw new IOException("Estádio com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    // Converte lista de estádios para JSON e salva no arquivo
    private void escreverJson(List<Estadio> lista) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < lista.size(); i++) {
            Estadio e = lista.get(i);
            Localizacao loc = e.getLocalizacao();
            sb.append("  {\n");
            sb.append("    \"id\": ").append(e.getId()).append(",\n");
            sb.append("    \"nome\": \"").append(e.getNome()).append("\",\n");
            sb.append("    \"capacidade\": ").append(e.getCapacidade()).append(",\n");
            sb.append("    \"cidade\": \"").append(loc.getCidade()).append("\",\n");
            sb.append("    \"estado\": \"").append(loc.getEstado()).append("\",\n");
            sb.append("    \"pais\": \"").append(loc.getPais()).append("\",\n");
            sb.append("    \"endereco\": \"").append(loc.getEndereco()).append("\"\n");
            sb.append("  }");
            if (i < lista.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        new File(caminhoArquivo).getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write(sb.toString());
        }
    }

    // Lê o JSON e converte para lista de estádios
    private List<Estadio> parseJson(String json) throws IOException {
        List<Estadio> lista = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) return lista;

        json = json.substring(1, json.lastIndexOf("]")).trim();
        String[] objetos = json.split("\\},\\s*\\{");

        for (String obj : objetos) {
            obj = obj.replace("{", "").replace("}", "").trim();
            int id = 0, capacidade = 0;
            String nome = "", cidade = "", estado = "", pais = "", endereco = "";

            for (String linha : obj.split(",\\s*\\r?\\n")) {
                linha = linha.trim();
                if (linha.isEmpty()) continue;
                int colonPos = linha.indexOf(":");
                if (colonPos == -1) continue;
                String chave = linha.substring(0, colonPos).trim().replace("\"", "");
                String valor = linha.substring(colonPos + 1).trim().replaceAll("^\"|\"$|\",$|,$", "").trim();

                switch (chave) {
                    case "id":         id        = Integer.parseInt(valor); break;
                    case "nome":       nome      = valor; break;
                    case "capacidade": capacidade = Integer.parseInt(valor); break;
                    case "cidade":     cidade    = valor; break;
                    case "estado":     estado    = valor; break;
                    case "pais":       pais      = valor; break;
                    case "endereco":   endereco  = valor; break;
                }
            }

            try {
                Localizacao loc = new Localizacao(cidade, estado, PaisSede.valueOf(pais), endereco);
                lista.add(new Estadio(id, nome, loc, capacidade));
            } catch (CapacidadeInvalidaException ex) {
                throw new IOException("Erro ao carregar estádio: " + ex.getMessage(), ex);
            }
        }
        return lista;
    }
}