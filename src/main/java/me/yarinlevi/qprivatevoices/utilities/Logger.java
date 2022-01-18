package me.yarinlevi.qprivatevoices.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author YarinQuapi
 **/
public class Logger {
    public static void info(String message) {
        System.out.println(ConsoleColors.BLUE + "[INFO: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) +"] " + ConsoleColors.RESET + message);
    }


    public static void warning(String message) {
        System.out.println(ConsoleColors.YELLOW + "[WARNING: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) +"] " + message + ConsoleColors.RESET);
    }


    public static void error(String message) {
        System.out.println(ConsoleColors.RED + "[ERROR: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) +"] " + message + ConsoleColors.RESET);
    }

    public static void tag(String tag, String message) {
        System.out.println(ConsoleColors.CYAN + "[" + tag.toUpperCase() + ": " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")) +"] " + message + ConsoleColors.RESET);
    }
}
