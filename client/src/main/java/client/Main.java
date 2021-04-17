package client;

import client.console.ConsoleManager;

import java.util.Scanner;
/**
 * Main class for client
 * @author Konanykhina Antonina
 */
public class Main {
    public static void main(String[] args) {
        int recconectAtmpts = 20;
        int timeout = 100;
        String host = null;
        int port = 0;
        try {
            host = args[0].trim();
            port = Integer.parseInt(args[1].trim());
        }catch (NumberFormatException exception){
            ConsoleManager.printErr("Порт должен быть числом");
            return;
        }catch (ArrayIndexOutOfBoundsException exception){
            ConsoleManager.printErr("Не достаточно аргументов");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        ConsoleManager consoleManager = new ConsoleManager(scanner);
        Client client = new Client(host, port, recconectAtmpts, timeout, consoleManager);
        client.run();
    }
}
