/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

/**
 *
 * @author alpha
 */
import javaapplication6.Nitka;
import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = 7000;
    public static LinkedList<Nitka> serverList = new LinkedList<>(); // список всех нитей

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                try {
                    serverList.add(new Nitka(socket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его при завершении работы:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}

class Nitka extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private DataInputStream in; // поток чтения из сокета
    private DataOutputStream out; // поток записи в сокет

    public Nitka(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию исключения, оно проброситься дальше
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        start(); // вызываем run()
    }

    @Override
    public void run() {
        String word;
        try {

            while (true) {
                word = in.readUTF();
                if (word.equals("stop")) {
                    break;
                }
                switch (word) {
                    case ("HARD: GETVARIABLES"):
                        send("HARD: SETVARIABLES~~var1=11~~var2=16~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var1=11~~var3=16~~var2=16~~var2=16~~var2=16~~var2=16~~var2=16~~var2=16~~var2=16~~var2=16~~var2=16~~");
                        System.out.println("HARD: setvariables");
                        break;
                    case ("Hello World"):
                        send("HARD: GETCMD");
                        System.out.println("HARD: GETCMD");
                        break;
                    default:
                        // out.close();
                        break;
                }
               
            }

        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            out.writeUTF(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }
}
