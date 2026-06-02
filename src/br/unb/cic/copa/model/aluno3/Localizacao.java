package br.unb.cic.copa.model.aluno3;

import java.io.Serializable;

// Representa a localização física de um estádio
public class Localizacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cidade;
    private String estado;
    private PaisSede pais; // país sede da Copa onde o estádio está localizado
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

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public PaisSede getPais() {
        return pais;
    }

    public void setPais(PaisSede pais) {
        this.pais = pais;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return endereco + ", " + cidade + " - " + estado + ", " + pais;
    }
}