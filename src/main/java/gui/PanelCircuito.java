package gui;

import constant.ModoPanel;
import controlador.ControladorCircuito;
import lombok.Getter;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;
import utils.LineUtils;
import utils.Punto;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
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
    @Getter
    private ControladorCircuito controladorCircuito;

    public PanelCircuito() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setModo(ModoPanel.MODO_NORMAL);
    }

    public void cambiarCircuito() {
        modo = ModoPanel.MODO_NORMAL;
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
        /*System.out.println("Cicked on: " + e.getPoint() + "; piezas at: " +
                controladorCircuito.getPiezasPosicionEntrySet().stream()
                                   .map(entry -> entry.getValue() + "(" +
                                           entry.getKey().getBounds() + "); " +
                                           controladorCircuito.getPosicionPieza(entry.getKey()))
                                   .toList());*/
        Pieza piezaSeleccionado = controladorCircuito.getPiezaByPosicion(new Punto(e.getPoint()));
        if (piezaSeleccionado == null) {
            if (isModo(ModoPanel.MODO_BORRADO)) {
                Conexion clicada = detectarClickLineas(e.getPoint());
                if (clicada != null) {
                    controladorCircuito.borrarConexion(clicada);
                    repaint();
                }
            } else if (isModo(ModoPanel.MODO_CONEXION)) {
                controladorCircuito.addPointConexion(new Punto(e.getPoint()));
                repaint();
            }
        } else {
            Conector conector = controladorCircuito.getConectorByPosicion(piezaSeleccionado,
                    new Punto(e.getPoint()));
            if (conector == null) {
                piezaSeleccionada(piezaSeleccionado, e);
            } else {
                conectorSeleccionado(conector);
            }
        }
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
        if (isModo(ModoPanel.MODO_ARRASTRANDO)) {
            modo = ModoPanel.MODO_NORMAL;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (isModo(ModoPanel.MODO_ARRASTRANDO)) {
            controladorCircuito.arrastrarPieza(piezaSeleccionada, new Punto(e.getPoint()),
                    grabPoint);
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Map<Conector, Color> coloresConectores = null;

        //Dibujar piezas
        if (isModo(ModoPanel.MODO_CONEXION)) {
            coloresConectores = controladorCircuito.getAllConectores().stream().collect(
                    Collectors.toMap(con -> con,
                            con -> controladorCircuito.getConectoresValidos(conectorSeleccionado)
                                                      .contains(con) ? Color.BLUE : Color.GRAY));
        }
        //for (Map.Entry<Pieza, Punto> entry : controladorCircuito.getPiezasPosicionEntrySet()) {
        for (Pieza p : controladorCircuito.getPiezas()) {
            p.dibujar(this, g, p.getPosicion(), true, coloresConectores);
        }

        //Dibujar conexiones
        g.setColor(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        for (Conexion c : controladorCircuito.getConexiones()) {
            List<Punto> puntos = new ArrayList<>(c.getPuntos());
            if (isModo(ModoPanel.MODO_CONEXION) && c.enCurso()) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    //endregion
}
