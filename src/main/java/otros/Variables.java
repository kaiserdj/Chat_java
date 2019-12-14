/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package otros;

import Objetos.Usuario;
import java.sql.Connection;
import metodos.Conexion;

/**
 *
 * @author gonfe
 */
public class Variables {
    //Variables publicas

    //Conexion BD
    public static String url = "localhost:3306";
    public static String bd = "chat";
    public static String bd_user = "kaiserdj";
    public static String bd_pass = "kaiserdj";
    public static Connection conexion = Conexion.mySQL(url, bd, bd_user, bd_pass);

    //Usuario iniciado sesión
    public static Usuario usuario = new Usuario();

    public static void DetectCerrarPrograma() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if(usuario.getValido()){
                    usuario.setEstado(0);
                }
            }
        });
    }
}
