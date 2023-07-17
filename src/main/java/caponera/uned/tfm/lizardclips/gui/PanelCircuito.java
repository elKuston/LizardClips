package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.constant.ModoPanel;
import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import caponera.uned.tfm.lizardclips.modelo.Conector;
import caponera.uned.tfm.lizardclips.modelo.Conexion;
import caponera.uned.tfm.lizardclips.modelo.Pieza;
import caponera.uned.tfm.lizardclips.utils.I18NUtils;
import caponera.uned.tfm.lizardclips.utils.LineUtils;
import caponera.uned.tfm.lizardclips.utils.Punto;
import lombok.Getter;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PanelCircuito extends JPanel implements MouseListener, MouseMotionListener {
    private ModoPanel modo;
    private Dimension grabPoint;
    private Pieza piezaSeleccionada;
    private Conector conectorSeleccionado;
    private Point grabPointDesplazamiento;
    @Getter
    private ControladorCircuito controladorCircuito;

    public PanelCircuito() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setModo(ModoPanel.MODO_NORMAL);
        setupEscKey();
        setBackground(Color.WHITE);
    }

    private void setupEscKey() {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "LizardClips:canelConexion");
        getActionMap().put("LizardClips:canelConexion", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isModo(ModoPanel.MODO_CONEXION)) {
                    controladorCircuito.cancelarConexion();
                    setModo(ModoPanel.MODO_NORMAL);
                }
            }
        });
    }

    public void cambiarCircuito() {
        setModo(ModoPanel.MODO_NORMAL);
        grabPoint = null;
        piezaSeleccionada = null;
        conectorSeleccionado = null;
        repaint();
    }

    public void setControladorCircuito(ControladorCircuito controladorCircuito) {
        this.controladorCircuito = controladorCircuito;
        repaint();
    }

    public void addPieza(Punto posicion, Pieza pieza) {
        controladorCircuito.colocarPieza(pieza, posicion);
        repaint();
    }

    public void addPiezaByDragging(Pieza pieza) {
        Point raton = MouseInfo.getPointerInfo().getLocation();
        raton.translate((int) -getLocationOnScreen().getX(), (int) -getLocationOnScreen().getY());
        addPieza(new Punto((int) Math.max(0, raton.getX()), (int) Math.max(0, raton.getY())),
                pieza);
        startDragging(pieza, new Dimension(pieza.getWidth() / 2, pieza.getHeight() / 2));
    }

    public void toggleDeleteMode() {
        if (isModo(ModoPanel.MODO_BORRADO)) {
            setModo(ModoPanel.MODO_NORMAL);
        } else {
            setModo(ModoPanel.MODO_BORRADO);
        }
    }

    private void startDragging(Pieza pieza, Dimension grabPoint) {
        if (!isModo(ModoPanel.MODO_BORRADO)) {
            setModo(ModoPanel.MODO_ARRASTRANDO);
            this.grabPoint = grabPoint;
        }
        this.piezaSeleccionada = pieza;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isMiddleMouseButton(e)) {
            setModo(ModoPanel.MODO_DESPLAZANDO);
            grabPointDesplazamiento = e.getPoint();
        } else {
            Pieza piezaSeleccionado =
                    controladorCircuito.getPiezaByPosicion(new Punto(e.getPoint()));
            if (piezaSeleccionado == null) {//No ha hecho click sobre una pieza ni conector
                if (isModo(ModoPanel.MODO_BORRADO)) {
                    Conexion clicada = detectarClickLineas(e.getPoint());
                    if (clicada != null) { //Ha pulsado una conexion
                        controladorCircuito.borrarConexion(clicada);
                        repaint();
                    }
                } else if (isModo(ModoPanel.MODO_CONEXION)) {
                    controladorCircuito.addPointConexion(new Punto(e.getPoint()));
                    repaint();
                }
            } else { //Ha pulsado una pieza o un conector
                Conector conector = controladorCircuito.getConectorByPosicion(piezaSeleccionado,
                        new Punto(e.getPoint()));
                if (conector == null) { //Ha pulsado una pieza
                    if (SwingUtilities.isRightMouseButton(e)) {//Click derecho sobre la pieza
                        clickDerechoPieza(piezaSeleccionado, e);
                    } else {//Click normal sobre la pieza
                        piezaSeleccionada(piezaSeleccionado, e);
                    }
                } else {//Ha pulsado un conector
                    conectorSeleccionado(conector);
                }
            }
        }
    }

    private void clickDerechoPieza(Pieza piezaSeleccionado, MouseEvent e) {
        JPopupMenu menuPieza = new JPopupMenu("Modificar pieza");
        JMenuItem anydir = new JMenuItem(I18NUtils.getString("add_input"));
        anydir.addActionListener(
                click -> controladorCircuito.addConectorToPieza(piezaSeleccionado));
        menuPieza.add(anydir);

        JMenuItem eliminar = new JMenuItem(I18NUtils.getString("remove_input"));
        eliminar.addActionListener(
                click -> controladorCircuito.removeConectorFromPieza(piezaSeleccionado));
        menuPieza.add(eliminar);

        JMenuItem rotarDerecha = new JMenuItem(I18NUtils.getString("rotate_right"));
        rotarDerecha.addActionListener(
                click -> controladorCircuito.rotarPieza(piezaSeleccionado, true));
        menuPieza.add(rotarDerecha);

        JMenuItem rotarIzquierda = new JMenuItem(I18NUtils.getString("rotate_left"));
        rotarIzquierda.addActionListener(
                click -> controladorCircuito.rotarPieza(piezaSeleccionado, false));
        menuPieza.add(rotarIzquierda);
        menuPieza.show(this, e.getX(), e.getY());
    }

    private Conexion detectarClickLineas(Point point) {
        return controladorCircuito.getConexiones().stream()
                                  .filter(con -> LineUtils.getParejas(con.getPuntosManhattan())
                                                          .stream().anyMatch(
                                                  pareja -> new Line2D.Double(pareja[0].getPoint(),
                                                          pareja[1].getPoint()).intersects(
                                                          new Rectangle(point,
                                                                  new Dimension(10, 10)))))
                                  .findFirst().orElse(null);
    }

    private void conectorSeleccionado(Conector conector) {
        if (isModo(ModoPanel.MODO_CONEXION)) {
            if (controladorCircuito.getConectoresValidos(conectorSeleccionado).contains(conector)) {
                controladorCircuito.finalizarConexion(conector);
                setModo(ModoPanel.MODO_NORMAL);
            } else {
                throw new RuntimeException(I18NUtils.getString("invalid-connection"));
            }
        } else {
            this.conectorSeleccionado = conector;
            controladorCircuito.iniciarConexion(conector);
            setModo(ModoPanel.MODO_CONEXION);
        }

        repaint();
    }

    private void piezaSeleccionada(Pieza piezaSeleccionado, MouseEvent e) {
        if (isModo(ModoPanel.MODO_BORRADO)) {
            borrarComponente(piezaSeleccionado);
        } else {
            Dimension grabPoint =
                    new Dimension((int) (e.getX() - piezaSeleccionado.getPosicion().getX()),
                            (int) (e.getY() - piezaSeleccionado.getPosicion().getY()));
            startDragging(piezaSeleccionado, grabPoint);
        }

    }


    private void borrarComponente(Pieza componente) {
        System.out.println("Borrando");
        controladorCircuito.borrarPieza(componente);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isModo(ModoPanel.MODO_ARRASTRANDO) || isModo(ModoPanel.MODO_DESPLAZANDO)) {
            setModo(ModoPanel.MODO_NORMAL);
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (isModo(ModoPanel.MODO_ARRASTRANDO)) {
            controladorCircuito.arrastrarPieza(piezaSeleccionada, new Punto(e.getPoint()),
                    grabPoint);
            repaint();
        } else if (isModo(ModoPanel.MODO_DESPLAZANDO)) {
            Punto.desplazarReferencia((int) (e.getX() - grabPointDesplazamiento.getX()),
                    (int) (e.getY() - grabPointDesplazamiento.getY()));
            grabPointDesplazamiento = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseDragged(e);
        if (isModo(ModoPanel.MODO_CONEXION)) {
            repaint();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            Pieza pieza = controladorCircuito.getPiezaByPosicion(new Punto(e.getPoint()));
            if (pieza != null) {
                /*String nombreOriginal = pieza.getNombrePieza() != null ? pieza.getNombrePieza() :
                        ModelicaGenerator.nombrePieza(pieza);
                String nuevoNombre =
                        JOptionPane.showInputDialog(I18NUtils.getString("rename_component_prompt"),
                                nombreOriginal);
                System.out.println(nuevoNombre);
                if (nuevoNombre != null && !nuevoNombre.isEmpty() &&
                        !nuevoNombre.equals(nombreOriginal)) {
                    controladorCircuito.renombrarPieza(pieza, nuevoNombre);
                }*/
                EditorPropiedadesPieza epp = new EditorPropiedadesPieza(pieza);
                String[] opciones =
                        {I18NUtils.getString("apply_changes"), I18NUtils.getString("cancel")};
                int res = JOptionPane.showOptionDialog(null, epp,
                        I18NUtils.getString("edit_component"), JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
                if (res == 0) {//Aceptar cambios
                    epp.actualizarValorPropiedades();
                }
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Map<Conector, Color> coloresConectores = null;

        //Dibujar piezas
        if (isModo(ModoPanel.MODO_CONEXION)) {
            coloresConectores = controladorCircuito.getAllConectores().stream().collect(
                    Collectors.toMap(con -> con,
                            con -> controladorCircuito.getConectoresValidos(conectorSeleccionado)
                                                      .contains(con) ? Conector.colorConector :
                                    Color.GRAY));
        }
        //for (Map.Entry<Pieza, Punto> entry : controladorCircuito.getPiezasPosicionEntrySet()) {
        for (Pieza p : controladorCircuito.getPiezas()) {
            p.dibujar(this, g, p.getPosicion(), false, coloresConectores);
        }

        //Dibujar conexiones
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        for (Conexion c : controladorCircuito.getConexiones()) {
            List<Punto> puntos = new ArrayList<>(c.getPuntos());
            if (isModo(ModoPanel.MODO_CONEXION) && c.enCurso() && getMousePosition() != null) {
                puntos.add(new Punto(getMousePosition()));
            }
            List<Punto> puntosManhattan = LineUtils.getPuntosManhattan(puntos);
            int[] x = puntosManhattan.stream().mapToInt(pt -> (int) pt.getX()).toArray();
            int[] y = puntosManhattan.stream().mapToInt(pt -> (int) pt.getY()).toArray();
            g2.setStroke(new BasicStroke(2));
            g2.drawPolyline(x, y, x.length);
        }
    }

    private void setModo(ModoPanel nuevoModo) {
        if (modo != null && isModo(ModoPanel.MODO_CONEXION)) {
            //Al salir del modo conexion borramos las conexiones incompletas
            controladorCircuito.borrarConexionesIncompletas();
        }
        this.modo = nuevoModo;
        repaint();
    }

    private boolean isModo(ModoPanel target) {
        return this.modo.equals(target);
    }

    //region métodos overriden que no queremos para nada
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    //endregion
}
