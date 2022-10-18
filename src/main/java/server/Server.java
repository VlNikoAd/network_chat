package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static final int PORT = 23420;
    private final ArrayList<ClientThread> clients = new ArrayList<>();
    Logger logger = Logger.getInstance();

    public Server() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            // создаём серверный сокет на определенном порту
            serverSocket = new ServerSocket(PORT);
            logger.log("Чат запущен!");
            // ждём подключений в бесконечном цикле
            while (!serverSocket.isClosed()) {
                clientSocket = serverSocket.accept();
                // создаём обьект потока клиента, который подключился к серверу
                // this - это наш сервер
                ClientThread client = new ClientThread(clientSocket, this);
                clients.add(client);
                // каждое подключение клиента обрабатываем в новом потоке
                new Thread(client).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert clientSocket != null;
                clientSocket.close();
                logger.log("Сервер остановлен");
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //метод отправки сообщений всем клиентам
    public void sendMsgToAll(String msg) {
        for (ClientThread cl : clients) {
            cl.sendMessage(msg);
        }
    }

    // удаляем человека из коллекции, если он покидает чат
    public void removeClient(ClientThread client) {
        clients.remove(client);
    }
}