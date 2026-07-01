package br.unb.cic.copa.model.aluno4.repository;

import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno4.Fase;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.StatusPartida;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PartidaRepository {

    private static final String ARQUIVO = "src/dados/partidas.json";
    // como não há necessidade de fazer uma sobrescrita, esse método já é declarado como final
    public PartidaRepository() {
        File file = new File(ARQUIVO);
        file.getParentFile().mkdirs();
        try {
            if (!file.exists()) {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
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
    // pecorre a lista de partidas e compara o ID de cada uma
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
                String sel1Nome    = p.getSelecao1() != null ? p.getSelecao1().getNome() : "";
                String sel2Nome    = p.getSelecao2() != null ? p.getSelecao2().getNome() : "";
                String estadioNome = p.getEstadio()  != null ? p.getEstadio().getNome()  : "";
                String fase        = p.getFase()      != null ? p.getFase().name()        : "GRUPOS";

                String json = "  {\n" +
                        "    \"id\": "              + p.getId()           + ",\n" +
                        "    \"arbitroId\": "       + p.getArbitroId()    + ",\n" +
                        "    \"fase\": \""          + fase                + "\",\n" +
                        "    \"status\": \""        + p.getStatus()       + "\",\n" +
                        "    \"data\": \""          + p.getData()         + "\",\n" +
                        "    \"selecao1Nome\": \""  + sel1Nome            + "\",\n" +
                        "    \"selecao2Nome\": \""  + sel2Nome            + "\",\n" +
                        "    \"estadioNome\": \""   + estadioNome         + "\",\n" +
                        "    \"golsSelecao1\": "    + p.getGolsSelecao1() + ",\n" +
                        "    \"golsSelecao2\": "    + p.getGolsSelecao2() + "\n"  +
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
        File file = new File(ARQUIVO);
        if (!file.exists()) return partidas;

        try {
            String conteudo = new String(Files.readAllBytes(Paths.get(ARQUIVO))).trim();
            if (conteudo.equals("[]") || conteudo.isEmpty()) return partidas;

            conteudo = conteudo.substring(1, conteudo.lastIndexOf("]")).trim();
            String[] objetos = conteudo.split("\\},\\s*\\{");

            for (String obj : objetos) {
                obj = obj.replace("{", "").replace("}", "").trim();
                int id = 0, arbitroId = 0, gols1 = 0, gols2 = 0;
                String fase = "", status = "", data = "", sel1Nome = "", sel2Nome = "", estadioNome = "";

                for (String linha : obj.split(",\\s*\\r?\\n")) {
                    linha = linha.trim();
                    if (linha.isEmpty()) continue;
                    int colonPos = linha.indexOf(":");
                    if (colonPos == -1) continue;
                    String chave = linha.substring(0, colonPos).trim().replace("\"", "");
                    String valor = linha.substring(colonPos + 1).trim().replaceAll("^\"|\"$|\",$|,$", "").trim();

                    switch (chave) {
                        case "id":           id        = Integer.parseInt(valor); break;
                        case "arbitroId":    arbitroId = Integer.parseInt(valor); break;
                        case "fase":         fase      = valor; break;
                        case "status":       status    = valor; break;
                        case "data":         data      = valor; break;
                        case "selecao1Nome": sel1Nome  = valor; break;
                        case "selecao2Nome": sel2Nome  = valor; break;
                        case "estadioNome":  estadioNome = valor; break;
                        case "golsSelecao1": gols1     = Integer.parseInt(valor); break;
                        case "golsSelecao2": gols2     = Integer.parseInt(valor); break;
                    }
                }

                try {
                    Selecao sel1 = new Selecao(sel1Nome, "", "");
                    Selecao sel2 = new Selecao(sel2Nome, "", "");
                    Localizacao loc = new Localizacao("", "", PaisSede.ESTADOS_UNIDOS, "");
                    Estadio estadio = new Estadio(0, estadioNome, loc, 1);
                    Fase faseEnum = Fase.valueOf(fase);

                    Partida p = new Partida(sel1, sel2, data, estadio, faseEnum);
                    p.setId(id);
                    p.setArbitroId(arbitroId);

                    if ("EM_ANDAMENTO".equals(status)) {
                        p.iniciarPartida();
                    } else if ("FINALIZADA".equals(status)) {
                        p.iniciarPartida();
                        p.finalizarPartida(new br.unb.cic.copa.model.aluno4.Resultado(gols1, gols2));
                    }

                    partidas.add(p);
                } catch (Exception e) {
                    System.out.println("[ERRO ao carregar partida]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return partidas;
    }

    public List<Partida> listarPorArbitro(int arbitroId) {
        List<Partida> resultado = new ArrayList<>();
        for (Partida p : listarTodos()) {
            if (p.getArbitroId() == arbitroId) resultado.add(p);
        }
        return resultado;
    }
}