package caponera.uned.tfm.lizardclips.utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    public static final String MEDIA_BASE_FOLDER = "media";
    public static final int DEFAULT_IMAGE_WIDTH = 65;
    public static final int DEFAULT_IMAGE_HEIGHT = 65;
    private static Map<DescriptorImagen, ImageIcon> imagenesCacheadas = new HashMap<>();

    public static final String pathImagenMedia(String nombreImagen) {
        return ImageUtils.MEDIA_BASE_FOLDER + "/" + nombreImagen;
    }

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
        return rescalarImagen(imagen, ancho, alto, Image.SCALE_SMOOTH);
    }

    public static ImageIcon cargarImageneEscaladaPreserveRatio(String pathImagen, int ancho, int alto) {
        ImageIcon raw = cargarImageIcon(pathImagen);
        return rescalarImagenPreserveRatio(pathImagen, raw, ancho, alto);
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, double ratio) {
        ImageIcon raw = cargarImageIcon(pathImagen);
        return rescalarImagenPreserveRatio(pathImagen, raw, (int) (raw.getIconWidth() * ratio),
                (int) (raw.getIconHeight() * ratio));
    }

    public static ImageIcon rescalarImagenPreserveRatio(String pathImagen, ImageIcon raw, int ancho, int alto) {
        double imageRatio = 1.0 * raw.getIconWidth() / raw.getIconHeight();
        double containerRatio = 1.0 * ancho / alto;
        if (imageRatio > containerRatio) {
            alto = (int) (ancho / imageRatio);
        } else {
            ancho = (int) (imageRatio * alto);
        }
        DescriptorImagen desc = new DescriptorImagen(pathImagen, ancho, alto);
        if (!imagenesCacheadas.containsKey(desc)) {
            imagenesCacheadas.put(desc, rescalarImagen(raw, ancho, alto, Image.SCALE_SMOOTH));
        }

        return imagenesCacheadas.get(desc);
    }

    public static ImageIcon rescalarImagenPreserveRatio(ImageIcon raw, int ancho, int alto) {
        return rescalarImagenPreserveRatio(String.valueOf(raw.hashCode()), raw, ancho, alto);
    }

    public static ImageIcon cargarImagenEscalada(String pathImagen, int ancho, int alto) {
        return cargarImagenEscalada(pathImagen, ancho, alto, Image.SCALE_SMOOTH);
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

    public static ImageIcon rotar(ImageIcon original, int grados) {
        BufferedImage image = new BufferedImage(original.getIconWidth(), original.getIconHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        original.paintIcon(null, g, 0, 0);
        g.dispose();
        // Calculate the new size of the image based on the angle of rotaion
        double radians = Math.toRadians(grados);
        double sin = Math.abs(Math.sin(radians));
        double cos = Math.abs(Math.cos(radians));
        int newWidth = (int) Math.round(image.getWidth() * cos + image.getHeight() * sin);
        int newHeight = (int) Math.round(image.getWidth() * sin + image.getHeight() * cos);

        // Create a new image
        BufferedImage rotate = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotate.createGraphics();
        // Calculate the "anchor" point around which the image will be rotated
        int x = (newWidth - image.getWidth()) / 2;
        int y = (newHeight - image.getHeight()) / 2;
        // Transform the origin point around the anchor point
        AffineTransform at = new AffineTransform();
        at.setToRotation(radians, x + (image.getWidth() / 2.0), y + (image.getHeight() / 2.0));
        at.translate(x, y);
        g2d.setTransform(at);
        // Paint the originl image
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return new ImageIcon(rotate);
    }
}
