package controlador;

import constant.TipoConector;
import db.CircuitoRepository;
import gui.PanelCircuito;
import gui.VentanaPrincipal;
import lombok.Setter;
import modelo.Circuito;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;
import utils.Punto;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControladorCircuito {
    private final PanelCircuito panelCircuito;
    private final CircuitoRepository circuitoRepository;
    private Circuito circuito;

    @Setter
    private VentanaPrincipal ventanaPrincipal;

    public ControladorCircuito(Circuito circuito, PanelCircuito panelCircuito) {
        this.panelCircuito = panelCircuito;
        panelCircuito.setControladorCircuito(this);
        setCircuito(circuito);
        circuitoRepository = new CircuitoRepository();
    }


    private void setCircuito(Circuito circuito) {
        if (this.circuito != null) {
            for (Pieza p : circuito.getComponentes()) {
                p.setCircuito(circuito);
            }
        }
        this.circuito = circuito;
        this.circuito.setControlador(this);

    }

    public void colocarPieza(Pieza pieza, Punto posicion) {
        circuito.colocarPieza(pieza, posicion);
    }

    public void borrarPieza(Pieza pieza) {
        circuito.borrarPieza(pieza);
    }

    public void arrastrarPieza(Pieza pieza, Punto posicion, Dimension grabPoint) {
        Punto posicionReal = posicion;
        posicionReal.translate((int) -grabPoint.getWidth(), (int) -grabPoint.getHeight());
        if (dentroDelPanel(posicionReal, pieza.getTamano())) {
            circuito.moverPieza(pieza, posicionReal);
        }
    }

    private boolean dentroDelPanel(Punto posicion, Dimension tamanoPieza) {
        Rectangle tamanoPanel = panelCircuito.getBounds();
        tamanoPanel.setLocation(0,
                0);//Si no, se tiene un offset igual al tamaño de los componentes a la izquierda en X y arriba en Y
        Punto esquinaInferiorDerecha = new Punto((int) (posicion.getX() + tamanoPieza.getWidth()),
                (int) (posicion.getY() + tamanoPieza.getHeight()));
        return tamanoPanel.contains(posicion.getPoint()) &&
                tamanoPanel.contains(esquinaInferiorDerecha.getPoint());
    }

    public Pieza getPiezaByPosicion(Punto posicionRaton) {
        return circuito.getComponentes().stream()
                       .filter(p -> p.getBounds().contains(posicionRaton.getPoint())).findFirst()
                       .orElse(null);
    }

    public Conector getConectorByPosicion(Punto posicion) {
        return getConectorByPosicion(getAllConectoresStream().iterator(), posicion);
    }

    public List<Pieza> getPiezas() {
        return circuito.getComponentes();
    }

    private Conector getConectorByPosicion(Iterator<Conector> candidatos, Punto posicion) {
        Conector conector = null;
        while (conector == null && candidatos.hasNext()) {
            Conector c = candidatos.next();
            Punto posicionConector =
                    c.getPieza().getPosicionConectorEnPanel(c, c.getPieza().getPosicion());
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
        return circuito.getComponentes().stream().map(Pieza::getConectores).flatMap(List::stream);

    }

    public List<Conector> getAllConectores() {
        return getAllConectoresStream().collect(Collectors.toList());
    }

    public List<Conector> getConectoresValidos(Conector conectorSeleccionado) {
        return getAllConectoresStream().filter(
                                               con -> !con.getTipoConector().equals(conectorSeleccionado.getTipoConector()))
                                       .collect(Collectors.toList());
    }

    public Conector getConectorByPosicion(Pieza pieza, Punto posicion) {
        Iterator<Conector> it = pieza.getConectores().iterator();
        return getConectorByPosicion(it, posicion);
    }

    private boolean puntoDentroDeBounds(Punto punto, Map.Entry<Pieza, Punto> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds.contains(punto.getPoint());
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

    public void addPointConexion(Punto punto) {
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

    public void guardar() {
        String nombre;
        if (circuito.getNombre() == null || circuito.getNombre().isBlank()) {
            nombre = JOptionPane.showInputDialog("Guardar circuito como: ");
            circuito.setNombre(nombre);
        }
        if (circuito.getNombre() != null &&
                !circuito.getNombre().isBlank()) { //No guardar si se hace cancel

            circuitoRepository.guardar(circuito);
        }
    }

    public void cargar() {
        System.out.println("Cargando circuito");
        List<Circuito> circuitos = circuitoRepository.getAll();
        String[] nombresCircuitos =
                circuitos.stream().map(Circuito::getNombre).toList().toArray(new String[0]);
        String nombre = (String) JOptionPane.showInputDialog(ventanaPrincipal.getFrame(),
                "Selecciona tu circuito", "Cargar circuito", JOptionPane.PLAIN_MESSAGE, null,
                nombresCircuitos, // Array of choices
                nombresCircuitos[0]); // Initial choice
        Circuito seleccionado =
                circuitos.stream().filter(c -> c.getNombre().equals(nombre)).findFirst().get();
        setCircuito(seleccionado);
        panelCircuito.cambiarCircuito();
        ventanaPrincipal.setNombreCircuito(seleccionado.getNombre());
        System.out.println("cargado circuito" + circuito);
    }

    public BufferedImage generarThumbnail() {
        BufferedImage image = new BufferedImage(panelCircuito.getWidth(), panelCircuito.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        panelCircuito.printAll(g);
        g.dispose();
        try {
            ImageIO.write(image, "png", new File("thumbnail.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return image;
    }
}
