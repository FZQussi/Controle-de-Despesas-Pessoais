package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;

import java.util.List;

public interface DespesaServiceInterface {
    void adicionarDespesa(Despesa despesa);
    List<Despesa> listarDespesas();
}
