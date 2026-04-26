package br.unb.cic.copa.testes;
import br.unb.cic.copa.view.GerenciarArbitroView;
import javax.swing.*;
public class TestarInterfaceArbitro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarArbitroView tela = new GerenciarArbitroView();
            tela.setVisible(true);
        });
    }
}
