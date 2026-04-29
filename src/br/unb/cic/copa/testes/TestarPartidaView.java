package CopaDoMundoTP1.src.br.unb.cic.copa.testes;

import CopaDoMundoTP1.src.br.unb.cic.copa.view.PartidaView;

import javax.swing.SwingUtilities;

public class TestarPartidaView {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PartidaView().setVisible(true);
        });
    }
}