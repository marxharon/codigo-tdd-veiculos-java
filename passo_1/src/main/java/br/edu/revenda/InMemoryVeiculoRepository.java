package br.edu.revenda;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Passo 1 (CA5): Implementação em memória (Fake) que gera ID sequencial e armazena o veículo.
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
