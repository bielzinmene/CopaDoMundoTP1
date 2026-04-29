package br.unb.cic.copa.testes.aluno3;
import br.unb.cic.copa.view.aluno3.GerenciarArbitroView;
import javax.swing.*;
public class TestarInterfaceArbitro {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GerenciarArbitroView tela = new GerenciarArbitroView();
            tela.setVisible(true);
        });
    }
}
