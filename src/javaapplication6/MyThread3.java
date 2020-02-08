/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication6;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alpha
 */
 class MyThread3 extends Thread {

        private ServerSocket serverSocket;
        private Socket socket;
        private Scanner scanner;
        private String request;
        private PrintWriter writer;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8888);
                while (true) {
                    socket = serverSocket.accept();
                    scanner = new Scanner(socket.getInputStream());
                    request = scanner.nextLine();
                    System.out.println("REQ: " + request);
                   
                    scanner.close();
                    socket.close();
                    // JOptionPane.showMessageDialog(null, time, "Сообщение от " + timem[timem.length - 1], PLAIN_MESSAGE);
                }
            } catch (IOException ex) {
               
                Logger.getLogger(MyThread3.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }