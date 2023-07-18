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
    private static float escala = 1f;
    @Transient
    private static final float escala_min = 0.1f, escala_max = 2f;

    //Coordenadas virtuales en un hipotético plano infinito
    private int x, y;

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

    public static void reescalar(float dZ) {
        escala = Math.min(escala_max,
                Math.max(escala_min, escala + dZ)); //Asegurar que escala está entre min y max
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
        x = (int) ((xPanel - referenciaX) / escala);
    }

    public int getY() {
        return (int) ((y * escala + referenciaY));
    }

    private void setYPanel(int yPanel) {
        y = (int) ((yPanel - referenciaY) / escala);
    }

    public int getXVirtual() {
        return x;
    }

    public int getYVirtual() {
        return y;
    }

    public Point getPoint() {
        return new Point(getX(), getY());
    }

    public void translate(int dx, int dy) {
        x += dx;
        y += dy;
    }

}
