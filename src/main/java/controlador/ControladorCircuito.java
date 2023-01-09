package controlador;

import constant.TipoConector;
import gui.PanelCircuito;
import modelo.Circuito;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControladorCircuito {
    private final Circuito circuito;
    private final PanelCircuito panelCircuito;

    public ControladorCircuito(Circuito circuito, PanelCircuito panelCircuito) {
        panelCircuito.setControladorCircuito(this);
        this.circuito = circuito;
        this.panelCircuito = panelCircuito;
    }

    public void colocarPieza(Pieza pieza, Point posicion) {
        circuito.moverPieza(pieza, posicion);
    }

    public void borrarPieza(Pieza pieza) {
        circuito.borrarPieza(pieza);
    }

    public void arrastrarPieza(Pieza pieza, Point posicion, Dimension grabPoint) {
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

    public Pieza getPiezaByPosicion(Point posicionRaton) {
        return circuito.getComponentes().entrySet().stream()
                       .filter((par -> puntoDentroDeBounds(posicionRaton, par)))
                       .map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public Conector getConectorByPosicion(Point posicion) {
        return getConectorByPosicion(getAllConectoresStream().iterator(), posicion);
    }

    private Conector getConectorByPosicion(Iterator<Conector> candidatos, Point posicion) {
        Conector conector = null;
        while (conector == null && candidatos.hasNext()) {
            Conector c = candidatos.next();
            Point posicionConector =
                    c.getPieza().getPosicionConectorEnPanel(c, getPosicionPieza(c.getPieza()));
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

    private Stream<Conector> getAllConectoresStream() {
        return circuito.getComponentes().keySet().stream().map(Pieza::getConectores)
                       .flatMap(List::stream);

    }

    public List<Conector> getAllConectores() {
        return getAllConectoresStream().collect(Collectors.toList());
    }

    public List<Conector> getConectoresValidos(Conector conectorSeleccionado) {
        return getAllConectoresStream().filter(
                                               con -> !con.getTipoConector().equals(conectorSeleccionado.getTipoConector()))
                                       .collect(Collectors.toList());
    }

    public Conector getConectorByPosicion(Pieza pieza, Point posicion) {
        Iterator<Conector> it = pieza.getConectores().iterator();
        return getConectorByPosicion(it, posicion);
    }

    public Point getPosicionPieza(Pieza pieza) {
        return circuito.getPosicionPieza(pieza);
    }

    private boolean puntoDentroDeBounds(Point punto, Map.Entry<Pieza, Point> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds.contains(punto);
    }

    public Set<Map.Entry<Pieza, Point>> getPiezasPosicionEntrySet() {
        return circuito.getComponentes().entrySet();
    }

    public void generarPieza(String pathImagen, int ancho, int alto, List<Conector> conectores) {
        panelCircuito.addPiezaByDragging(new Pieza(circuito, pathImagen, ancho, alto, conectores));
    }

    public void generarResistor() {
        generarPieza("media/res.png", 200, 100, List.of(new Conector(0, 0.5, TipoConector.ENTRADA),
                new Conector(1, 0.5, TipoConector.SALIDA)));
    }

    public void generarAnd() {
        generarPieza("media/and.png", 200, 100, List.of(new Conector(0, 0.25, TipoConector.ENTRADA),
                new Conector(0, 0.75, TipoConector.ENTRADA),
                new Conector(1, 0.5, TipoConector.SALIDA)));
    }

    private Optional<Conexion> getConexionEnCursoOptional() {
        return circuito.getConexiones().stream().filter(Conexion::enCurso).findFirst();
    }

    private Conexion getConexionEnCurso() {
        Optional<Conexion> enCurso = getConexionEnCursoOptional();
        if (!enCurso.isPresent()) {
            throw new RuntimeException("No hay ninguna conexion en curso");
        }
        return enCurso.get();
    }

    public void finalizarConexion(Conector destino) {
        getConexionEnCurso().cerrar(destino);
    }


    public void iniciarConexion(Conector origen) {
        Conexion conexionEnCurso = new Conexion(origen);
        circuito.addConexion(conexionEnCurso);
    }

    public void addPointConexion(Point punto) {
        System.out.println("adding point");
        getConexionEnCurso().addPoint(punto);
        System.out.println(getConexionEnCurso());
    }

    public List<Conexion> getConexiones() {
        return circuito.getConexiones();
    }

    public void borrarConexionesIncompletas() {
        Optional<Conexion> enCurso = getConexionEnCursoOptional();
        enCurso.ifPresent(circuito::borrarConexion);
    }

    public void borrarConexion(Conexion clicada) {
        circuito.borrarConexion(clicada);
    }
}
