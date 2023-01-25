package utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static final String MEDIA_BASE_FOLDER = "src/main/resources/media";

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto, int modo) {
        return new ImageIcon(
                new ImageIcon(pathImagen).getImage().getScaledInstance(ancho, alto, modo));
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
