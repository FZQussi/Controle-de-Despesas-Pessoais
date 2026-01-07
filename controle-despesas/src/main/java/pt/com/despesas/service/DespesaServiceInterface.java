package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;

import java.util.List;
import java.util.Map;

public interface DespesaServiceInterface {
    Map<String, Double> totalPorCategoria();
    void adicionarDespesa(Despesa despesa);
    List<Despesa> listarDespesas();
}
