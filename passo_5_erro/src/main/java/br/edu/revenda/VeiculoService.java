package br.edu.revenda;

import java.math.BigDecimal;
import java.time.Year;
import java.util.regex.Pattern;

/**
 * Passo 5 ERRO (Fase RED): O código de produção ainda é o do Passo 4!
 * Valida placa (CA1), ano (CA2) e preço (CA3), mas ainda NÃO impede placas duplicadas (CA4).
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
        validarPreco(veiculo.getPreco());
        // TODO (Fase GREEN no passo_5): verificar no repositório se a placa já existe antes de salvar
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

    private void validarPreco(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Preço inválido. O preço deve ser maior que zero.");
        }
    }
}
