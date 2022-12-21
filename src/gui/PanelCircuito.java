package gui;

import constant.ModoPanel;
import constant.TipoConector;
import controlador.ControladorCircuito;
import lombok.Getter;
import modelo.Conector;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Map;

public class PanelCircuito extends JPanel implements MouseListener, MouseMotionListener {
    private ModoPanel modo;
    private Dimension grabPoint;
    private ModeloPieza piezaSeleccionada;
    private Conector conectorSeleccionado;
    @Getter
    private ControladorCircuito controladorCircuito;

    public PanelCircuito() {
        addMouseListener(this);
        addMouseMotionListener(this);
        modo = ModoPanel.MODO_NORMAL;
    }

    public void setControladorCircuito(ControladorCircuito controladorCircuito) {
        this.controladorCircuito = controladorCircuito;
        repaint();
    }

    public void addImagePanel(Point posicion, ModeloPieza modeloPieza) {
        controladorCircuito.colocarPieza(modeloPieza, posicion);
        repaint();
    }

    public void addImagePanelByDragging(ModeloPieza modeloPieza) {
        Point raton = MouseInfo.getPointerInfo().getLocation();
        raton.translate((int) -getLocationOnScreen().getX(), (int) -getLocationOnScreen().getY());
        addImagePanel(new Point((int) Math.max(0, raton.getX()), (int) Math.max(0, raton.getY())),
                modeloPieza);
        startDragging(modeloPieza,
                new Dimension(modeloPieza.getWidth() / 2, modeloPieza.getHeight() / 2));
    }

    public void toggleDeleteMode() {
        if (modo.equals(ModoPanel.MODO_BORRADO)) {
            modo = ModoPanel.MODO_NORMAL;
        } else {
            modo = ModoPanel.MODO_BORRADO;
        }
    }

    private void startDragging(ModeloPieza modeloPieza, Dimension grabPoint) {
        if (!modo.equals(ModoPanel.MODO_BORRADO)) {
            modo = ModoPanel.MODO_ARRASTRANDO;
            this.grabPoint = grabPoint;
        }
        this.piezaSeleccionada = modeloPieza;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Pressed");
        ModeloPieza modeloPiezaSeleccionado = controladorCircuito.getPiezaByPosicion(e.getPoint());
        if (modeloPiezaSeleccionado == null) {
            System.out.println("clicking null");
        } else {
            Conector conector = controladorCircuito.getConectorByPosicion(modeloPiezaSeleccionado,
                    e.getPoint());
            if (conector == null) {
                piezaSeleccionada(modeloPiezaSeleccionado, e);
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

    private void piezaSeleccionada(ModeloPieza modeloPiezaSeleccionado, MouseEvent e) {
        System.out.println("clicking on object");
        if (modo.equals(ModoPanel.MODO_BORRADO)) {
            borrarComponente(modeloPiezaSeleccionado);
        } else {
            Dimension grabPoint = new Dimension((int) (e.getX() -
                    controladorCircuito.getPosicionPieza(modeloPiezaSeleccionado).getX()),
                    (int) (e.getY() -
                            controladorCircuito.getPosicionPieza(modeloPiezaSeleccionado).getY()));
            startDragging(modeloPiezaSeleccionado, grabPoint);
        }

    }


    private void borrarComponente(ModeloPieza componente) {
        System.out.println("Borrando");
        controladorCircuito.borrarPieza(componente);
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
            controladorCircuito.arrastrarPieza(piezaSeleccionada, e.getPoint(), grabPoint);
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseDragged(e);
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
        if (controladorCircuito != null) {
            for (Map.Entry<ModeloPieza, Point> entry : controladorCircuito.getPiezasPosicionEntrySet()) {
                entry.getKey().dibujar(this, g, entry.getValue(), true, coloresConectores);
            }
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
