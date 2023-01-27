package caponera.uned.tfm.lizardclips.utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    public static final String MEDIA_BASE_FOLDER = "media";

    public static ImageIcon cargarImageIcon(String pathImagen) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //ClassLoader classLoader = ImageUtils.class.getClassLoader();
        URL resource = classLoader.getResource(pathImagen);
        return new ImageIcon(resource);
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto, int modo) {
        return new ImageIcon(
                cargarImageIcon(pathImagen).getImage().getScaledInstance(ancho, alto, modo));
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto) {
        return cargarImagenEscalada(pathImagen, ancho, alto, Image.SCALE_FAST);
    }

    public static byte[] bytesFromBufferedImage(BufferedImage bi) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public static ImageIcon imageIconFromBytes(byte[] bytes) {
        return new ImageIcon(bytes);
    }
}
