package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public abstract class PanelDragAndDrop extends JComponent implements MouseListener, MouseMotionListener {
    protected Point posicion;
    private Point posicionPrevia;
    private boolean dragging;
    private boolean dibujarContorno;

    public PanelDragAndDrop(Point posicionInicial, boolean dibujarContorno) {
        this.posicion = posicionInicial;
        this.dibujarContorno = dibujarContorno;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    abstract int getTamanoX();

    abstract int getTamanoY();

    private Rectangle getLimites() {
        return new Rectangle((int) posicion.getX(), (int) posicion.getY(), getTamanoX(), getTamanoY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Pressed");
        System.out.println(getBounds());
        if (getLimites().contains(e.getPoint())) {
            dragging = true;
            posicionPrevia = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            posicion.translate((int) (e.getX() - posicionPrevia.getX()), (int) (e.getY() - posicionPrevia.getY()));
            posicionPrevia = e.getPoint();
            setBounds(getLimites());
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (dibujarContorno) {
            Rectangle limites = getLimites();
            g.drawRect((int) limites.getX(), (int) limites.getY(), (int) limites.getWidth(), (int) limites.getHeight());
        }
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
