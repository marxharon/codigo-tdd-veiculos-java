package br.edu.revenda;

import java.util.regex.Pattern;

/**
 * Passo 3 ERRO (Fase RED): O código de produção ainda é o do Passo 2!
 * Valida apenas a placa (CA1), mas ainda NÃO valida o ano (CA2).
 */
public class VeiculoService {

    private static final Pattern PADRAO_PLACA_MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        validarPlaca(veiculo.getPlaca());
        // TODO (Fase GREEN no passo_3): validar se o ano está entre 1900 e anoAtual + 1
        return repository.salvar(veiculo);
    }

    private void validarPlaca(String placa) {
        if (placa == null || !PADRAO_PLACA_MERCOSUL.matcher(placa).matches()) {
            throw new RegraNegocioException("Placa inválida. Deve seguir o padrão Mercosul simplificado AAA1A23.");
        }
    }
}
