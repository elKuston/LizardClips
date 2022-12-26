package modelo;

import lombok.Getter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Conexion {
    private Conector origen, destino;
    private List<Point> puntosIntermedios;


    public Conexion(Conector origen) {
        this.origen = origen;
        this.puntosIntermedios = new ArrayList<>();
    }

    public void addPoint(Point punto) {
        puntosIntermedios.add(punto);
    }

    public void cerrar(Conector destino) {
        this.destino = destino;
    }

    public List<Point> getPuntos() {
        List<Point> puntos = new ArrayList<>(List.of(origen.getPosicionEnPanel()));
        puntos.addAll(puntosIntermedios);
        if (isComplete()) {
            puntos.add(destino.getPosicionEnPanel());
        }
        return puntos;
    }

    public boolean isComplete() {
        return destino != null;
    }

    public boolean enCurso() {
        return !isComplete();
    }

    public String toString() {
        return puntosIntermedios.toString();
    }
}
