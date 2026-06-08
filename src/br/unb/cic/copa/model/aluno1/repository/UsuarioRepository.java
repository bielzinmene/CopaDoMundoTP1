package br.unb.cic.copa.model.aluno1.repository;

import br.unb.cic.copa.model.aluno1.*;
import br.unb.cic.copa.model.aluno3.Arbitro;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository implements Repositorio<Usuario> {

    private final String caminhoArquivo;

    public UsuarioRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    public void salvar(Usuario usuario) throws IOException {
        List<Usuario> lista = listarTodos();
        boolean atualizado = false;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == usuario.getId()) {
                lista.set(i, usuario);
                atualizado = true;
                break;
            }
        }

        if (!atualizado) {
            lista.add(usuario);
        }

        escreverJson(lista);
    }

    @Override
    public Usuario buscarPorId(int id) throws IOException {
        for (Usuario u : listarTodos()) {
            if (u.getId() == id) return u;
        }
        throw new IOException("Usuário com id " + id + " não encontrado.");
    }

    @Override
    public List<Usuario> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        if (conteudo.trim().isEmpty()) return new ArrayList<>();

        return parseJson(conteudo);
    }

    @Override
    public void remover(int id) throws IOException {
        List<Usuario> lista = listarTodos();
        boolean removido = lista.removeIf(u -> u.getId() == id);
        if (!removido) throw new IOException("Usuário com id " + id + " não encontrado.");
        escreverJson(lista);
    }

    private void escreverJson(List<Usuario> lista) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(u.getId()).append(",\n");
            sb.append("    \"nome\": \"").append(escapeJson(u.getNome())).append("\",\n");
            sb.append("    \"email\": \"").append(escapeJson(u.getEmail())).append("\",\n");
            sb.append("    \"login\": \"").append(escapeJson(u.getLogin())).append("\",\n");
            sb.append("    \"senha\": \"").append(escapeJson(u.getSenha())).append("\",\n");
            sb.append("    \"cpf\": \"").append(escapeJson(u.getCpf())).append("\",\n");
            sb.append("    \"pais\": \"").append(escapeJson(u.getPais())).append("\",\n");
            sb.append("    \"funcao\": \"").append(escapeJson(u.getFuncao())).append("\",\n");
            sb.append("    \"status\": \"").append(escapeJson(u.getStatus())).append("\"\n");
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

    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private List<Usuario> parseJson(String json) throws IOException {
        List<Usuario> lista = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) return lista;

        json = json.substring(1, json.lastIndexOf("]")).trim();

        String[] objetos = splitJsonObjects(json);

        for (String obj : objetos) {
            obj = obj.trim().replace("{", "").replace("}", "").trim();

            int id = 0;
            String nome = "", email = "", login = "", senha = "", cpf = "", pais = "", funcao = "", status = "";

            String[] linhas = obj.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String linha : linhas) {
                linha = linha.trim();
                int colonPos = linha.indexOf(":");
                if (colonPos == -1) continue;

                String chave = linha.substring(0, colonPos).trim().replace("\"", "");
                String valor = linha.substring(colonPos + 1).trim().replace("\"", "");

                switch (chave) {
                    case "id": id = Integer.parseInt(valor); break;
                    case "nome": nome = valor; break;
                    case "email": email = valor; break;
                    case "login": login = valor; break;
                    case "senha": senha = valor; break;
                    case "cpf": cpf = valor; break;
                    case "pais": pais = valor; break;
                    case "funcao": funcao = valor; break;
                    case "status": status = valor; break;
                }
            }

            Usuario u = criarUsuario(id, nome, email, login, senha, cpf, pais, funcao);
            u.setStatus(status.isEmpty() ? "Ativo" : status);
            lista.add(u);
        }
        return lista;
    }

    private String[] splitJsonObjects(String json) {
        List<String> objetos = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int braceCount = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{') braceCount++;
                else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        current.append(c);
                        objetos.add(current.toString());
                        current = new StringBuilder();
                        i++;
                        continue;
                    }
                }
            }
            current.append(c);
        }
        return objetos.toArray(new String[0]);
    }

    private Usuario criarUsuario(int id, String nome, String email, String login,
                                 String senha, String cpf, String pais, String funcao) {
        switch (funcao) {
            case "Administrador": return new Administrador(id, nome, email, login, senha, cpf, pais);
            case "Organizador":   return new Organizador(id, nome, email, login, senha, cpf, pais);
            case "Operador":      return new Operador(id, nome, email, login, senha, cpf, pais);
            case "Arbitro":
                try {
                    return new Arbitro(id, nome, email, login, senha, cpf, pais);
                } catch (Exception e) {
                    return new Organizador(id, nome, email, login, senha, cpf, pais);
                }
            default: return new Organizador(id, nome, email, login, senha, cpf, pais);
        }
    }
}