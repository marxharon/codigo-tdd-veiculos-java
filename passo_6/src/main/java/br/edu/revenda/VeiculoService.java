package br.edu.revenda;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Passo 6 (Versão Final Refatorada): Serviço de domínio responsável pelo cadastro de veículos no estoque.
 *
 * Critérios de Aceitação atendidos:
 * - CA1: Placa no padrão Mercosul simplificado AAA1A23
 * - CA2: Ano entre 1900 e anoAtual + 1
 * - CA3: Preço maior que zero
 * - CA4: Impedimento de duplicidade de placa
 * - CA5: Geração de identificador e persistência do veículo válido
 */
public class VeiculoService {

    private static final Pattern PADRAO_PLACA_MERCOSUL = Pattern.compile("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    private static final int ANO_MINIMO = 1900;

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = Objects.requireNonNull(repository, "O repositório de veículos é obrigatório.");
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        validarObjetoVeiculo(veiculo);
        validarPlacaMercosul(veiculo.getPlaca());
        validarIntervaloDeAno(veiculo.getAno());
        validarPrecoPositivo(veiculo.getPreco());
        validarUnicidadeDaPlaca(veiculo.getPlaca());

        return repository.salvar(veiculo);
    }

    private void validarObjetoVeiculo(Veiculo veiculo) {
        if (veiculo == null) {
            throw new RegraNegocioException("Os dados do veículo são obrigatórios.");
        }
    }

    private void validarPlacaMercosul(String placa) {
        if (placa == null || !PADRAO_PLACA_MERCOSUL.matcher(placa).matches()) {
            throw new RegraNegocioException("Placa inválida. Deve seguir o padrão Mercosul simplificado AAA1A23.");
        }
    }

    private void validarIntervaloDeAno(int ano) {
        int anoMaximoPermitido = Year.now().getValue() + 1;
        if (ano < ANO_MINIMO || ano > anoMaximoPermitido) {
            throw new RegraNegocioException(
                    "Ano inválido. O ano deve estar entre " + ANO_MINIMO + " e " + anoMaximoPermitido + "."
            );
        }
    }

    private void validarPrecoPositivo(BigDecimal preco) {
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("Preço inválido. O preço deve ser maior que zero.");
        }
    }

    private void validarUnicidadeDaPlaca(String placa) {
        if (repository.existePorPlaca(placa)) {
            throw new RegraNegocioException("Já existe um veículo cadastrado com a placa " + placa + ".");
        }
    }
}
