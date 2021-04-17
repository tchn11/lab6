package server;

import messages.AnswerMsg;
import messages.CommandMsg;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import messages.Status;
import server.commands.CommandManager;

public class Server {
    private int port;
    private int timeout;
    private ServerSocketChannel socketChannel;
    private SocketChannel clientChanel;
    private CommandManager commandManager;

    public Server(int in_port, int in_timeout, CommandManager com){
        port = in_port;
        timeout = in_timeout;
        commandManager = com;
    }

    private boolean openSocket(){
        try {
            Main.logger.info("Начинаю запуск сервера");
            socketChannel = ServerSocketChannel.open();
            socketChannel.socket().bind(new InetSocketAddress(port));
            socketChannel.socket().setSoTimeout(timeout);
            Main.logger.info("Сервер успешно запущен");
            return true;
        } catch (IllegalArgumentException exception) {
            Main.printError("Порт '" + port + "' находится за пределами возможных значений");
            Main.logger.fatal("Порт '" + port + "' находится за пределами возможных значений");
            return false;
        }catch (IOException exception) {
            Main.printError("Произошла ошибка при попытке использовать порт");
            Main.logger.fatal("Произошла ошибка при попытке использовать порт");
            return false;
        }
    }

    private  void closeSocket(){
        try{
            Main.logger.info("Пытаюсь закрыть сокет");
            socketChannel.close();
            Main.logger.info("Сокет успешно закрыт");
        } catch (IOException exception) {
            Main.logger.error("Ошибка при закрытии сокета");
        }
    }

    private boolean startTransmission(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            Main.logger.info("Вхожу в ожидание соединения");
            clientChanel = socketChannel.accept();
            Main.logger.info("Уcтановлено соединение с клиентом");
            return true;
        } catch (IOException exception) {
            Main.printError("Ошибка подключения к клиенту");
            Main.logger.error("Ошибка подключения к клиенту");
            return false;
        }
    }

    private Object readObj(){
        try{
            Main.logger.info("Начинаю чтение объекта");
            ObjectInputStream ois = new ObjectInputStream(clientChanel.socket().getInputStream());
            Main.logger.info("Начинаю получать объект");
            Object obj = ois.readObject();
            Main.logger.info("Объект получен");
            ois.close();
            return obj;
        } catch (IOException exception) {
            Main.logger.error("Ошибка создния объекта ObjectInputStream");
        } catch (ClassNotFoundException exception) {
            Main.logger.error("Ошибка получения объекта");
        }
        return null;
    }

    private void sendAnswer(AnswerMsg answerMsg){
        try{
            Main.logger.info("Отправляю ответ");
            ObjectOutputStream ous = new ObjectOutputStream(clientChanel.socket().getOutputStream());
            ous.writeObject(answerMsg);
            ous.flush();
            Main.logger.info("Ответ отправлен");
            ous.close();
            Main.logger.info("Закрыт ObjectOutputStream");
        } catch (IOException exception) {
            Main.logger.error("Ошибка отправки ответа");
        }
    }

    private boolean endTransmission(){
        try {
            Main.logger.info("Закрываю соединение");
            clientChanel.close();
            Main.logger.info("Соединение успешно закрыто");
            return true;
        } catch (IOException exception) {
            Main.printError("Ошибка чтения данных");
            Main.logger.error("Ошибка закрытия соеденения");
            return false;
        }
    }

    public void run() {
        if (!openSocket())
            return;
        boolean wokrking = true;
        while (wokrking) {
            startTransmission();
            CommandMsg commandMsg = (CommandMsg) readObj();
            AnswerMsg answerMsg = new AnswerMsg();
            commandManager.executeCommand(commandMsg, answerMsg);
            sendAnswer(answerMsg);
            endTransmission();
            if (answerMsg.getStatus() == Status.EXIT)
                wokrking = false;
        }
        Main.logger.info("Конец завершение работы");
        closeSocket();
    }
}
