package br.unb.cic.copa.init.aluno3;

import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;
import javax.swing.SwingUtilities;

public class TestarInterfaceEstadio {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("=== Abrindo GerenciarEstadioView ===");
            new GerenciarEstadioView();
        });
    }
}