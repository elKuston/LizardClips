package gui;

import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
public class ImagePanel extends JComponent {
    private final ImageIcon imagen;


    public ImagePanel(String pathImagen, Point posicion, int ancho, int alto) {
        imagen = escalarImagen(pathImagen, ancho, alto);
    }


    private ImageIcon escalarImagen(String pathImagen, int ancho, int alto) {
        return new ImageIcon(
                new ImageIcon(pathImagen)
                        .getImage()
                        .getScaledInstance(ancho, alto, Image.SCALE_FAST)
        );
    }

    public void dibujar(Graphics g, Point posicion, boolean dibujarContorno) {
        getImagen().paintIcon(this, g, (int) posicion.getX(), (int) posicion.getY());
        if (dibujarContorno) {//TODO boolean dibujarContornos
            Rectangle b = getBounds();
            g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        System.out.println("pintando");
        imagen.paintIcon(this, graphics, 0, 0);
        Rectangle limites = getBounds();
        graphics.drawRect((int) limites.getX(), (int) limites.getY(), (int) limites.getWidth(), (int) limites.getHeight());
    }

    public Dimension getTamano() {
        return new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
    }

    public ImageIcon getImagen() {
        return imagen;
    }
}
