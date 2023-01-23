package utils;

import java.util.ArrayList;
import java.util.List;

public class LineUtils {
    public static List<Punto> getPuntosManhattan(List<Punto> puntos) {
        List<Punto> puntosManhattan = new ArrayList<>();
        if (puntos.size() < 2) {
            puntosManhattan = puntos;
        } else {
            puntosManhattan.add(puntos.get(0));
            for (Punto[] pareja : getParejas(puntos)) {
                Punto intermedio = manhattan(pareja[0], pareja[1]);
                if (intermedio != null) {
                    puntosManhattan.add(intermedio);
                }
                puntosManhattan.add(pareja[1]);
            }
        }

        return puntosManhattan;
    }

    /**
     * Calcula el punto intermedio para conectar p1 y p2 rectangularmente
     *
     * @param p1 primer punto a conectar
     * @param p2 segundo punto a conectar
     * @return {@code null} si los puntos ya se puden conectar rectangularmente. Si no, el punto
     * intermedio para conectarlos.
     */
    private static Punto manhattan(Punto p1, Punto p2) {
        Punto intermedio = null;
        if (p1.getX() != p2.getX() && p1.getY() != p2.getY()) {
            //primero el eje más largo
            if (Math.abs(p1.getX() - p2.getX()) >= Math.abs(p1.getY() - p2.getY())) {
                intermedio = new Punto(p2.getX(), p1.getY());
            } else {
                intermedio = new Punto(p1.getX(), p2.getY());
            }
        }

        return intermedio;
    }

    public static List<Punto[]> getParejas(List<Punto> puntos) {
        List<Punto[]> parejas = new ArrayList<>();
        Punto p1 = puntos.get(0);
        for (int i = 1; i < puntos.size(); i++) {
            Punto p2 = puntos.get(i);
            parejas.add(new Punto[]{p1, p2});
            p1 = p2;
        }

        return parejas;
    }
}
