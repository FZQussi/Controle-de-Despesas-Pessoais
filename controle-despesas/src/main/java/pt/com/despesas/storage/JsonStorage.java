package pt.com.despesas.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import pt.com.despesas.model.Despesa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonStorage {

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
    }

    public void guardar(List<Despesa> despesas) throws IOException {

        if (ficheiro.getParent() != null) {
            Files.createDirectories(ficheiro.getParent());
        }

        // 🔐 cria backup antes de sobrescrever
        backupService.criarBackup(ficheiro);

        mapper.writeValue(ficheiro.toFile(), despesas);
    }

    public List<Despesa> carregar() throws IOException {

        if (!Files.exists(ficheiro)) {
            return new ArrayList<>();
        }

        return mapper.readValue(
                ficheiro.toFile(),
                new TypeReference<List<Despesa>>() {}
        );
    }
     public void restaurar(Path backup) throws IOException {
        backupService.restaurarBackup(backup, ficheiro);
    }

    public List<Path> listarBackups() throws IOException {
        return backupService.listarBackups();
    }
}
