package br.unb.cic.copa.model.aluno2.repository;

import br.unb.cic.copa.model.aluno2.*;
import br.unb.cic.copa.model.aluno2.exception.*;
import br.unb.cic.copa.model.aluno3.repository.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SelecaoRepository implements Repositorio<Selecao> {

    private final String caminhoArquivo;

    public SelecaoRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        System.out.println(">>> Caminho do arquivo: " + new File(caminhoArquivo).getAbsolutePath());
    }

    // --------------------------------------------------------------
    // Métodos da interface
    // --------------------------------------------------------------

    @Override
    public void salvar(Selecao selecao) throws IOException {
        List<Selecao> lista = listarTodos();
        boolean atualizado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == selecao.getId()) {
                lista.set(i, selecao);
                atualizado = true;
                break;
            }
        }
        if (!atualizado) {
            lista.add(selecao);
        }
        escreverJson(lista);
    }

    @Override
    public Selecao buscarPorId(int id) throws IOException {
        for (Selecao s : listarTodos()) {
            if (s.getId() == id) return s;
        }
        throw new IOException("Seleção com id " + id + " não encontrada.");
    }

    @Override
    public List<Selecao> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();
        String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        return parseJson(conteudo);
    }

    @Override
    public void remover(int id) throws IOException {
        List<Selecao> lista = listarTodos();
        boolean removido = lista.removeIf(s -> s.getId() == id);
        if (!removido) throw new IOException("Seleção com id " + id + " não encontrada.");
        escreverJson(lista);
    }

    // --------------------------------------------------------------
    // Métodos auxiliares para JSON manual (sem bibliotecas)
    // --------------------------------------------------------------

    private void escreverJson(List<Selecao> lista) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < lista.size(); i++) {
            Selecao s = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(s.getId()).append(",\n");
            sb.append("    \"nome\": \"").append(escapeJson(s.getNome())).append("\",\n");
            sb.append("    \"grupo\": \"").append(escapeJson(s.getGrupo())).append("\",\n");
            sb.append("    \"tecnico\": \"").append(escapeJson(s.getTecnico())).append("\",\n");
            sb.append("    \"jogadores\": [\n");
            List<Jogador> jogadores = s.getJogadores();
            for (int j = 0; j < jogadores.size(); j++) {
                Jogador jog = jogadores.get(j);
                sb.append("      {\n");
                sb.append("        \"nome\": \"").append(escapeJson(jog.getNome())).append("\",\n");
                sb.append("        \"numeracao\": ").append(jog.getNumeracao()).append(",\n");
                sb.append("        \"posicao\": \"").append(jog.getPosicao().name()).append("\",\n");
                sb.append("        \"titular\": ").append(jog.isTitular()).append(",\n");
                sb.append("        \"status\": \"").append(jog.getStatus().name()).append("\"\n");
                sb.append("      }");
                if (j < jogadores.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("    ]\n");
            sb.append("  }");
            if (i < lista.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        // Garante que a pasta exista
        new File(caminhoArquivo).getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write(sb.toString());
        }
    }

    private List<Selecao> parseJson(String json) throws IOException {
        List<Selecao> lista = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) return lista;

        // Remove os colchetes externos
        json = json.substring(1, json.lastIndexOf("]")).trim();
        // Divide os objetos (cada seleção está entre { } )
        String[] objetos = json.split("\\},\\s*\\{");

        for (String obj : objetos) {
            obj = obj.replace("{", "").replace("}", "").trim();
            Selecao s = new Selecao(); // construtor vazio
            List<Jogador> jogadores = new ArrayList<>();

            // Extrai a parte do array de jogadores
            String arrayJogadores = "";
            int inicioArray = obj.indexOf("[");
            int fimArray = obj.lastIndexOf("]");
            if (inicioArray != -1 && fimArray != -1 && fimArray > inicioArray) {
                arrayJogadores = obj.substring(inicioArray + 1, fimArray).trim();
                // Remove a parte do array do obj principal
                obj = obj.substring(0, inicioArray) + obj.substring(fimArray + 1);
            }

            // Processa os campos simples (id, nome, grupo, tecnico)
            String[] linhas = obj.split(",\\s*");
            for (String linha : linhas) {
                linha = linha.trim();
                if (linha.startsWith("\"id\"")) {
                    int id = Integer.parseInt(linha.split(":", 2)[1].trim());
                    s.setId(id);
                } else if (linha.startsWith("\"nome\"")) {
                    String nome = linha.split(":", 2)[1].trim().replace("\"", "");
                    s.setNome(nome);
                } else if (linha.startsWith("\"grupo\"")) {
                    String grupo = linha.split(":", 2)[1].trim().replace("\"", "");
                    s.setGrupo(grupo);
                } else if (linha.startsWith("\"tecnico\"")) {
                    String tecnico = linha.split(":", 2)[1].trim().replace("\"", "");
                    s.setTecnico(tecnico);
                }
            }

            // Processa o array de jogadores
            if (!arrayJogadores.isEmpty()) {
                String[] jogObjs = arrayJogadores.split("\\},\\s*\\{");
                for (String jogObj : jogObjs) {
                    jogObj = jogObj.replace("{", "").replace("}", "").trim();
                    Jogador j = new Jogador(); // construtor vazio
                    String[] camposJog = jogObj.split(",\\s*");
                    boolean invalido = false;

                    for (String campo : camposJog) {
                        campo = campo.trim();
                        try {
                            if (campo.startsWith("\"nome\"")) {
                                String nome = campo.split(":", 2)[1].trim().replace("\"", "");
                                j.setNome(nome);
                            } else if (campo.startsWith("\"numeracao\"")) {
                                int num = Integer.parseInt(campo.split(":", 2)[1].trim());
                                j.setNumeracao(num);  // pode lançar NumeroCamisaInvalidoException
                            } else if (campo.startsWith("\"posicao\"")) {
                                String pos = campo.split(":", 2)[1].trim().replace("\"", "");
                                j.setPosicao(Posicao.valueOf(pos));
                            } else if (campo.startsWith("\"titular\"")) {
                                j.setTitular(Boolean.parseBoolean(campo.split(":", 2)[1].trim()));
                            } else if (campo.startsWith("\"status\"")) {
                                String status = campo.split(":", 2)[1].trim().replace("\"", "");
                                j.setStatus(StatusJogador.valueOf(status));
                            }
                        } catch (NumeroCamisaInvalidoException e) {
                            System.err.println("Ignorando jogador inválido: " + e.getMessage());
                            invalido = true;
                            break;
                        } catch (IllegalArgumentException e) {
                            System.err.println("Ignorando jogador com dado inválido: " + e.getMessage());
                            invalido = true;
                            break;
                        }
                    }
                    if (!invalido) {
                        j.setSelecao(s);
                        jogadores.add(j);
                    }
                }
            }
            s.setJogadores(jogadores);
            lista.add(s);
        }
        return lista;
    }

    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // --------------------------------------------------------------
    // Métodos adicionais
    // --------------------------------------------------------------
    public void salvarTodas(List<Selecao> selecoes) throws IOException {
        escreverJson(selecoes);
    }

    public List<Selecao> carregarTodas() throws IOException {
        return listarTodos();
    }

    public Selecao buscarPorNome(String nome) throws IOException {
        for (Selecao s : listarTodos()) {
            if (s.getNome().equalsIgnoreCase(nome)) return s;
        }
        return null;
    }

    public void removerPorNome(String nome) throws IOException {
        List<Selecao> todas = listarTodos();
        boolean removido = todas.removeIf(s -> s.getNome().equalsIgnoreCase(nome));
        if (!removido) throw new IOException("Seleção " + nome + " não encontrada.");
        escreverJson(todas);
    }
}