package gui;

import componentes.Conector;
import constant.TipoConector;
import lombok.Data;
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
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class PanelPieza extends JComponent {
    private final ImageIcon imagen;
    private List<Conector> conectores;


    public PanelPieza(String pathImagen, int ancho, int alto, List<Conector> conectores) {
        imagen = ImageUtils.cargarImagenEscalada(pathImagen, ancho, alto);
        this.conectores = conectores;
    }

    public Point calcularPosicionAbsolutaConector(Conector conector, Point posicionPanelPieza) {

        Point posicionConector = new Point(
                (int) (posicionPanelPieza.getX() + conector.getPosicionRelativaX() * getWidth()),
                (int) (posicionPanelPieza.getY() + conector.getPosicionRelativaY() * getHeight()));
        //Mantener posición dentro de los limites de la pieza
        if (posicionConector.getX() - Conector.RADIO < posicionPanelPieza.getX()) {
            posicionConector.translate(Conector.RADIO, 0);
        }
        if (posicionConector.getX() + Conector.RADIO > posicionPanelPieza.getX() + getWidth()) {
            posicionConector.translate(-Conector.RADIO, 0);
        }
        if (posicionConector.getY() - Conector.RADIO < posicionPanelPieza.getY()) {
            posicionConector.translate(0, Conector.RADIO);
        }
        if (posicionConector.getY() + Conector.RADIO > posicionPanelPieza.getY() + getHeight()) {
            posicionConector.translate(0, -Conector.RADIO);
        }
        return posicionConector;
    }


    public void dibujar(Graphics g, Point posicion, boolean dibujarContorno, Map<TipoConector, Color> coloresConectores) {
        getImagen().paintIcon(this, g, (int) posicion.getX(), (int) posicion.getY());
        if (dibujarContorno) {
            Rectangle b = getBounds();
            g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
        }
        for (Conector c : conectores) {
            g.setColor(coloresConectores.get(c.getTipoConector()));
            Point pos = calcularPosicionAbsolutaConector(c, posicion);
            g.fillOval((int) (pos.getX() - Conector.RADIO),
                    (int) (pos.getY() - Conector.RADIO),
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
}
