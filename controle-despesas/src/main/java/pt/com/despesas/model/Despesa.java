package pt.com.despesas.model;

import java.time.LocalDate;

public class Despesa {

    private String descricao;
    private String categoria;
    private double valor;
    private LocalDate data;

    public Despesa(String descricao, String categoria, double valor, LocalDate data) {
        this.descricao = descricao;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return descricao + " | " + categoria + " | " + valor + " | " + data;
    }
}
