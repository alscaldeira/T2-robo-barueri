package robo.barueri.exception;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorLogger {

    private static final String LOG_FILE = "erro.log";

    public static void logError(Throwable throwable) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);

            pw.println("=== ERRO EM " + timestamp + " ===");
            pw.println("Mensagem: " + throwable.getMessage());
            pw.println("Classe: " + throwable.getClass().getName());
            pw.println("Stack Trace:");
            throwable.printStackTrace(pw);
            pw.println("=====================================\n");

        } catch (IOException e) {
            // Se falhar ao escrever o log, imprime no console como fallback
            System.err.println("Falha ao escrever no arquivo de log:");
            e.printStackTrace();
        }
    }
}
