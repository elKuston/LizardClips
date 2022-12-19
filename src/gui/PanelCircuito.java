package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

public class PanelCircuito extends JPanel implements MouseListener, MouseMotionListener {
    private Map<ImagePanel, Point> componentes;
    private boolean dragging;
    private Dimension grabPoint;
    private ImagePanel seleccionado;

    public PanelCircuito() {
        componentes = new HashMap<>();
        addMouseListener(this);
        addMouseMotionListener(this);
        //setBackground(Color.red);
    }

    public void addImagePanel(Point posicion, ImagePanel imagePanel) {
        //this.add(imagePanel);
        Dimension tamano = imagePanel.getTamano();
        imagePanel.setBounds(new Rectangle((int) posicion.getX(), (int) posicion.getY(), tamano.width, tamano.height));
        Rectangle b = imagePanel.getBounds();
        componentes.put(imagePanel, posicion);
        repaint();
    }

    public void addImagePanelByDragging(ImagePanel imagePanel) {
        Point raton = MouseInfo.getPointerInfo().getLocation();
        raton.translate((int) -getLocationOnScreen().getX(), (int) -getLocationOnScreen().getY());
        addImagePanel(new Point((int) Math.max(0, raton.getX()), (int) Math.max(0, raton.getY())), imagePanel);
        startDragging(imagePanel, new Dimension(imagePanel.getWidth() / 2, imagePanel.getHeight() / 2));
    }

    private void startDragging(ImagePanel imagePanel, Dimension grabPoint) {
        dragging = true;
        seleccionado = imagePanel;
        this.grabPoint = grabPoint;
    }

    private ImagePanel getComponenteSeleccionado(Point raton) {
        return componentes.entrySet().stream().filter((par -> puntoDentroDeCaja(raton, par))).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    private Rectangle calcularBoundingBox(Map.Entry<ImagePanel, Point> par) {
        Point posicion = par.getValue();
        Dimension dimensiones = par.getKey().getTamano();
        Rectangle caja = new Rectangle(dimensiones);
        caja.translate((int) posicion.getX(), (int) posicion.getY());
        return caja;
    }

    private boolean puntoDentroDeCaja(Point punto, Map.Entry<ImagePanel, Point> par) {
        Rectangle bounds = par.getKey().getBounds();
        return bounds
                .contains(punto);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Pressed");
        ImagePanel seleccionado = getComponenteSeleccionado(e.getPoint());
        if (seleccionado == null) {
            System.out.println("clicking null");
        } else {
            System.out.println("clicking on object");
            Dimension grabPoint = new Dimension(e.getX() - seleccionado.getX(), e.getY() - seleccionado.getY());
            startDragging(seleccionado, grabPoint);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            System.out.println("Dragging");
            Rectangle nuevoContorno = new Rectangle((int) (e.getX() - grabPoint.getWidth()), (int) (e.getY() - grabPoint.getHeight()), (int) seleccionado.getTamano().getWidth(), (int) seleccionado.getTamano().getHeight());
            if (dentroDelPanel(nuevoContorno)) {
                seleccionado.setBounds(nuevoContorno);
                componentes.put(seleccionado, nuevoContorno.getLocation());
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
        tamanoPanel.setLocation(0, 0);//Si no, se tiene un offset igual al tamaño de los componentes a la izquierda en X y arriba en Y

        return tamanoPanel.contains(boundsComponente.getLocation()) && tamanoPanel.contains(new Point((int) boundsComponente.getMaxX(), (int) boundsComponente.getMaxY()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Map.Entry<ImagePanel, Point> entry : componentes.entrySet()) {
            entry.getKey().dibujar(g, entry.getValue(), true);
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
