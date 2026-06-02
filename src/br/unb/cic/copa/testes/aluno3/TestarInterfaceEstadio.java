package br.unb.cic.copa.testes.aluno3;

import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;
import br.unb.cic.copa.model.aluno3.exception.EstadioIndisponivelException;
import br.unb.cic.copa.model.aluno4.Partida;

public class TestarInterfaceEstadio {

    public static void main(String[] args) {
        testarCriacaoValida();
        testarCapacidadeInvalida();
        testarAdicionarPartidaValida();
        testarAdicionarPartidaDataRepetida();
        testarVerificarDisponibilidade();
    }

    // Deve criar estádio sem erros
    private static void testarCriacaoValida() {
        try {
            Localizacao loc = new Localizacao("São Paulo", "SP", PaisSede.ESTADOS_UNIDOS, "Praça Charles Miller, s/n");
            Estadio e = new Estadio(1, "Pacaembu", loc, 40199);
            System.out.println("[OK] Estádio criado: " + e);
        } catch (CapacidadeInvalidaException e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve lançar CapacidadeInvalidaException
    private static void testarCapacidadeInvalida() {
        try {
            Localizacao loc = new Localizacao("Brasília", "DF", PaisSede.ESTADOS_UNIDOS, "Eixo Monumental");
            new Estadio(2, "Mané Garrincha", loc, -1);
            System.out.println("[FALHA] Deveria ter lançado CapacidadeInvalidaException.");
        } catch (CapacidadeInvalidaException e) {
            System.out.println("[OK] CapacidadeInvalidaException lançada: " + e.getMessage());
        }
    }

    // Deve adicionar partida com sucesso
    private static void testarAdicionarPartidaValida() {
        try {
            Estadio estadio = criarEstadio();
            Partida partida = criarPartida(estadio, "Brasil", "Argentina", "2026-06-15");
            estadio.adicionarPartida(partida);
            System.out.println("[OK] Partida adicionada ao estádio com sucesso.");
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve lançar EstadioIndisponivelException ao tentar adicionar segunda partida na mesma data
    private static void testarAdicionarPartidaDataRepetida() {
        try {
            Estadio estadio = criarEstadio();
            estadio.adicionarPartida(criarPartida(estadio, "Brasil", "Argentina", "2026-06-20"));
            estadio.adicionarPartida(criarPartida(estadio, "França", "Espanha", "2026-06-20"));
            System.out.println("[FALHA] Deveria ter lançado EstadioIndisponivelException.");
        } catch (EstadioIndisponivelException e) {
            System.out.println("[OK] EstadioIndisponivelException lançada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve retornar true para data livre e false para data ocupada
    private static void testarVerificarDisponibilidade() {
        try {
            Estadio estadio = criarEstadio();
            estadio.adicionarPartida(criarPartida(estadio, "Brasil", "Argentina", "2026-07-01"));

            boolean livre = estadio.verificarDisponibilidade("2026-07-02");
            boolean ocupado = estadio.verificarDisponibilidade("2026-07-01");

            System.out.println("[OK] Disponibilidade em data livre: " + livre);    // true
            System.out.println("[OK] Disponibilidade em data ocupada: " + ocupado); // false
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Métodos auxiliares para criação de objetos de teste
    private static Estadio criarEstadio() throws CapacidadeInvalidaException {
        Localizacao loc = new Localizacao("Miami", "Florida", PaisSede.ESTADOS_UNIDOS, "Av. Atlântica, 1000");
        return new Estadio(1, "Nu Stadium", loc, 78000);
    }

    private static Partida criarPartida(Estadio estadio, String nomeS1, String nomeS2, String data) {
        return new Partida(new Selecao(nomeS1, "A", "Técnico1"), new Selecao(nomeS2, "A", "Técnico2"), data, estadio);
    }
}