package pt.com.despesas.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

    private static final Logger log = LoggerFactory.getLogger(JsonStorage.class);

    private final Path ficheiro;
    private final ObjectMapper mapper;
    private final BackupService backupService;

    public JsonStorage(Path ficheiro) {
        this.ficheiro = ficheiro;
        this.mapper = new ObjectMapper()
                .findAndRegisterModules()
                .enable(SerializationFeature.INDENT_OUTPUT);

        Path backupDir = ficheiro.getParent().resolve("backups");
        this.backupService = new BackupService(backupDir);

        log.info("JsonStorage inicializado em: {}", ficheiro.toAbsolutePath());
    }

    // -------------------------
    // Guardar despesas em JSON
    // -------------------------
    public void guardar(List<Despesa> despesas) throws IOException {
        if (ficheiro.getParent() != null) {
            Files.createDirectories(ficheiro.getParent());
        }

        // 🔐 cria backup antes de sobrescrever
        log.info("Criando backup antes de salvar JSON...");
        backupService.criarBackup(ficheiro);

        mapper.writeValue(ficheiro.toFile(), despesas);
        log.info("Despesas salvas com sucesso em JSON: {}", ficheiro.toAbsolutePath());
    }

    // -------------------------
    // Carregar despesas do JSON
    // -------------------------
    public List<Despesa> carregar() throws IOException {
        if (!Files.exists(ficheiro)) {
            log.info("Arquivo JSON não existe, retornando lista vazia: {}", ficheiro.toAbsolutePath());
            return new ArrayList<>();
        }

        List<Despesa> despesas = mapper.readValue(
                ficheiro.toFile(),
                new TypeReference<List<Despesa>>() {}
        );
        log.info("Carregadas {} despesas do JSON", despesas.size());
        return despesas;
    }

    // -------------------------
    // Restaurar backup
    // -------------------------
    public void restaurar(Path backup) throws IOException {
        log.info("Restaurando backup: {}", backup.toAbsolutePath());
        backupService.restaurarBackup(backup, ficheiro);
        log.info("Backup restaurado com sucesso em: {}", ficheiro.toAbsolutePath());
    }

    // -------------------------
    // Listar backups
    // -------------------------
    public List<Path> listarBackups() throws IOException {
        List<Path> backups = backupService.listarBackups();
        log.info("Listados {} backups disponíveis", backups.size());
        return backups;
    }
}
