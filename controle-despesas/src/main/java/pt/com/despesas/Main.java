package pt.com.despesas;

import pt.com.despesas.config.Config;
import pt.com.despesas.repository.DespesaRepositoryInterface;
import pt.com.despesas.repository.DespesaRepositoryJson;
import pt.com.despesas.service.DespesaService;
import pt.com.despesas.storage.JsonStorage;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

        // Diretório padrão de dados (configurável)
        String diretorioDados = Config.get("dados.diretorio", "dados");

        // Ficheiro JSON de persistência
        Path ficheiroJson = Path.of(diretorioDados, "despesas.json");

        // Storage JSON
        JsonStorage storage = new JsonStorage(ficheiroJson);

        // Repository com persistência
        DespesaRepositoryInterface repository =
                new DespesaRepositoryJson(storage);

        // Service
        DespesaService service = new DespesaService(repository);

        // Menu
        Menu menu = new Menu(service);
        menu.iniciar();
    }
}
