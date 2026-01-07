package pt.com.despesas.repository;

import pt.com.despesas.model.Despesa;
import java.util.List;

public interface DespesaRepositoryInterface {
    void salvar(Despesa despesa);
    List<Despesa> listar();
}
