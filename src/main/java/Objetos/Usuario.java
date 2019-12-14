/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import com.kaiserdj.chat.client.Login;
import com.mysql.cj.jdbc.Blob;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import metodos.Conexion;
import metodos.Utilidades;
import static otros.Variables.conexion;

/**
 *
 * @author gonfe
 */
public class Usuario {

    //Elementos
    int id, estado, tipo;
    String username, password, codigo;
    byte[] salt;
    Blob avatar;
    Boolean valido = false;

    public Usuario() {
    }

    public void inciar_sesion(String username, String password) {
        String sql_username = "SELECT Usuarios.username, Usuarios.salt FROM `Usuarios` WHERE username=?";
        String sql_password = "SELECT Usuarios.password FROM `Usuarios` WHERE username=? and password=?";
        byte[] salt = null;

        try {
            PreparedStatement rs = (PreparedStatement) conexion.prepareStatement(sql_username);
            rs.setString(1, username);
            ResultSet rd = rs.executeQuery();
            if (rd.next()) {
                System.out.println("El usuario existe");
                salt = rd.getBytes("salt");

                String contraseña = Utilidades.get_SHA_512_SecurePassword(password, salt);

                try {
                    rs = (PreparedStatement) conexion.prepareStatement(sql_password);
                    rs.setString(1, username);
                    rs.setString(2, contraseña);
                    rd = rs.executeQuery();
                    if (rd.next()) {
                        System.out.println("Inicio de sesíon correcto");
                        this.username = username;
                        this.password = contraseña;
                        this.salt = salt;
                        valido = true;
                        get_datos();
                        setEstado(1);
                    } else {
                        System.out.println("Inicio de sesíon fallido");
                        JOptionPane.showMessageDialog(null, "Error al iniciar sesión", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                System.out.println("El usuario no existe");
                JOptionPane.showMessageDialog(null, "Error al iniciar sesión", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void registrar_user(String username, String password, int tipo, Icon avatar) throws IOException, SQLException {
        this.username = username;
        estado = 0;
        this.tipo = tipo;
        codigo = "";
        this.avatar = Utilidades.ImageToBlob(Utilidades.iconToImage(avatar));

        byte[] salt = Utilidades.salt();
        String contraseña = Utilidades.get_SHA_512_SecurePassword(password, salt);
        this.password = contraseña;

        try {
            String sql = "INSERT INTO `Usuarios` (`Id`, `username`, `password`, `salt`, `codigo`, `avatar`, `estado`, `tipo`) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = (PreparedStatement) conexion.prepareStatement(sql);
            ps.setString(1, this.username);
            ps.setString(2, this.password);
            ps.setBytes(3, salt);
            ps.setString(4, codigo);
            ps.setBlob(5, this.avatar);
            ps.setInt(6, estado);
            ps.setInt(7, this.tipo);
            ps.execute();
            System.out.println("Registro completado");
            get_datos();
        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void get_datos() {
        try {
            String sql_datos = "SELECT * FROM `Usuarios` WHERE username=? and password=?";
            PreparedStatement rs = (PreparedStatement) conexion.prepareStatement(sql_datos);
            rs.setString(1, username);
            rs.setString(2, password);
            ResultSet rd = rs.executeQuery();
            if (rd.next()) {
                id = rd.getInt("Id");
                username = rd.getString("username");
                password = rd.getString("password");
                salt = rd.getBytes("salt");
                avatar = (Blob) rd.getBlob("avatar");
                codigo = rd.getString("codigo");
                estado = rd.getInt("estado");
                tipo = rd.getInt("tipo");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        Conexion.ModificarDato(conexion, "UPDATE `Usuarios` SET `estado` = '" + estado + "' WHERE `Usuarios`.`Id` = " + id + ";");
        this.estado = estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Blob getImagen() {
        return avatar;
    }

    public void setImagen(Blob avatar) {
        this.avatar = avatar;
    }

    public Blob getAvatar() {
        return avatar;
    }

    public void setAvatar(Blob avatar) {
        this.avatar = avatar;
    }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }
}
