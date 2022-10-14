package client;

import server.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    Logger logger = Logger.getInstance();

    private final String host;
    private final int port;

    //сокет клиента
    private Socket socket;
    // поток чтения из сокета
    private BufferedReader in;
    //поток в сокет
    private PrintWriter outMsg;
    //поток из консоли
    private BufferedReader input;
    // имя клиента
    private String clientNickName;


    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            //подключаемся к серверу
            this.socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(System.in));
            outMsg = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //спросим имя/ник
            this.addNickName();
            new Thread(new MsgFromServer()).start();
            new Thread(new MsgToServer()).start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Client.this.exitClient();
        }
    }

    public void addNickName() throws IOException {
        logger.log("Введите ваше имя/nickname ");
        clientNickName = input.readLine();
        outMsg.write("Привет " + clientNickName + "\n");
        outMsg.flush();

    }

    //прерываение нити и выход
    public void exitClient() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                outMsg.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MsgFromServer implements Runnable {
        @Override
        public void run() {
            String msg;
            try {
                while (true) {
                    //ожидаем сообщение от сервера
                    msg = in.readLine();
                    if (msg.equals("exit")) {
                        Client.this.exitClient();
                        break;
                    }
                    logger.log(msg);
                }
            } catch (IOException e) {
                Client.this.exitClient();
            }
        }
    }

    public class MsgToServer implements Runnable {

        @Override
        public void run() {
            while (true) {
                String userMessage;
                try {
                    userMessage = input.readLine();
                    if (userMessage.equals("exit")) {
                        outMsg.write("exit" + "\n");
                        Client.this.exitClient();
                        break;
                    } else {
                        outMsg.write((clientNickName + ": " + userMessage + "\n"));
                    }
                    outMsg.flush();
                } catch (Exception e) {
                    Client.this.exitClient();
                }
            }
        }
    }
}