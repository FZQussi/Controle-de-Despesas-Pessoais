package pt.com.despesas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.config.Config;
import pt.com.despesas.repository.DespesaRepositoryInterface;
import pt.com.despesas.repository.DespesaRepositoryJson;
import pt.com.despesas.service.DespesaService;
import pt.com.despesas.storage.JsonStorage;

import java.nio.file.Path;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Iniciando aplicação de Controle de Despesas...");

        // Diretório padrão de dados (configurável)
        String diretorioDados = Config.get("dados.diretorio", "dados");
        log.info("Diretório de dados definido: {}", diretorioDados);

        // Ficheiro JSON de persistência
        Path ficheiroJson = Path.of(diretorioDados, "despesas.json");
        log.info("Arquivo JSON de persistência: {}", ficheiroJson.toAbsolutePath());

        // Storage JSON
        JsonStorage storage = new JsonStorage(ficheiroJson);
        log.info("JsonStorage inicializado");

        // Repository com persistência
        DespesaRepositoryInterface repository = new DespesaRepositoryJson(storage);
        log.info("Repository inicializado com persistência JSON");

        // Service
        DespesaService service = new DespesaService(repository);
        log.info("DespesaService inicializado");

        // Menu
        Menu menu = new Menu(service);
        log.info("Menu inicializado, aguardando interação do usuário...");
        menu.iniciar();

        log.info("Aplicação encerrada");
    }
}
