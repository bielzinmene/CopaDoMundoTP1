package br.unb.cic.copa.model.aluno3.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

//Classe mãe CopaException
public class ArbitroNacionalidadeException extends CopaException {

    public ArbitroNacionalidadeException(String nomeArbitro, String nacionalidade) {
        super("Árbitro '" + nomeArbitro + "' não pode apitar: nacionalidade '"
                + nacionalidade + "' coincide com uma das seleções da partida.");
    }
}