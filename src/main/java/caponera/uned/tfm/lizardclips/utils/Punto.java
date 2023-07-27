package caponera.uned.tfm.lizardclips.utils;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.awt.Point;

@Embeddable
@NoArgsConstructor
public class Punto {
    //Coordenadas de referencia en las que se sitúa el panel
    @Transient
    private static int referenciaX = 0, referenciaY = 0;
    @Transient
    @Getter
    private static double escala = 1f;
    @Transient
    private static final float ESCALA_MIN = 0.1f, ESCALA_MAX = 2f;

    //Coordenadas virtuales en un hipotético plano infinito
    private double x, y;

    public Punto(Point p) {
        this(p.x, p.y);
    }

    public Punto(Punto otro) {
        this.x = otro.x;
        this.y = otro.y;
    }

    public Punto(int x, int y) {
        setXPanel(x);
        setYPanel(y);
    }

    public static Punto puntoCoordenadasVirtuales(double x, double y) {
        Punto p = new Punto(0, 0);
        p.setXVirtual(x);
        p.setYVirtual(y);
        return p;
    }

    public static void reescalar(float dZ) {
        escala = Math.min(ESCALA_MAX,
                Math.max(ESCALA_MIN, escala + dZ)); //Asegurar que escala está entre min y max
        System.out.println("Nueva escala: " + escala);
    }

    public static void desplazarReferencia(int dX, int dY) {
        referenciaX += dX;
        referenciaY += dY;
    }

    public static void resetReferencia() {
        referenciaX = 0;
        referenciaY = 0;
    }

    public int getX() {
        return (int) (x * escala + referenciaX);
    }

    private void setXPanel(int xPanel) {
        x = (xPanel - referenciaX) / escala;
    }

    public int getY() {
        return (int) ((y * escala + referenciaY));
    }

    private void setYPanel(int yPanel) {
        y = (yPanel - referenciaY) / escala;
    }

    public int getXVirtual() {
        return (int) x;
    }

    public int getYVirtual() {
        return (int) y;
    }

    public void setXVirtual(double x) {
        this.x = x;
    }

    public void setYVirtual(double y) {
        this.y = y;
    }

    public Point getPoint() {
        return new Point(getX(), getY());
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

}
