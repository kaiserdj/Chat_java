/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import otros.Variables;

/**
 *
 * @author gonfe
 */
public class Cliente_pruebas {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {

        //Variables.usuario.inciar_sesion("julia", "julia");
        MyClass myObj = new MyClass();
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        for (int i = 0; i < 3; i++) {

            socket = new Socket(host.getHostName(), 9876);

            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Enviando solicitud a servidor");
            if (i == 0) {
                oos.writeObject(myObj);
            } else {
                oos.writeObject("Cerrar");
            }

            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Mensaje: " + message);
            System.out.println("------------");
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }
}
