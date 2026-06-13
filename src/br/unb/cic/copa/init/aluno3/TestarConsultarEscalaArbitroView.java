package br.unb.cic.copa.init.aluno3;

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
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Abrindo ConsultarEscalaArbitroView ===");
            System.out.println("Busque pelo ID 99 para ver as partidas designadas.");
            new ConsultarEscalaArbitroView();
        });
    }
}
