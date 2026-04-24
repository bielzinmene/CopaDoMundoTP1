package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.GerenciarSelecaoView;

import javax.swing.SwingUtilities;

public class TestarInterfaceSelecao {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarSelecaoView tela = new GerenciarSelecaoView();
            tela.setVisible(true);
        });
    }
}