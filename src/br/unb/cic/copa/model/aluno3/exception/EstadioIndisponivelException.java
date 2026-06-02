package br.unb.cic.copa.model.aluno3.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

public class EstadioIndisponivelException extends CopaException {

    public EstadioIndisponivelException(String nomeEstadio, String data) {
        super("Estádio '" + nomeEstadio + "' já possui partida agendada em " + data + ".");
    }
}