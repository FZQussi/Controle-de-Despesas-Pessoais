package pt.com.despesas.repository;

import pt.com.despesas.model.Despesa;
import pt.com.despesas.storage.JsonStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DespesaRepositoryJson implements DespesaRepositoryInterface {

    private final JsonStorage storage;
    private final List<Despesa> despesas;

    public DespesaRepositoryJson(JsonStorage storage) {
        this.storage = storage;
        this.despesas = carregarInicial();
    }

    // -------------------------
    // Carregamento inicial
    // -------------------------
    private List<Despesa> carregarInicial() {
        try {
            return new ArrayList<>(storage.carregar());
        } catch (IOException e) {
            System.out.println("Aviso: falha ao carregar despesas do JSON");
            return new ArrayList<>();
        }
    }

    // -------------------------
    // Salvar despesa
    // -------------------------
    @Override
    public void salvar(Despesa despesa) {
        despesas.add(despesa);
        persistir();
    }

    // -------------------------
    // Listar despesas
    // -------------------------
    @Override
    public List<Despesa> listar() {
        return new ArrayList<>(despesas);
    }

    // -------------------------
    // Persistência automática
    // -------------------------
    private void persistir() {
        try {
            storage.guardar(despesas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar despesas em JSON", e);
        }
    }
}
