package br.edu.revenda;

/**
 * Passo 1 (CA5): Implementação mínima do serviço para atender ao caminho feliz (persistência com ID).
 */
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo cadastrar(Veiculo veiculo) {
        return repository.salvar(veiculo);
    }
}
