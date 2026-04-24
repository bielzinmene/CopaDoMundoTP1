package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.GerenciarJogadorView;

import javax.swing.*;

public class TestarInterfaceJogador {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarJogadorView tela = new GerenciarJogadorView();
            tela.setVisible(true);
        });
    }
}
