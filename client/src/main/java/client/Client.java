package client;
import client.console.ConsoleManager;
import general.data.RowStudyGroup;
import general.exeptions.ConnectionBrokenException;
import messages.AnswerMsg;
import messages.CommandMsg;
import messages.Status;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import static client.console.ConsoleManager.print;
import static client.console.ConsoleManager.printErr;

public class Client {
    private String serverHost;
    private int serverPort;
    private int connectionAttempts;
    private int connectionTimeout;

    public int attempts = 0;

    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;

    private Socket socket;

    private  ConsoleManager consoleManager;

    public Client(String host, int port, int attempts, int timeout, ConsoleManager cons){
        serverHost = host;
        serverPort = port;
        connectionAttempts = attempts;
        connectionTimeout = timeout;
        consoleManager = cons;
    }

    private boolean connectToServer(){
        try {
            if (attempts > 0)
                print("Попытка переподключиться");
            attempts++;
            socket = new Socket(serverHost, serverPort);
            serverWriter = new ObjectOutputStream(socket.getOutputStream());
            serverReader = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            printErr("Неизвестный хост: " + serverHost);
            return false;
        } catch (IOException exception) {
            printErr("Ошибка открытия порта" + serverPort);
            return false;
        }
        print("Порт успешно открыт.");
        return true;
    }

    private void writeMessage(CommandMsg msg) throws ConnectionBrokenException {
        try{
            serverWriter.writeObject(msg);
        } catch (IOException exception) {
            printErr("Разрыв соеденения");
            throw new ConnectionBrokenException();
        }
    }

    private AnswerMsg readMessage() throws ConnectionBrokenException{
        AnswerMsg retMsg = null;
        try {
            retMsg =  (AnswerMsg) serverReader.readObject();
        } catch (IOException exception) {
            printErr("Разрыв соеденения");
            throw new ConnectionBrokenException();
        } catch (ClassNotFoundException exception) {
            printErr("Пришедшие данные не класс");
        }
        return retMsg;
    }


    private void closeConnection(){
        try{
            socket.close();
            serverReader.close();
            serverWriter.close();
        } catch (IOException exception) {
            printErr("Ошибка закртия файлов");
        }
    }

    public void run(){
        boolean work = true;
        while (!connectToServer()) {
            if(attempts > connectionAttempts){
                printErr("Превышено количество попыток подключиться");
                return;
            }
            try {
                Thread.sleep(connectionTimeout);
            } catch (InterruptedException e) {
                printErr("Произошла ошибка при попытке ожидания подключения!");
                print("Повторное подключение будет произведено немедленно.");
            }

        }
        while (work){
            consoleManager.waitCommand();
            RowStudyGroup studyGroup = null;
            if (consoleManager.getCommand().equals("add")){
                studyGroup = consoleManager.askGroup();
            }
            CommandMsg send = new CommandMsg(consoleManager.getCommand(), consoleManager.getArg(), studyGroup);

            AnswerMsg answ = null;
            boolean wasSend = false;
            try{
                writeMessage(send);
                wasSend = true;
                answ = readMessage();
            } catch (ConnectionBrokenException e) {
                print("Попытка переподключиться");
                while (!connectToServer()){
                    if(attempts > connectionAttempts){
                        printErr("Превышено количество попыток подключиться");
                        return;
                    }
                    try {
                        Thread.sleep(connectionTimeout);
                    } catch (InterruptedException exception) {
                        printErr("Произошла ошибка при попытке ожидания подключения!");
                        print("Повторное подключение будет произведено немедленно.");
                    }
                }
                if (wasSend){
                    print("Сервер запомнил данные о команде");
                }else {
                    print("Сервер не запомнил данные о команде");
                }

            }
            if (answ.getStatus() == Status.ERROR){
                print("При выполнении приграммы произошла ошибка");
                print(answ.getMessage());
            } else {
                print(answ.getMessage());
            }
            if (answ.getStatus() == Status.EXIT){
                work = false;
            }
        }
    }

}
