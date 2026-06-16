package br.unb.cic.copa.model.aluno2;

import java.util.ArrayList;
import java.util.List;
import br.unb.cic.copa.model.aluno2.exception.*;
import java.io.*;

public class Selecao {
    private static final int MIN_JOGADORES = 18;
    private static final int MAX_JOGADORES = 26;

    private int id;
    private String nome;
    private String grupo;
    private String tecnico;
    private List<Jogador> jogadores;

    // Construtor para nova seleção
    public Selecao(String nome, String grupo, String tecnico) {
        this.nome = nome;
        this.grupo = grupo;
        this.tecnico = tecnico;
        this.jogadores = new ArrayList<>();
    }

    // Construtor vazio para uso do GSON
    public Selecao() {
        this.jogadores = new ArrayList<>();
    }

    // ----------métodos ---------
    public boolean numeroJaExiste(int numeracao) {
        for (Jogador j : jogadores) {
            if (j.getNumeracao() == numeracao) {
                return true;
            }
        }
        return false;
    }

    public void adicionarJogador(Jogador novoJogador) throws LimiteJogadoresException, JogadorDuplicadoException {
        if(jogadores.size() >= MAX_JOGADORES) {
            throw new LimiteJogadoresException("A seleção " + nome + " já atingiu o limite máximo de " + MAX_JOGADORES + " jogadores.");
        }

        if (numeroJaExiste(novoJogador.getNumeracao())) {
            throw new JogadorDuplicadoException("A numeração " + novoJogador.getNumeracao() + " já está sendo usada na seleção " + nome);
        }

        jogadores.add(novoJogador);
        novoJogador.setSelecao(this);
    }

    public void removerJogador(Jogador jogador) {
        jogadores.remove(jogador);
        if (jogador.getSelecao() == this) {
            jogador.setSelecao(null);
        }
    }

    public void removerJogadorPorNome(String nome) {
        Jogador remover = null;
        for (Jogador j : jogadores) {
            if (j.getNome().equalsIgnoreCase(nome)) {
                remover = j;
                break;
            }
        }
        if (remover != null) {
            removerJogador(remover);
        }
    }

    public boolean ehEscalacaoValida() throws StatusJogadorInvalidoException {
        int contTitular = 0;
        boolean temGoleiro = false;

        for(Jogador j : jogadores) {
            if(j.isTitular()){
                if(j.getStatus() != StatusJogador.ATIVO){
                    throw new StatusJogadorInvalidoException("Jogador " + j.getNome() + " está " + j.getStatus() + " e não pode ser titular.");
                }
                contTitular++;
            }
            if(j.getPosicao() == Posicao.GOLEIRO && j.isTitular()) {
                temGoleiro = true;
            }
        }
        if(contTitular != 11) {
            throw new StatusJogadorInvalidoException("Erro na escalação da " + nome + ": Apenas " + contTitular + " titulares (necessário 11).");
        }

        if (!temGoleiro) {
            throw new StatusJogadorInvalidoException("Erro na escalação da " + nome + ": Nenhum goleiro entre os titulares.");
        }

        return true;
    }

    public List<Jogador> buscarJogadoresPorNome(String parteNome) {
        List<Jogador> resultado = new ArrayList<>();
        String parteLower = parteNome.toLowerCase();
        for (Jogador j : jogadores) {
            if (j.getNome().toLowerCase().contains(parteLower)) {
                resultado.add(j);
            }
        }
        return resultado;
    }

    /*metodo usado para debug
    public void exibirIntegrantes() {
        System.out.println("=== Seleção: " + nome + " (Grupo " + grupo + ") ===");
        System.out.println("Técnico: " + tecnico);
        System.out.println("Total de jogadores: " + jogadores.size());
        System.out.println("--- Titulares ---");
        for (Jogador j : jogadores) {
            if (j.isTitular()) System.out.println(j);
        }
        System.out.println("--- Reservas ---");
        for (Jogador j : jogadores) {
            if (!j.isTitular()) System.out.println(j);
        }
    }
    */

    // ========== GETTERS E SETTERS ==========

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public void setJogadores(List<Jogador> jogadores) {
        this.jogadores = jogadores;
    }

    public String getNome() {
        return nome;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getTecnico() {
        return tecnico;
    }

    public List<Jogador> getJogadores() {
        return jogadores;
    }
}