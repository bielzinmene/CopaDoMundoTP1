package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.VendaIngressoView;

import javax.swing.*;

public class TestarInterfaceIngresso {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VendaIngressoView tela = new VendaIngressoView();
            tela.setVisible(true);
        });
    }
}