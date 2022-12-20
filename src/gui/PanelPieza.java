package gui;

import componentes.Conector;
import lombok.EqualsAndHashCode;
import utils.ImageUtils;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
public class PanelPieza extends JComponent {
    private final ImageIcon imagen;
    private List<Conector> conectores;


    public PanelPieza(String pathImagen, int ancho, int alto, List<Conector> conectores) {
        imagen = ImageUtils.cargarImagenEscalada(pathImagen, ancho, alto);
        this.conectores = conectores;
    }


    public void dibujar(Graphics g, Point posicion, boolean dibujarContorno) {
        getImagen().paintIcon(this, g, (int) posicion.getX(), (int) posicion.getY());
        if (dibujarContorno) {
            Rectangle b = getBounds();
            g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
        }
        for (Conector c : conectores) {
            switch (c.getTipoConector()) {
                case SALIDA -> g.setColor(Color.GREEN);
                case ENTRADA -> g.setColor(Color.BLUE);
            }
            g.fillOval((int) (posicion.getX() + c.getPosicionRelativaX() * getWidth() - Conector.RADIO),
                    (int) (posicion.getY() + c.getPosicionRelativaY() * getHeight() - Conector.RADIO),
                    2 * Conector.RADIO, 2 * Conector.RADIO);
            g.setColor(Color.BLACK);
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        System.out.println("pintando");
        imagen.paintIcon(this, graphics, 0, 0);
        Rectangle limites = getBounds();
        graphics.drawRect((int) limites.getX(), (int) limites.getY(), (int) limites.getWidth(),
                (int) limites.getHeight());
    }

    public Dimension getTamano() {
        return new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
    }

    public ImageIcon getImagen() {
        return imagen;
    }
}
