package controlador;

import constant.TipoConector;
import gui.ModeloPieza;
import gui.PanelCircuito;
import modelo.Circuito;
import modelo.Conector;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControladorCircuito {

    private final Circuito circuito;
    private final PanelCircuito panelCircuito;

    public ControladorCircuito(Circuito circuito, PanelCircuito panelCircuito) {
        panelCircuito.setControladorCircuito(this);
        this.circuito = circuito;
        this.panelCircuito = panelCircuito;
    }

    public void colocarPieza(ModeloPieza pieza, Point posicion) {
        Rectangle controrno = new Rectangle((int) (posicion.getX()), (int) (posicion.getY()),
                (int) pieza.getTamano().getWidth(), (int) pieza.getTamano().getHeight());
        pieza.setBounds(controrno);
        circuito.moverPieza(pieza, posicion);
    }

    public void borrarPieza(ModeloPieza pieza) {
        circuito.borrarPieza(pieza);
    }

    public void arrastrarPieza(ModeloPieza pieza, Point posicion, Dimension grabPoint) {
        Point posicionReal = posicion;
        posicionReal.translate((int) -grabPoint.getWidth(), (int) -grabPoint.getHeight());
        if (dentroDelPanel(posicionReal, pieza.getTamano())) {
            colocarPieza(pieza, posicionReal);
        }
    }

    private boolean dentroDelPanel(Point posicion, Dimension tamanoPieza) {
        Rectangle tamanoPanel = panelCircuito.getBounds();
        tamanoPanel.setLocation(0,
                0);//Si no, se tiene un offset igual al tamaño de los componentes a la izquierda en X y arriba en Y
        Point esquinaInferiorDerecha = new Point((int) (posicion.getX() + tamanoPieza.getWidth()),
                (int) (posicion.getY() + tamanoPieza.getHeight()));
        return tamanoPanel.contains(posicion) && tamanoPanel.contains(esquinaInferiorDerecha);
    }

    public ModeloPieza getPiezaByPosicion(Point posicionRaton) {
        return circuito.getComponentes().entrySet().stream().filter(
                (par -> puntoDentroDeBounds(posicionRaton, par))).map(
                Map.Entry::getKey).findFirst().orElse(null);
    }

    public Conector getConectorByPosicion(Point posicion) {
        return getConectorByPosicion(
                circuito.getComponentes().keySet().stream().map(ModeloPieza::getConectores).flatMap(
                        List::stream).iterator(), posicion);
    }

    private Conector getConectorByPosicion(Iterator<Conector> candidatos, Point posicion) {
        Conector conector = null;
        while (conector == null && candidatos.hasNext()) {
            Conector c = candidatos.next();
            Point posicionConector = c.getPieza().calcularPosicionAbsolutaConector(c,
                    getPosicionPieza(c.getPieza()));
            double d = Math.sqrt(Math.pow(posicionConector.getX() - posicion.getX(), 2) +
                    Math.pow(posicionConector.getY() - posicion.getY(), 2));
            System.out.println(d);
            if (d <= Conector.RADIO) {
                System.out.println("clicking on conector");
                conector = c;
            }
        }
        return conector;
    }

    public Conector getConectorByPosicion(ModeloPieza modeloPieza, Point posicion) {
        Iterator<Conector> it = modeloPieza.getConectores().iterator();
        return getConectorByPosicion(it, posicion);
    }

    public Point getPosicionPieza(ModeloPieza pieza) {
        return circuito.getComponentes().get(pieza);
    }

    private boolean puntoDentroDeBounds(Point punto, Map.Entry<ModeloPieza, Point> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds.contains(punto);
    }

    public Set<Map.Entry<ModeloPieza, Point>> getPiezasPosicionEntrySet() {
        return circuito.getComponentes().entrySet();
    }

    public void generarModeloPieza(String pathImagen, int ancho, int alto, List<Conector> conectores) {
        panelCircuito.addImagePanelByDragging(new ModeloPieza(pathImagen, ancho, alto, conectores));
    }

    public void generarResistor() {
        generarModeloPieza("media/res.png", 200, 100,
                List.of(new Conector(0, 0.5, TipoConector.ENTRADA),
                        new Conector(1, 0.5, TipoConector.SALIDA)));
    }

    public void generarAnd() {
        generarModeloPieza("media/and.png", 200, 100,
                List.of(new Conector(0, 0.25, TipoConector.ENTRADA),
                        new Conector(0, 0.75, TipoConector.ENTRADA),
                        new Conector(1, 0.5, TipoConector.SALIDA)));
    }


}
