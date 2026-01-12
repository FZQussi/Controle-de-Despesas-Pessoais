package pt.com.despesas.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BackupService {

    private final Path diretorioBackup;

    public BackupService(Path diretorioBackup) {
        this.diretorioBackup = diretorioBackup;
    }

    public void criarBackup(Path ficheiroOriginal) throws IOException {

        if (!Files.exists(ficheiroOriginal)) {
            return;
        }

        Files.createDirectories(diretorioBackup);

        String nomeBackup =
                ficheiroOriginal.getFileName().toString()
                        .replace(".json", "")
                        + "_" + java.time.LocalDateTime.now()
                        .toString()
                        .replace(":", "-")
                        + ".json";

        Files.copy(ficheiroOriginal,
                diretorioBackup.resolve(nomeBackup));
    }

    // 🔹 listar backups
    public List<Path> listarBackups() throws IOException {

        if (!Files.exists(diretorioBackup)) {
            return List.of();
        }

        return Files.list(diretorioBackup)
                .filter(p -> p.toString().endsWith(".json"))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // 🔹 restaurar backup
    public void restaurarBackup(Path backup, Path destino)
            throws IOException {

        if (!Files.exists(backup)) {
            throw new IllegalArgumentException("Backup não existe");
        }

        Files.copy(backup, destino,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
}
