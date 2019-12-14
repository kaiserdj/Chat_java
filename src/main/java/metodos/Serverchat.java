/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metodos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gonfe
 */
public class Serverchat implements Runnable {
    private static ServerSocket server;
    private static int port = 0;

    @Override
    public void run() {
        try {
            server = new ServerSocket(port);
            while (true) {
                System.out.println("Server abierto - Puerto: " + server.getLocalPort());
                System.out.println("Esperando un cliente");
                Socket socket = server.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String message = null;
                try {
                    message = (String) ois.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Serverchat.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Mensaje recivido: " + message);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject("Cliente " + message);
                ois.close();
                oos.close();
                socket.close();
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
            }
            System.out.println("Apagando el servidor!!");
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(Serverchat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
