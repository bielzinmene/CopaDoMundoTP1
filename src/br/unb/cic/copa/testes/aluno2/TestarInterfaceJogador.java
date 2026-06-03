package br.unb.cic.copa.testes.aluno2;

import br.unb.cic.copa.view.aluno2.GerenciarJogadorView;
import javax.swing.SwingUtilities;

public class TestarInterfaceJogador {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Abrindo GerenciarJogadorView ===");
            new GerenciarJogadorView();
        });
    }
}