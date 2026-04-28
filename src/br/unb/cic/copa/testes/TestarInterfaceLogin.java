package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.LoginView;
import javax.swing.*;

public class TestarInterfaceLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView tela = new LoginView();
            tela.setVisible(true);
        });
    }
}