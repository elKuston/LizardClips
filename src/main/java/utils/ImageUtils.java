package utils;

import javax.swing.ImageIcon;
import java.awt.Image;

public class ImageUtils {
    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto, int modo) {
        return new ImageIcon(
                new ImageIcon(pathImagen)
                        .getImage()
                        .getScaledInstance(ancho, alto, modo)
        );
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto) {
        return cargarImagenEscalada(pathImagen, ancho, alto, Image.SCALE_FAST);
    }
}
