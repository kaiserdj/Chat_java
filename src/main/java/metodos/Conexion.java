package metodos;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gonfe
 */
public class Conexion {

    /**
     * Conexion a la base de datos
     *
     * @param Url Url del hosting de la base de datos
     * @param baseDatos Nombre de la base de datos
     * @param username Usuario de la base de datos
     * @param password Contraseña de la base de datos
     * @return Variable connection de la base de datos MYSQL
     */
    public static Connection mySQL(String Url, String baseDatos, String username, String password) {
        Connection con = null;
        try {
            java.util.Properties props = new java.util.Properties();
            props.put("charSet", "UTF-8");
            props.put("user", username);
            props.put("password", password);
            props.put("useLegacyDatetimeCode", "false");
            props.put("serverTimezone", "UTC");
            String url = "jdbc:mysql://" + Url + "/" + baseDatos;
            con = (Connection) DriverManager.getConnection(url, props);
            return (Connection) con;
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
            return null;
        }
    }

    /**
     * Lee toda una tabla
     *
     * @param conexion Variable connection de la base de datos MYSQL
     * @param sql Tabla de la que se desea
     * @return Arraylist con el primer valor los nombres de las columnas en un
     * Array de Strings y todos los demas son las lines de la tabla en Arrays de
     * Strings
     */
    public static ArrayList leerTabla(Connection conexion, String sql) {
        ArrayList<String[]> base = new ArrayList<String[]>();
        if (conexion != null) {
            try {
                ResultSet rs = null;
                Statement sentencia = (Statement) conexion.createStatement();
                rs = sentencia.executeQuery(sql);

                ResultSetMetaData rsMetaData = (ResultSetMetaData) rs.getMetaData();
                int numberOfColumns = rsMetaData.getColumnCount();
                String[] columnas = new String[numberOfColumns];

                for (int i = 0; i < numberOfColumns; i++) {
                    columnas[i] = rsMetaData.getColumnName(i + 1);
                }

                base.add(columnas);

                while (rs.next()) {
                    String[] datos = new String[columnas.length];

                    for (int i = 0; i < columnas.length; i++) {
                        datos[i] = rs.getString(columnas[i]);
                    }
                    base.add(datos);
                }
                return base;
            } catch (SQLException ex) {
                System.out.println("Error: " + ex);
                JOptionPane.showMessageDialog(null, "<html><body>Se a producido un error en la conexion<br>Error: " + ex, "Error conexión", JOptionPane.ERROR_MESSAGE);
            }
        }
        return base;
    }

    public static int ModificarDato(Connection conexion, String sql) {
        if (conexion != null) {
            try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
                return stmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }
}
