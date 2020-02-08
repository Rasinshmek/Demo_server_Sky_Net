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
import javaapplication6.MyThread3;
import java.net.*;
import java.io.*;
public class JavaApplication6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
               MyThread3 t=new MyThread3();
               t.start();
    }
    
}
