package br.unb.cic.copa.testes.aluno3;

import br.unb.cic.copa.model.aluno2.Selecao;
import br.unb.cic.copa.model.aluno3.Arbitro;
import br.unb.cic.copa.model.aluno3.Estadio;
import br.unb.cic.copa.model.aluno3.Localizacao;
import br.unb.cic.copa.model.aluno3.PaisSede;
import br.unb.cic.copa.model.aluno3.exception.CapacidadeInvalidaException;
import br.unb.cic.copa.model.aluno3.exception.ExperienciaInvalidaException;
import br.unb.cic.copa.model.aluno4.Fase;
import br.unb.cic.copa.model.aluno4.Partida;
import br.unb.cic.copa.model.aluno3.repository.ArbitroRepository;
import br.unb.cic.copa.view.aluno3.ConsultarEscalaArbitroView;

import javax.swing.*;
import java.io.IOException;

public class TestarConsultarEscalaArbitroView {

    public static void main(String[] args) {
        popularDadosDeTeste();

        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Abrindo ConsultarEscalaArbitroView ===");
            System.out.println("Busque pelo ID 99 para ver as partidas designadas.");
            new ConsultarEscalaArbitroView();
        });
    }

    private static void popularDadosDeTeste() {
        try {
            // Cria árbitro de teste com ID 99
            Arbitro arbitro = new Arbitro(99, "Árbitro Teste", "teste@copa.com", "arbitro99", "Senha123", "999.999.999-99", "Itália", "Italiana", 15);

            Partida p1 = criarPartida("Brasil", "Argentina", "2026-06-15", "Maracanã");
            Partida p2 = criarPartida("França", "Espanha",   "2026-06-20", "Allianz Arena");

            arbitro.designarParaPartida(p1);
            arbitro.designarParaPartida(p2);

            ArbitroRepository repo = new ArbitroRepository("src/dados/arbitros.json");
            repo.salvar(arbitro);

            System.out.println("[OK] Árbitro ID 99 salvo com " + arbitro.consultarEscala().size() + " partidas.");

        } catch (ExperienciaInvalidaException | CapacidadeInvalidaException | IOException e) {
            System.out.println("[ERRO] Falha ao popular dados de teste: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    private static Partida criarPartida(String sel1, String sel2, String data, String nomeEstadio)
            throws CapacidadeInvalidaException {
        Selecao s1 = new Selecao(sel1, "A", "Técnico1");
        Selecao s2 = new Selecao(sel2, "B", "Técnico2");
        Localizacao loc = new Localizacao("Cidade", "Estado", PaisSede.MEXICO, "Endereço 100");
        Estadio estadio = new Estadio(1, nomeEstadio, loc, 60000);
        return new Partida(s1, s2, data, estadio, Fase.GRUPOS);
    }
}