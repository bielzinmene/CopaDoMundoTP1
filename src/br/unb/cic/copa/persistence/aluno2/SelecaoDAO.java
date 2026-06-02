package br.unb.cic.copa.persistence.aluno2;

import br.unb.cic.copa.model.aluno2.Jogador;
import br.unb.cic.copa.model.aluno2.Posicao;
import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno2.StatusJogador;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SelecaoDAO {
    // ========== PERSISTÊNCIA EM TXT ==========

    /**
     * Salva todas as seleções (e seus jogadores) em um arquivo texto.
     * Formato:
     * S;nome;grupo;tecnico
     * J;nome;numero;posicao;titular;status;nomeSelecao
     * ...
     */

    public static void salvarTodas(List<Selecao> selecoes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("selecoes.txt"))) {
            for (Selecao s : selecoes) {
                writer.println("S;" + s.getNome() + ";" + s.getGrupo() + ";" + s.getTecnico());
                for (Jogador j : s.getJogadores()) {
                    writer.println("J;" + j.getNome() + ";" + j.getNumeracao() + ";"
                            + j.getPosicao().name() + ";" + j.isTitular() + ";"
                            + j.getStatus().name() + ";" + s.getNome());
                }
            }
            System.out.println("Dados salvos com sucesso em 'selecoes.txt'.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
        }
    }

    /**
     * Carrega todas as seleções a partir do arquivo texto.
     * Se o arquivo não existir, retorna lista vazia.
     * Jogadores inválidos (ex: número duplicado, limite excedido) são ignorados.
     */

    public static List<Selecao> carregarTodas() {
        List<Selecao> selecoes = new ArrayList<>();
        File arquivo = new File("selecoes.txt");
        if (!arquivo.exists()) {
            return selecoes; // arquivo ainda não existe
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            Selecao selecaoAtual = null;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 0) continue;

                if (partes[0].equals("S")) {
                    // Criar nova seleção
                    String nome = partes[1];
                    String grupo = partes[2];
                    String tecnico = partes[3];
                    selecaoAtual = new Selecao(nome, grupo, tecnico);
                    selecoes.add(selecaoAtual);
                }
                else if (partes[0].equals("J") && selecaoAtual != null) {
                    try {
                        String nomeJ = partes[1];
                        int num = Integer.parseInt(partes[2]);
                        Posicao pos = Posicao.valueOf(partes[3].toUpperCase());
                        boolean titular = Boolean.parseBoolean(partes[4]);
                        StatusJogador status = StatusJogador.valueOf(partes[5].toUpperCase());
                        // O campo "selecao" será preenchido dentro de adicionarJogador()
                        Jogador j = new Jogador(nomeJ, num, pos, titular);
                        j.setStatus(status);
                        // tenta adicionar à seleção; se violar regra, ignora o jogador
                        selecaoAtual.adicionarJogador(j);
                    } catch (Exception e) {
                        // Exceções podem ser: IllegalArgumentException do construtor do Jogador,
                        // LimiteJogadoresException, JogadorDuplicadoException, etc.
                        System.err.println("Ignorando jogador inválido no arquivo: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
        }
        return selecoes;
    }
}
