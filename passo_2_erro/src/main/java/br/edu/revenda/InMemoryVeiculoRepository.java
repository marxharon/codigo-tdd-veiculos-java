package br.edu.revenda;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Passo 2 ERRO (Fase RED): Mesma implementação em memória do Passo 1.
 */
public class InMemoryVeiculoRepository implements VeiculoRepository {

    private final Map<Long, Veiculo> bancoEmMemoria = new LinkedHashMap<>();
    private long sequenciaId = 1L;

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        veiculo.setId(sequenciaId++);
        bancoEmMemoria.put(veiculo.getId(), veiculo);
        return veiculo;
    }

    @Override
    public Optional<Veiculo> buscarPorId(Long id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }
}
