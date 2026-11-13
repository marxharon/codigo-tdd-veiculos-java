package br.edu.revenda;

/**
 * Passo 2 (CA1): Exceção de domínio introduzida para sinalizar violações de regras de negócio.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
