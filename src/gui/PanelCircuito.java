package gui;

import componentes.Conector;
import constant.ModoPanel;
import constant.TipoConector;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PanelCircuito extends JPanel implements MouseListener, MouseMotionListener {
    private Map<PanelPieza, Point> componentes;
    private ModoPanel modo;
    private Dimension grabPoint;
    private PanelPieza piezaSeleccionada;
    private Conector conectorSeleccionado;

    public PanelCircuito() {
        componentes = new HashMap<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        modo = ModoPanel.MODO_NORMAL;
    }

    public void addImagePanel(Point posicion, PanelPieza panelPieza) {
        //this.add(imagePanel);
        Dimension tamano = panelPieza.getTamano();
        panelPieza.setBounds(
                new Rectangle((int) posicion.getX(), (int) posicion.getY(), tamano.width,
                        tamano.height));
        Rectangle b = panelPieza.getBounds();
        componentes.put(panelPieza, posicion);
        repaint();
    }

    public void addImagePanelByDragging(PanelPieza panelPieza) {
        Point raton = MouseInfo.getPointerInfo().getLocation();
        raton.translate((int) -getLocationOnScreen().getX(), (int) -getLocationOnScreen().getY());
        addImagePanel(new Point((int) Math.max(0, raton.getX()), (int) Math.max(0, raton.getY())),
                panelPieza);
        startDragging(panelPieza,
                new Dimension(panelPieza.getWidth() / 2, panelPieza.getHeight() / 2));
    }

    public void toggleDeleteMode() {
        if (modo.equals(ModoPanel.MODO_BORRADO)) {
            modo = ModoPanel.MODO_NORMAL;
        } else {
            modo = ModoPanel.MODO_BORRADO;
        }
    }

    private void startDragging(PanelPieza panelPieza, Dimension grabPoint) {
        if (!modo.equals(ModoPanel.MODO_BORRADO)) {
            modo = ModoPanel.MODO_ARRASTRANDO;
            this.grabPoint = grabPoint;
        }
        this.piezaSeleccionada = panelPieza;
    }

    private PanelPieza getPiezaSeleccionada(Point raton) {
        return componentes.entrySet().stream().filter((par -> puntoDentroDeCaja(raton, par))).map(
                Map.Entry::getKey).findFirst().orElse(null);
    }

    private Rectangle calcularBoundingBox(Map.Entry<PanelPieza, Point> par) {
        Point posicion = par.getValue();
        Dimension dimensiones = par.getKey().getTamano();
        Rectangle caja = new Rectangle(dimensiones);
        caja.translate((int) posicion.getX(), (int) posicion.getY());
        return caja;
    }

    private boolean puntoDentroDeCaja(Point punto, Map.Entry<PanelPieza, Point> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds
                .contains(punto);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Pressed");
        PanelPieza panelPiezaSeleccionado = getPiezaSeleccionada(e.getPoint());
        if (panelPiezaSeleccionado == null) {
            System.out.println("clicking null");
        } else {
            Conector conector = getConectorSelecionado(panelPiezaSeleccionado, e.getPoint());
            if (conector == null) {
                piezaSeleccionada(panelPiezaSeleccionado, e);
            } else {
                conectorSeleccionado(conector);
            }
        }
    }

    private void conectorSeleccionado(Conector conector) {
        this.conectorSeleccionado = conector;
        modo = ModoPanel.MODO_CONEXION;
        repaint();
    }

    private void piezaSeleccionada(PanelPieza panelPiezaSeleccionado, MouseEvent e) {
        System.out.println("clicking on object");
        if (modo.equals(ModoPanel.MODO_BORRADO)) {
            borrarComponente(panelPiezaSeleccionado);
        } else {
            Dimension grabPoint = new Dimension(e.getX() - panelPiezaSeleccionado.getX(),
                    e.getY() - panelPiezaSeleccionado.getY());
            startDragging(panelPiezaSeleccionado, grabPoint);
        }

    }

    private Conector getConectorSelecionado(PanelPieza panelPieza, Point raton) {
        Conector conector = null;
        Iterator<Conector> it = panelPieza.getConectores().iterator();
        while (conector == null && it.hasNext()) {
            Conector c = it.next();
            Point posicionConector =
                    panelPieza.calcularPosicionAbsolutaConector(c, componentes.get(panelPieza));
            double d = Math.sqrt(Math.pow(posicionConector.getX() - raton.getX(), 2) +
                    Math.pow(posicionConector.getY() - raton.getY(), 2));
            System.out.println(d);
            if (d <= Conector.RADIO) {
                System.out.println("clicking on conector");
                conector = c;
            }
        }
        return conector;
    }

    private void borrarComponente(PanelPieza componente) {
        System.out.println("Borrando");
        componentes.remove(componente);
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (modo.equals(ModoPanel.MODO_ARRASTRANDO)) {
            modo = ModoPanel.MODO_NORMAL;
        }
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (modo.equals(ModoPanel.MODO_ARRASTRANDO)) {
            Rectangle nuevoContorno = new Rectangle((int) (e.getX() - grabPoint.getWidth()),
                    (int) (e.getY() - grabPoint.getHeight()),
                    (int) piezaSeleccionada.getTamano().getWidth(),
                    (int) piezaSeleccionada.getTamano().getHeight());
            if (dentroDelPanel(nuevoContorno)) {
                piezaSeleccionada.setBounds(nuevoContorno);
                componentes.put(piezaSeleccionada, nuevoContorno.getLocation());
            }
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseDragged(e);
    }

    private boolean dentroDelPanel(Rectangle boundsComponente) {
        Rectangle tamanoPanel = this.getBounds();
        tamanoPanel.setLocation(0,
                0);//Si no, se tiene un offset igual al tamaño de los componentes a la izquierda en X y arriba en Y

        return tamanoPanel.contains(boundsComponente.getLocation()) && tamanoPanel.contains(
                new Point((int) boundsComponente.getMaxX(), (int) boundsComponente.getMaxY()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Map<TipoConector, Color> coloresConectores;
        coloresConectores =
                Map.of(TipoConector.ENTRADA, Color.BLUE, TipoConector.SALIDA, Color.GREEN);

        if (modo == ModoPanel.MODO_CONEXION) {
            if (conectorSeleccionado.getTipoConector().equals(TipoConector.SALIDA)) {
                coloresConectores =
                        Map.of(TipoConector.ENTRADA, Color.BLUE, TipoConector.SALIDA, Color.GRAY);
            } else {
                coloresConectores =
                        Map.of(TipoConector.ENTRADA, Color.GRAY, TipoConector.SALIDA, Color.BLUE);
            }
        }
        for (Map.Entry<PanelPieza, Point> entry : componentes.entrySet()) {
            entry.getKey().dibujar(g, entry.getValue(), true, coloresConectores);
        }
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
