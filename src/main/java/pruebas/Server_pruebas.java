/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import Objetos.Usuario;
import java.io.*;
import java.net.*;

/**
 *
 * @author gonfe
 */
public class Server_pruebas {

    private static ServerSocket server;
    private static int port = 9876;

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        server = new ServerSocket(port);
        int i = 0;
        String mensaje = "";
        
        while (true) {
            System.out.println("Esperando cliente");

            Socket socket = server.accept();
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            if (i == 0) {
                MyClass user = (MyClass) ois.readObject();
                System.out.println("Usuario recivido: " + user.x);
                i++;
            } else {
                mensaje = (String) ois.readObject();
                System.out.println("Mensaje recivido: " + mensaje);
            }

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject("Hola");

            System.out.println("------------");

            ois.close();
            oos.close();
            socket.close();

            if (mensaje.equalsIgnoreCase("Cerrar")) {
                break;
            }
        }
        System.out.println("Shutting down Socket server!!");

        server.close();
    }
}
