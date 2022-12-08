package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImagePanel extends JPanel implements MouseListener, MouseMotionListener {
    private final ImageIcon imagen;
    private Point posicion, posicionPrevia;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        posicionPrevia = e.getPoint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        posicion.translate((int) (e.getX()-posicionPrevia.getX()), (int) (e.getY()-posicionPrevia.getY()));
        posicionPrevia.setLocation(e.getPoint());
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public ImagePanel(String pathImagen, Point posicion, int ancho, int alto){
        imagen = escalarImagen(pathImagen, ancho, alto);
        this.posicion = posicion;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private ImageIcon escalarImagen(String pathImagen, int ancho, int alto){
        return new ImageIcon(
                new ImageIcon(pathImagen)
                        .getImage()
                        .getScaledInstance(ancho, alto, Image.SCALE_FAST)
        );
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        System.out.println("pintando");
        imagen.paintIcon(this,graphics, (int) posicion.getX(), (int) posicion.getY());
    }


}
