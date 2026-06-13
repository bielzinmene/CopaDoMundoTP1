package br.unb.cic.copa.init.aluno1;

import br.unb.cic.copa.model.aluno1.Administrador;
import br.unb.cic.copa.view.aluno1.MenuPrincipalView;
import javax.swing.*;

public class TestarMenu {
    public static void main(String[] args) {

        // Cria um usuário Admin fictício apenas para testar a tela visualmente
        Administrador usuarioTeste = new Administrador(
                1, "Admin Teste", "admin@teste.com",
                "admin", "Admin123", "000.000.000-00", "Brasil"
        );

        SwingUtilities.invokeLater(() -> {
            MenuPrincipalView menu = new MenuPrincipalView(usuarioTeste);
            menu.setVisible(true);
        });
    }
}