package br.unb.cic.copa.init.aluno4;

import br.unb.cic.copa.view.aluno4.RegistrarResultadoView;

import javax.swing.SwingUtilities;

public class TestarRegistrarResultadoView {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RegistrarResultadoView().setVisible(true);
        });
    }
}