package br.edu.revenda;

/**
 * Passo 4 ERRO (Fase RED): Exceção de domínio para violações das regras de negócio.
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
