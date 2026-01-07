package pt.com.despesas.service;

import pt.com.despesas.model.Despesa;

import java.util.List;
import java.util.Map;
import java.time.YearMonth;
import java.util.List;

public interface DespesaServiceInterface {
    double totalDoMes(YearMonth mes);
    Map<String, Double> totalPorCategoria();
    void adicionarDespesa(Despesa despesa);
    List<Despesa> listarDespesas();
    List<Despesa> listarPorMes(YearMonth mes);
}
