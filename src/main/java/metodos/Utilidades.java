package metodos;

import com.mysql.cj.jdbc.Blob;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import static otros.Variables.conexion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gonfe
 */
public class Utilidades {

    /**
     * Carga una imagen de una url y la transforma en un ImageIcon
     *
     * @param Url Url de la imagen
     * @return Un ImageIcon con la imagen
     */
    public static ImageIcon UrlIcon(String Url, int x, int y) {
        ImageIcon icon = null;
        try {
            URL url = new URL(Url);
            BufferedImage img = ImageIO.read(url);
            BufferedImage resized = resize(img, x, y);
            icon = new ImageIcon(resized);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    /**
     * Redimensiona una imagen al tamaño en pixeles deseado Source:
     * https://memorynotfound.com/java-resize-image-fixed-width-height-example/
     *
     * @param img Un BufferedImage que se desee redimensionar
     * @param height Altura que se desee de la imagen en pixeles
     * @param width Anchura que se desee de la imagen en pixeles
     * @return Un BufferedImage con la imagen redimensionada
     */
    public static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    /**
     * Metodo para generar un salt totalmente aleatorio y seguro
     *
     * @return Un byte[] con el salt
     */
    public static byte[] salt() {
        byte[] salt = new byte[16];
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salt;
    }

    /**
     * Genera un digesto con la contraseña deseada y un salt con el algoritmo
     * SHA-512
     *
     * @param passwordToHash Contraseña
     * @param salt salt
     * @return El digesto en un String
     */
    public static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static Blob ImageToBlob(Image img) throws IOException, SQLException {

        BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bi.getGraphics().drawImage(img, 0, 0, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "png", baos);
        byte[] imageInByte = baos.toByteArray();
        Blob Imagen = (Blob) conexion.createBlob();
        Imagen.setBytes(1, imageInByte);

        return Imagen;
    }

    public static Image iconToImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            return ((ImageIcon) icon).getImage();
        } else {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gd = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(w, h);
            Graphics2D g = image.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            return image;
        }
    }

    public static ImageIcon BlobToImageIcon(Blob img) {
        InputStream in = null;
        try {
            in = img.getBinaryStream();
            BufferedImage image = ImageIO.read(in);
            return new ImageIcon(image);
        } catch (SQLException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static String getIp() throws MalformedURLException, IOException {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
        String ip = in.readLine();
        return ip;
    }
    
    public static final String SUN_JAVA_COMMAND = "sun.java.command";
    /**
     * Reiniciar la aplicacion actual
     * Source: https://dzone.com/articles/programmatically-restart-java
     * @param runBeforeRestart some custom code to be run before restarting
     * @throws IOException
     */
    public static void restartApplication(){
        try {
            String java = System.getProperty("java.home") + "/bin/java";
            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            StringBuffer vmArgsOneLine = new StringBuffer();
            for (String arg : vmArguments) {
                if (!arg.contains("-agentlib")) {
                    vmArgsOneLine.append(arg);
                    vmArgsOneLine.append(" ");
                }
            }
            final StringBuffer cmd = new StringBuffer("\"" + java + "\" " + vmArgsOneLine);
            String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
            if (mainCommand[0].endsWith(".jar")) {
                cmd.append("-jar " + new File(mainCommand[0]).getPath());
            } else {
                cmd.append("-cp \"" + System.getProperty("java.class.path") + "\" " + mainCommand[0]);
            }
            for (int i = 1; i < mainCommand.length; i++) {
                cmd.append(" ");
                cmd.append(mainCommand[i]);
            }
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        Runtime.getRuntime().exec(cmd.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.exit(0);
        } catch (Exception e) {
            try {
                throw new IOException("Error cuando se intentaba reiniciar la app", e);
            } catch (IOException ex) {
                Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
