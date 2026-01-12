package pt.com.despesas.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;
import pt.com.despesas.storage.JsonStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DespesaRepositoryJson implements DespesaRepositoryInterface {

    private static final Logger log =
            LoggerFactory.getLogger(DespesaRepositoryJson.class);

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

        log.info("A carregar despesas do armazenamento JSON");

        try {
            List<Despesa> carregadas = storage.carregar();
            log.info("Carga concluída: {} despesas carregadas", carregadas.size());
            return new ArrayList<>(carregadas);

        } catch (IOException e) {
            log.warn("Falha ao carregar despesas do JSON. Iniciando com lista vazia.", e);
            return new ArrayList<>();
        }
    }

    // -------------------------
    // Salvar despesa
    // -------------------------
    @Override
    public void salvar(Despesa despesa) {

        log.debug("A adicionar despesa: {} | {} | {}",
                despesa.getDescricao(),
                despesa.getCategoria(),
                despesa.getValor()
        );

        despesas.add(despesa);
        persistir();
    }

    // -------------------------
    // Listar despesas
    // -------------------------
    @Override
    public List<Despesa> listar() {

        log.debug("A listar despesas (total: {})", despesas.size());
        return new ArrayList<>(despesas);
    }

    // -------------------------
    // Persistência automática
    // -------------------------
    private void persistir() {

        log.debug("A persistir {} despesas em JSON", despesas.size());

        try {
            storage.guardar(despesas);
            log.info("Despesas guardadas com sucesso em JSON");

        } catch (IOException e) {
            log.error("Erro ao salvar despesas em JSON", e);
            throw new RuntimeException("Erro ao salvar despesas em JSON", e);
        }
    }
}
