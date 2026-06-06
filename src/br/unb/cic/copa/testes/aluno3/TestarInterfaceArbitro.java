package br.unb.cic.copa.testes.aluno3;

import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno3.exception.ArbitroNacionalidadeException;
import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno4.StatusPartida;

public class TestarInterfaceArbitro {

    public static void main(String[] args) {
        testarCriacaoValida();
        testarExperienciaInvalida();
        testarDesignacaoValida();
        testarDesignacaoNacionalidadeInvalida();
        testarConsultarEscala();
    }

    // Deve criar árbitro sem erros
    private static void testarCriacaoValida() {
        try {
            Arbitro a = new Arbitro(1, "Carlos Silva", "carlos@copa.com", "carlos", "Senha123", "111.111.111-11", "Brasil", "Brasileira", 10);
            System.out.println("[OK] Árbitro criado: " + a);
        } catch (ExperienciaInvalidaException e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve lançar ExperienciaInvalidaException
    private static void testarExperienciaInvalida() {
        try {
            new Arbitro(2, "João Lima", "joao@copa.com", "joao", "Senha123", "222.222.222-22", "Argentina", "Argentina", -5);
            System.out.println("[FALHA] Deveria ter lançado ExperienciaInvalidaException.");
        } catch (ExperienciaInvalidaException e) {
            System.out.println("[OK] ExperienciaInvalidaException lançada: " + e.getMessage());
        }
    }

    // Árbitro com nacionalidade diferente das seleções deve ser designado com sucesso
    private static void testarDesignacaoValida() {
        try {
            Arbitro arbitro = new Arbitro(3, "Pierre Dupont", "pierre@copa.com", "pierre", "Senha123", "333.333.333-33", "França", "Francesa", 8);
            Partida partida = criarPartida("Brasil", "Argentina");
            arbitro.designarParaPartida(partida);
            System.out.println("[OK] Árbitro designado com sucesso para a partida.");
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve lançar ArbitroNacionalidadeException pois árbitro é brasileiro
    private static void testarDesignacaoNacionalidadeInvalida() {
        try {
            Arbitro arbitro = new Arbitro(4, "Marcos Souza", "marcos@copa.com", "marcos", "Senha123", "444.444.444-44", "Brasil", "Brasil", 5);
            Partida partida = criarPartida("Brasil", "Argentina");
            arbitro.designarParaPartida(partida);
            System.out.println("[FALHA] Deveria ter lançado ArbitroNacionalidadeException.");
        } catch (ArbitroNacionalidadeException e) {
            System.out.println("[OK] ArbitroNacionalidadeException lançada: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Deve listar as partidas designadas ao árbitro
    private static void testarConsultarEscala() {
        try {
            Arbitro arbitro = new Arbitro(5, "Hans Müller", "hans@copa.com", "hans", "Senha123", "555.555.555-55", "Alemanha", "Alemanha", 12);
            arbitro.designarParaPartida(criarPartida("Brasil", "Argentina"));
            arbitro.designarParaPartida(criarPartida("França", "Espanha"));
            System.out.println("[OK] Escala do árbitro (" + arbitro.consultarEscala().size() + " partidas):");
            for (Partida p : arbitro.consultarEscala()) {
                System.out.println("     " + p.getSelecao1().getNome() + " x " + p.getSelecao2().getNome());
            }
        } catch (Exception e) {
            System.out.println("[FALHA] " + e.getMessage());
        }
    }

    // Método auxiliar para criar uma partida de teste
    private static Partida criarPartida(String nomeSelecao1, String nomeSelecao2) throws CapacidadeInvalidaException {
        Selecao s1 = new Selecao(nomeSelecao1, "A", "Técnico1");
        Selecao s2 = new Selecao(nomeSelecao2, "A", "Técnico2");
        Localizacao loc = new Localizacao("Rio de Janeiro", "RJ", PaisSede.MEXICO, "Av. Atlântica, 1000");
        Estadio estadio = new Estadio(1, "Maracanã", loc, 78000);
        return new Partida(s1, s2, "2026-06-15", estadio);
    }
}