package br.unb.cic.copa.testes.aluno4;

import br.unb.cic.copa.view.aluno4.PartidaView;

import javax.swing.SwingUtilities;

public class TestarPartidaView {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PartidaView().setVisible(true);
        });
    }
}