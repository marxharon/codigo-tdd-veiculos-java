package br.edu.revenda;

/**
 * Passo 1 ERRO (Fase RED): Esqueleto do serviço criado apenas para permitir a compilação do primeiro teste.
 * O método cadastrar ainda não foi implementado (retorna o veículo sem ID e sem persistir).
 */
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        // TODO (Fase GREEN no passo_1): delegar para repository.salvar(veiculo) gerando ID
        return veiculo;
    }
}
