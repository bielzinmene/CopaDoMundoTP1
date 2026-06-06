package br.unb.cic.copa.model.aluno1.repository;

import br.unb.cic.copa.model.aluno1.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Responsável por ler e escrever usuários no arquivo JSON
// Implementa a interface Repositorio para seguir o padrão do projeto
public class UsuarioRepository implements Repositorio<Usuario> {

    // Caminho do arquivo onde os usuários serão salvos
    private final String caminhoArquivo;

    // Construtor recebe o caminho do arquivo
    // Ex: new UsuarioRepository("dados/usuarios.json")
    public UsuarioRepository(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    // Salva ou atualiza um usuário no arquivo
    // Se já existir um usuário com o mesmo ID, atualiza
    // Se não existir, adiciona ao final da lista
    @Override
    public void salvar(Usuario usuario) throws IOException {
        List<Usuario> lista = listarTodos();
        boolean atualizado = false;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId() == usuario.getId()) {
                lista.set(i, usuario); // substitui o existente
                atualizado = true;
                break;
            }
        }

        if (!atualizado) {
            lista.add(usuario); // adiciona novo
        }

        escreverJson(lista); // salva tudo no arquivo
    }

    // Busca um usuário pelo ID
    // Lança IOException se não encontrar
    @Override
    public Usuario buscarPorId(int id) throws IOException {
        for (Usuario u : listarTodos()) {
            if (u.getId() == id) return u;
        }
        throw new IOException("Usuário com id " + id + " não encontrado.");
    }

    // Lê todos os usuários do arquivo JSON
    // Retorna lista vazia se o arquivo não existir
    @Override
    public List<Usuario> listarTodos() throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        String conteudo = new String(Files.readAllBytes(Paths.get(caminhoArquivo)));
        if (conteudo.trim().isEmpty()) return new ArrayList<>();

        return parseJson(conteudo); // converte o texto JSON em objetos
    }

    // Remove um usuário pelo ID
    // Lança IOException se não encontrar
    @Override
    public void remover(int id) throws IOException {
        List<Usuario> lista = listarTodos();
        boolean removido = lista.removeIf(u -> u.getId() == id);
        if (!removido) throw new IOException("Usuário com id " + id + " não encontrado.");
        escreverJson(lista); // salva lista atualizada
    }

    // Converte a lista de usuários para formato JSON e escreve no arquivo
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

        // Cria a pasta "dados" se não existir
        new File(caminhoArquivo).getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            writer.write(sb.toString());
        }
    }

    // Trata caracteres especiais no JSON para não quebrar o arquivo
    private String escapeJson(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Converte o texto JSON lido do arquivo de volta para objetos Usuario
    private List<Usuario> parseJson(String json) throws IOException {
        List<Usuario> lista = new ArrayList<>();
        json = json.trim();
        if (json.equals("[]") || json.isEmpty()) return lista;

        // Remove os colchetes externos [ ]
        json = json.substring(1, json.lastIndexOf("]")).trim();

        // Divide em objetos individuais { }
        String[] objetos = splitJsonObjects(json);

        for (String obj : objetos) {
            obj = obj.trim().replace("{", "").replace("}", "").trim();

            int id = 0;
            String nome = "", email = "", login = "", senha = "", cpf = "", pais = "", funcao = "", status = "";

            // Lê cada linha do objeto e extrai chave e valor
            String[] linhas = obj.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String linha : linhas) {
                linha = linha.trim();
                int colonPos = linha.indexOf(":");
                if (colonPos == -1) continue;

                String chave = linha.substring(0, colonPos).trim().replace("\"", "");
                String valor = linha.substring(colonPos + 1).trim().replace("\"", "");

                // Atribui cada valor ao campo correto
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

            // Cria o objeto do tipo correto baseado na funcao
            Usuario u = criarUsuario(id, nome, email, login, senha, cpf, pais, funcao);
            u.setStatus(status.isEmpty() ? "Ativo" : status);
            lista.add(u);
        }
        return lista;
    }

    // Divide o JSON em objetos individuais respeitando as chaves { }
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

    // Padrão Factory — decide qual subclasse criar baseado na funcao
    // Isso é polimorfismo: trabalha com Usuario mas cria o tipo certo
    private Usuario criarUsuario(int id, String nome, String email, String login,
                                 String senha, String cpf, String pais, String funcao) {
        switch (funcao) {
            case "Administrador": return new Administrador(id, nome, email, login, senha, cpf, pais);
            case "Organizador":   return new Organizador(id, nome, email, login, senha, cpf, pais);
            case "Operador":      return new Operador(id, nome, email, login, senha, cpf, pais);
            default:              return new Organizador(id, nome, email, login, senha, cpf, pais);
        }
    }
}