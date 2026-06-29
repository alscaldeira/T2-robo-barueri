package robo.barueri.exception;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;

public class ErrorHandler {

    public static void handleError(Throwable throwable, Component parentComponent) {
        // 1. Registra no arquivo de log
        ErrorLogger.logError(throwable);

        // 2. Exibe uma tela de erro
        String mensagem = "Ocorreu um erro inesperado:\n" + throwable.getMessage();

        // Opcional: mostrar stack trace em um campo de texto expansível
        JTextArea textArea = new JTextArea(getStackTraceAsString(throwable), 10, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        Object[] options = {"OK", "Detalhes"};
        int opcao = JOptionPane.showOptionDialog(
                parentComponent,
                new Object[]{mensagem, scrollPane},
                "Erro no Sistema",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        // Se clicar em "Detalhes", mostra a stack trace completa em uma nova janela
        if (opcao == 1) {
            JTextArea detalhes = new JTextArea(getStackTraceAsString(throwable), 20, 60);
            detalhes.setEditable(false);
            JOptionPane.showMessageDialog(
                    parentComponent,
                    new JScrollPane(detalhes),
                    "Detalhes do Erro",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private static String getStackTraceAsString(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}