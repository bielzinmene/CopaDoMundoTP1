package br.unb.cic.copa.model.aluno3.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

//Classe mãe CopaException
public class CapacidadeInvalidaException extends CopaException {

    public CapacidadeInvalidaException(int capacidade) {
        super("Capacidade inválida: " + capacidade + ". Deve ser maior que zero.");
    }
}