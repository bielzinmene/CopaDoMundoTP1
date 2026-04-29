package br.unb.cic.copa.testes.aluno1;

import br.unb.cic.copa.view.aluno1.LoginView;
import javax.swing.*;

public class TestarInterfaceLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView tela = new LoginView();
            tela.setVisible(true);
        });
    }
}