package server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger logger = null;

    private Logger() {
    }

    public static Logger getInstance() {
        if (logger == null) logger = new Logger();
        return logger;
    }

    public void log(String msg) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm")) + "] " + msg);

        try (FileWriter writer = new FileWriter("file.log", true)) {

            writer.write("[" + LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern("dd.MM.yyyy HH:mm")) + "] " + msg + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
