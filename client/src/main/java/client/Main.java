package client;

import client.console.ConsoleManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int recconectAtmpts = 20;
        int timeout = 20;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner scanner = new Scanner(System.in);
        ConsoleManager consoleManager = new ConsoleManager(scanner);
        Client client = new Client(host, port, recconectAtmpts, timeout, consoleManager);
        client.run();
    }
}
