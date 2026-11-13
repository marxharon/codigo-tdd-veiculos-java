package br.edu.revenda;

import java.math.BigDecimal;

/**
 * Passo 2 ERRO (Fase RED): Mesma entidade do Passo 1.
 */
public class Veiculo {

    private Long id;
    private final String placa;
    private final String modelo;
    private final int ano;
    private final BigDecimal preco;

    public Veiculo(String placa, String modelo, int ano, BigDecimal preco) {
        this.placa = placa;
        this.modelo = modelo;
        this.ano = ano;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}
