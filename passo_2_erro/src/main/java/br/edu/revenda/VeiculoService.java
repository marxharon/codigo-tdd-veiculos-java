package br.edu.revenda;

/**
 * Passo 2 ERRO (Fase RED): O código de produção ainda é exatamente o do Passo 1!
 * Ainda NÃO valida a placa Mercosul (CA1), portanto os novos testes de placa inválida irão FALHAR.
 */
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        // TODO (Fase GREEN no passo_2): validar padrão Mercosul AAA1A23 antes de salvar
        return repository.salvar(veiculo);
    }
}
