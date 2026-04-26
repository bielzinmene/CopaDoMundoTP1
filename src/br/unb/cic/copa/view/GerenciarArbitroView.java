package br.unb.cic.copa.view;
import javax.swing.*;
import java.awt.*;

public class GerenciarArbitroView extends JFrame {
    private JTextField txtNome;
    private JTextField txtNacionalidade;
    private JTextField txtExperiencia;

    public GerenciarArbitroView(){
        setTitle("Gerenciar Árbitro - Copa do Mundo 2026");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("Nacionalidade:"));
        txtNacionalidade = new JTextField();
        add(txtNacionalidade);

        add(new JLabel("Experiência:"));
        txtExperiencia = new JTextField();
        add(txtExperiencia);

        JButton botaoSalvar = new JButton("Salvar");
        JButton botaoCancelar = new JButton("Cancelar");

        botaoCancelar.addActionListener(e -> dispose());

        add(botaoSalvar);
        add(botaoCancelar);
    }

}
