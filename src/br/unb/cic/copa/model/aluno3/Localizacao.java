package br.unb.cic.copa.model.aluno3;

public class Localizacao {
    private String cidade;
    private String estado;
    private PaisSede pais;
    private String endereco;

    public Localizacao(String cidade, String estado, PaisSede pais, String endereco) {
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getPais() {
        return estado;
    }

    public String getEndereco() {
        return endereco;
    }
}