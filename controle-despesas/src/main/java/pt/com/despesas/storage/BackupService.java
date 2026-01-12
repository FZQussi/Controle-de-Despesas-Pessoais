package pt.com.despesas.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BackupService {

    private static final Logger log = LoggerFactory.getLogger(BackupService.class);

    private final Path diretorioBackup;

    public BackupService(Path diretorioBackup) {
        this.diretorioBackup = diretorioBackup;
        log.info("BackupService inicializado no diretório: {}", diretorioBackup.toAbsolutePath());
    }

    // -------------------------
    // Criar backup
    // -------------------------
    public void criarBackup(Path ficheiroOriginal) throws IOException {
        if (!Files.exists(ficheiroOriginal)) {
            log.warn("Arquivo original para backup não existe: {}", ficheiroOriginal.toAbsolutePath());
            return;
        }

        Files.createDirectories(diretorioBackup);

        String nomeBackup = ficheiroOriginal.getFileName().toString()
                .replace(".json", "")
                + "_" + java.time.LocalDateTime.now()
                .toString()
                .replace(":", "-")
                + ".json";

        Path destino = diretorioBackup.resolve(nomeBackup);
        Files.copy(ficheiroOriginal, destino);

        log.info("Backup criado com sucesso: {}", destino.toAbsolutePath());
    }

    // -------------------------
    // Listar backups
    // -------------------------
    public List<Path> listarBackups() throws IOException {
        if (!Files.exists(diretorioBackup)) {
            log.info("Diretório de backup não existe: {}", diretorioBackup.toAbsolutePath());
            return List.of();
        }

        List<Path> backups = Files.list(diretorioBackup)
                .filter(p -> p.toString().endsWith(".json"))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        log.info("Backups encontrados: {}", backups.size());
        return backups;
    }

    // -------------------------
    // Restaurar backup
    // -------------------------
    public void restaurarBackup(Path backup, Path destino) throws IOException {
        if (!Files.exists(backup)) {
            log.error("Backup não existe: {}", backup.toAbsolutePath());
            throw new IllegalArgumentException("Backup não existe");
        }

        Files.copy(backup, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        log.info("Backup restaurado com sucesso de {} para {}", backup.toAbsolutePath(), destino.toAbsolutePath());
    }
}
