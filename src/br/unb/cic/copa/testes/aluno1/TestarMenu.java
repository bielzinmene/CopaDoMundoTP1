package br.unb.cic.copa.testes.aluno1;

import br.unb.cic.copa.view.aluno1.MenuPrincipalView;
import javax.swing.*;

public class TestarMenu {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView();
            menu.setVisible(true);
        });
    }
}