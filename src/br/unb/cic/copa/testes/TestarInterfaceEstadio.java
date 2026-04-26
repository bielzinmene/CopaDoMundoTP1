package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.GerenciarArbitroView;
import br.unb.cic.copa.view.GerenciarEstadioView;

import javax.swing.*;

public class TestarInterfaceEstadio {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarEstadioView tela = new GerenciarEstadioView();
            tela.setVisible(true);
        });
    }
}
