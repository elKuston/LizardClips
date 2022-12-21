package gui;

import constant.TipoConector;
import lombok.Data;
import modelo.Circuito;
import modelo.Conector;
import utils.ImageUtils;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

@Data
public class ModeloPieza {
    private final ImageIcon imagen;
    private List<Conector> conectores;
    private Dimension tamano;
    private Circuito circuito;


    public ModeloPieza(Circuito circuito, String pathImagen, int ancho, int alto, List<Conector> conectores) {
        conectores.forEach(con -> con.setPieza(this));

        this.circuito = circuito;
        this.conectores = conectores;
        imagen = ImageUtils.cargarImagenEscalada(pathImagen, ancho, alto);
        tamano = new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
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


    public void dibujar(PanelCircuito panelCircuito, Graphics g, Point posicion, boolean dibujarContorno, Map<TipoConector, Color> coloresConectores) {
        getImagen().paintIcon(panelCircuito, g, (int) posicion.getX(), (int) posicion.getY());
        if (dibujarContorno) {
            g.drawRect((int) posicion.getX(), (int) posicion.getY(), getWidth(), getHeight());
        }
        for (Conector c : conectores) {
            g.setColor(coloresConectores.get(c.getTipoConector()));
            Point pos = calcularPosicionAbsolutaConector(c, posicion);
            g.fillOval((int) (pos.getX() - Conector.RADIO), (int) (pos.getY() - Conector.RADIO),
                    2 * Conector.RADIO, 2 * Conector.RADIO);
            g.setColor(Color.BLACK);
        }
    }

    public int getWidth() {
        return (int) tamano.getWidth();
    }

    public int getHeight() {
        return (int) tamano.getHeight();
    }

    public Rectangle getBounds() {
        return new Rectangle(circuito.getPosicionPieza(this), getTamano());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ModeloPieza that = (ModeloPieza) o;

        if (!imagen.equals(that.imagen))
            return false;
        if (!tamano.equals(that.tamano))
            return false;
        return circuito.equals(that.circuito);
    }

    @Override
    public int hashCode() {
        int result = imagen.hashCode();
        result = 31 * result + tamano.hashCode();
        result = 31 * result + circuito.hashCode();
        return result;
    }
}
