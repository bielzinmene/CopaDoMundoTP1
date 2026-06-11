package br.unb.cic.copa.model.aluno3.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

//Classe mãe CopaException
public class ExperienciaInvalidaException extends CopaException {

    public ExperienciaInvalidaException(int experiencia) {
        super("Valor de experiência inválido: " + experiencia + ". Deve ser maior ou igual a zero.");
    }
}