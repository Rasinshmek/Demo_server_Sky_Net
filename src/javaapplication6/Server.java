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
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static final int PORT = 7000;
    public static LinkedList<Nitka> serverList = new LinkedList<>(); // список всех нитей

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Nitka(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();

        }
    }

}

class Nitka extends Thread {

    private static final String url = "jdbc:mysql://db4free.net:3306/skynet_master";
    private static final String user = "skyadmin";
    private static final String password = "09yecgaa09";
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

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
        send("?CON");
        try {
            while (true) {
                word = in.readUTF();
                if (word.equals("stop")) {
                    System.out.println("Server stoped");
                    System.exit(0);
                    break;
                }
                switch (word) {
                    case ("CON-OK"):
                        send("?CON-TYPE");
                        switch (in.readUTF()) {
                            case ("TYPE=PEOPLE"):
                                break;
                            case ("TYPE=MACHINE"):
                                send("?MAC");
                                String[] mac = in.readUTF().split("=");
                                send("?NAME");
                                String[] name = in.readUTF().split("=");
                                send("?IP");
                                String[] ip = in.readUTF().split("=");
                                String select = select("SELECT id FROM skynet_master.machine WHERE MAC='" + mac[1] + "'");
                                if (!select.isEmpty()) {
                                    update("INSERT INTO skynet_master.machine (name,IP,MAC) VALUES ('" + name[1] + "','" + ip[1] + "','" + mac[1] + "')");
                                } else {
                                    update("UPDATE skynet_master.machine SET name='" + name[1] + "',IP='" + ip[1] + "' WHERE MAC='" + mac[1] + "'");

                                }

                                break;
                            default:
                                break;
                        }
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
            System.out.println(msg);
            out.writeUTF(msg + "\n");
            out.flush();
        } catch (IOException ignored) {
        }
    }

    private void update(String ins) {
        try {
            con = DriverManager.getConnection(url + "?user=" + user + "&password=" + password);
            stmt = con.createStatement();
            stmt.executeUpdate(ins);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
                /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) {
                /*can't do anything */ }
        }
    }

    private String select(String sel) {
        ResultSet rss = null;
        String temp = "";
        int i = 0;
        try {
            con = DriverManager.getConnection(url + "?user=" + user + "&password=" + password);
            stmt = con.createStatement();
            rss = stmt.executeQuery(sel);
            while (rss.next()) {
                temp += rss.getString(i);
                i++;
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
                /*can't do anything */ }
            try {
                stmt.close();
            } catch (SQLException se) {
                /*can't do anything */ }
        }
        return temp;
    }
}
