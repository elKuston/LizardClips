package modelo;

import gui.ModeloPieza;
import lombok.Getter;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Circuito {
    @Getter
    private Map<ModeloPieza, Point> componentes;
    private Map<Conector, Conector> conexiones;

    public Circuito() {
        componentes = new HashMap<>();
        conexiones = new HashMap<>();
    }

    public void moverPieza(ModeloPieza pieza, Point posicion) {
        componentes.put(pieza, posicion);
    }

    public void anyadirPieza(ModeloPieza pieza, Point posicion) {
        componentes.put(pieza, posicion);
    }

    public void borrarPieza(ModeloPieza pieza) {
        componentes.remove(pieza);
    }


}
