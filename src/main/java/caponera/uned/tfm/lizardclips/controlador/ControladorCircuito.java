package caponera.uned.tfm.lizardclips.controlador;

import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.db.CircuitoRepository;
import caponera.uned.tfm.lizardclips.gui.PanelCircuito;
import caponera.uned.tfm.lizardclips.gui.VentanaPrincipal;
import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.modelo.Circuito;
import caponera.uned.tfm.lizardclips.modelo.Conector;
import caponera.uned.tfm.lizardclips.modelo.Conexion;
import caponera.uned.tfm.lizardclips.modelo.Pieza;
import caponera.uned.tfm.lizardclips.utils.Punto;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
        //Solo puede haber una conexion por conector de entrada
        List<Conector> conectoresEntradaOcupados =
                circuito.getConexiones().stream().filter(Conexion::isComplete)
                        .flatMap(conexion -> Stream.of(conexion.getOrigen(), conexion.getDestino()))
                        .filter(conector -> conector.getTipoConector().equals(TipoConector.ENTRADA))
                        .toList();
        if (conectoresEntradaOcupados.contains(conectorSeleccionado)) {
            return new ArrayList<>();// Si se selecciona un conector de entrada que y está ocupado no se puede conectar a ningún sitio
        }
        return getAllConectoresStream().filter(
                con -> !con.getPieza().equals(conectorSeleccionado.getPieza()) &&
                        !con.getTipoConector().equals(conectorSeleccionado.getTipoConector()) &&
                        !conectoresEntradaOcupados.contains(con)).collect(Collectors.toList());
    }

    public Conector getConectorByPosicion(Pieza pieza, Punto posicion) {
        Iterator<Conector> it = pieza.getConectores().iterator();
        return getConectorByPosicion(it, posicion);
    }

    private boolean puntoDentroDeBounds(Punto punto, Map.Entry<Pieza, Punto> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds.contains(punto.getPoint());
    }

    public void generarPieza(TipoPieza tipoPieza, int nConectoresEntrada) {
        panelCircuito.addPiezaByDragging(new Pieza(circuito, tipoPieza, nConectoresEntrada));
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
        Punto.resetReferencia();
    }

    public void nuevoCircuito() {
        setCircuito(new Circuito());
        panelCircuito.cambiarCircuito();
        ventanaPrincipal.setNombreCircuito("Circuito sin nombre");
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

    public void exportarCodigo() {
        String codigoModelica = ModelicaGenerator.generarCodigoModelica(circuito);
        System.out.println(codigoModelica);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar código Modelica como");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".mo");
            }

            @Override
            public String getDescription() {
                return "Modelica files";
            }
        });

        int userSelection = fileChooser.showSaveDialog(ventanaPrincipal.getFrame());

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase(Locale.ROOT).endsWith(".mo")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".mo");
            }
            try {
                FileWriter fw = new FileWriter(fileToSave);
                fw.write(codigoModelica);
                fw.close();
                int result = JOptionPane.showConfirmDialog(ventanaPrincipal.getFrame(),
                        "¿Quieres abrir ahora el fichero generado?", "Abrir fichero",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(fileToSave);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addConectorToPieza(Pieza p) {
        p.addConectorEntrada();
        panelCircuito.repaint();
    }

    public void removeConectorFromPieza(Pieza p) {
        p.removeConectorEntrada();
        panelCircuito.repaint();
    }

    public void cancelarConexion() {
        circuito.cancelarConexion();
    }
}
