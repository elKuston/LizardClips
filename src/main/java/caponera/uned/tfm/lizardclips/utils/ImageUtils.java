package caponera.uned.tfm.lizardclips.utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    public static final String MEDIA_BASE_FOLDER = "media";
    public static final int DEFAULT_IMAGE_WIDTH = 50;
    public static final int DEFAULT_IMAGE_HEIGHT = 50;
    private static Map<DescriptorImagen, ImageIcon> imagenesCacheadas = new HashMap<>();

    public static ImageIcon cargarImageIcon(String pathImagen) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //ClassLoader classLoader = ImageUtils.class.getClassLoader();
        URL resource = classLoader.getResource(pathImagen);
        System.out.println("Cargando " + pathImagen);
        return new ImageIcon(resource);
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto, int modo) {
        DescriptorImagen desc = new DescriptorImagen(pathImagen, ancho, alto);
        if (!imagenesCacheadas.containsKey(desc)) {
            imagenesCacheadas.put(desc,
                    rescalarImagen(cargarImageIcon(pathImagen), ancho, alto, modo));
        }
        return imagenesCacheadas.get(desc);
    }

    public static ImageIcon rescalarImagen(ImageIcon imagen, int ancho, int alto, int modo) {
        return new ImageIcon(imagen.getImage().getScaledInstance(ancho, alto, modo));
    }

    public static ImageIcon rescalarImagen(ImageIcon imagen, int ancho, int alto) {
        return rescalarImagen(imagen, ancho, alto, Image.SCALE_FAST);
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
