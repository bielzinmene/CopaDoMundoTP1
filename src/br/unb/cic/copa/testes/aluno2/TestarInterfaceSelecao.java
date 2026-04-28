package br.unb.cic.copa.testes.aluno2;

import br.unb.cic.copa.view.aluno2.GerenciarSelecaoView;

import javax.swing.SwingUtilities;

public class TestarInterfaceSelecao {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarSelecaoView tela = new GerenciarSelecaoView();
            tela.setVisible(true);
        });
    }
}