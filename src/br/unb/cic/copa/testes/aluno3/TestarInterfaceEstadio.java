package br.unb.cic.copa.testes.aluno3;

import br.unb.cic.copa.view.aluno3.GerenciarEstadioView;

import javax.swing.*;

public class TestarInterfaceEstadio {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarEstadioView tela = new GerenciarEstadioView();
            tela.setVisible(true);
        });
    }
}
