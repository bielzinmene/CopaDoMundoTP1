package br.unb.cic.copa.testes.aluno5;

import br.unb.cic.copa.view.aluno5.VendaIngressoView;

import javax.swing.*;

public class TestarInterfaceIngresso {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VendaIngressoView tela = new VendaIngressoView();
            tela.setVisible(true);
        });
    }
}