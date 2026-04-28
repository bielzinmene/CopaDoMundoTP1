package br.unb.cic.copa.testes;

import br.unb.cic.copa.view.MenuPrincipalView;
import javax.swing.*;

public class TestarMenu {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView();
            menu.setVisible(true);
        });
    }
}