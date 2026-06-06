package br.unb.cic.copa.model.aluno1.exception;

import br.unb.cic.copa.model.aluno2.exception.CopaException;

// Lançada quando um usuário tenta fazer algo que não tem permissão
// Ex: Organizador tentando cadastrar outro usuário
// Herda de CopaException que é a exceção base do projeto (criada pelo Aluno 2)
public class AcessoNegadoException extends CopaException {
    public AcessoNegadoException() {
        super("Acesso negado! Você não tem permissão para realizar esta operação.");
    }
}