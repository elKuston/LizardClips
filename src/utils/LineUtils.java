package utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class LineUtils {
    public static List<Point> getPuntosManhattan(List<Point> puntos) {
        List<Point> puntosManhattan = new ArrayList<>();
        if (puntos.size() < 2) {
            puntosManhattan = puntos;
        } else {
            puntosManhattan.add(puntos.get(0));
            for (Point[] pareja : getParejas(puntos)) {
                Point intermedio = manhattan(pareja[0], pareja[1]);
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
    private static Point manhattan(Point p1, Point p2) {
        Point intermedio = null;
        if (p1.getX() != p2.getX() && p1.getY() != p2.getY()) {
            //primero el eje más largo
            if (Math.abs(p1.getX() - p2.getX()) >= Math.abs(p1.getY() - p2.getY())) {
                intermedio = new Point(p2.x, p1.y);
            } else {
                intermedio = new Point(p1.x, p2.y);
            }
        }

        return intermedio;
    }

    public static List<Point[]> getParejas(List<Point> puntos) {
        List<Point[]> parejas = new ArrayList<>();
        Point p1 = puntos.get(0);
        for (int i = 1; i < puntos.size(); i++) {
            Point p2 = puntos.get(i);
            parejas.add(new Point[]{p1, p2});
            p1 = p2;
        }

        return parejas;
    }
}
