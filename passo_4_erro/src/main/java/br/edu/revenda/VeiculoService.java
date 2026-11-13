package br.edu.revenda;

import java.time.Year;
import java.util.regex.Pattern;

/**
 * Passo 4 ERRO (Fase RED): O código de produção ainda é o do Passo 3!
 * Valida placa (CA1) e ano (CA2), mas ainda NÃO valida se o preço é maior que zero (CA3).
 */
public class VeiculoService {

    private static final Pattern PADRAO_PLACA_MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    private static final int ANO_MINIMO = 1900;

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        validarPlaca(veiculo.getPlaca());
        validarAno(veiculo.getAno());
        // TODO (Fase GREEN no passo_4): validar se o preço é maior que zero
        return repository.salvar(veiculo);
    }

    private void validarPlaca(String placa) {
        if (placa == null || !PADRAO_PLACA_MERCOSUL.matcher(placa).matches()) {
            throw new RegraNegocioException("Placa inválida. Deve seguir o padrão Mercosul simplificado AAA1A23.");
        }
    }

    private void validarAno(int ano) {
        int anoMaximo = Year.now().getValue() + 1;
        if (ano < ANO_MINIMO || ano > anoMaximo) {
            throw new RegraNegocioException(
                    "Ano inválido. O ano deve estar entre " + ANO_MINIMO + " e " + anoMaximo + "."
            );
        }
    }
}
