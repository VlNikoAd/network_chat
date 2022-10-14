package server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {

    //наш сервер
    private final Server server;

    // сокет, через который сервер общается с клиентом
    private final Socket socket;

    //сообщение для отправки в чат
    private final BufferedWriter msgOut;

    // поток чтения из сокета
    private final BufferedReader in;

    //количество пользователей в чате
    private static int clientsCount = 0;


    public ClientThread(Socket clientSocket, Server server) throws IOException {
        clientsCount++;
        this.socket = clientSocket;
        this.server = server;
        this.msgOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            // сообщения от сервера
            server.sendMsgToAll("Новый участник вошел в чат");
            server.sendMsgToAll("В чате " + clientsCount + " человек");
            break;
        }
        // первое сообщение отправленное сюда - это никнейм
        String clientMessage;
        try {
            clientMessage = in.readLine();
            try {
                msgOut.write(clientMessage + "\n");
                msgOut.flush();
            } catch (IOException ignored) {
            }
            try {
                while (true) {
                    clientMessage = in.readLine();
                    // когда клиент введет "exit" на сервер у нас придет пустое сообщение
                    // делаем для него обработчик
                    if (clientMessage.equals(null)) {
                        this.exitClient();
                        break;
                    }
                    System.out.println(clientMessage);
                    // отправляем сообщение всем пользователям
                    server.sendMsgToAll(clientMessage);
                }
            } catch (NullPointerException ignored) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.exitClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String msg) {
        try {
            msgOut.write(msg + "\n");
            msgOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //прерываение нити и выход
    private void exitClient() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
            in.close();
            msgOut.close();
            server.removeClient(this);
            clientsCount--;
            server.sendMsgToAll("Клиентов в чате " + clientsCount);
        }
    }
}
