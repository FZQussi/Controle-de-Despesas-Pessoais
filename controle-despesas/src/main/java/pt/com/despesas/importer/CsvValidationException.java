package pt.com.despesas.importer;

public class CsvValidationException extends RuntimeException {

    public CsvValidationException(String message) {
        super(message);
    }
}
