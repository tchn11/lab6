package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class Main {
    public static Logger logger = LogManager.getLogger("ServerLogger");
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 60000;

    public static void main(String[] args) {
        Server server = new Server(PORT, CONNECTION_TIMEOUT);

    }

    public static void printError(String msg){
        System.out.println("error: " + msg);
    }
    public static void print(String msg){
        System.out.println(msg);
    }
}