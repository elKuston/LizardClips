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
        seleccionado = getComponenteSeleccionado(e.getPoint());
        if (seleccionado == null) {
            System.out.println("clicking null");
        } else {
            System.out.println("clicking on object");
            dragging = true;
            grabPoint = new Dimension(e.getX() - seleccionado.getX(), e.getY() - seleccionado.getY());
            System.out.print(grabPoint);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            seleccionado.setBounds(new Rectangle((int) (e.getX() - grabPoint.getWidth()), (int) (e.getY() - grabPoint.getHeight()), (int) seleccionado.getTamano().getWidth(), (int) seleccionado.getTamano().getHeight()));
            componentes.put(seleccionado, seleccionado.getBounds().getLocation());
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Map.Entry<ImagePanel, Point> entry : componentes.entrySet()) {
            entry.getKey().getImagen().paintIcon((Component) this, g, (int) entry.getValue().getX(), (int) entry.getValue().getY());
            if (true) {//TODO boolean dibujarContornos
                Rectangle b = entry.getKey().getBounds();
                g.drawRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
            }
        }
        /**/
    }

    //region métodos overriden que no queremos para nada
    @Override
    public void mouseMoved(MouseEvent e) {
    }

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
