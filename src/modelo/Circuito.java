package modelo;

import lombok.Getter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Circuito {
    private Map<Pieza, Point> componentes;
    private List<Conexion> conexiones;

    public Circuito() {
        componentes = new HashMap<>();
        conexiones = new ArrayList<>();
    }

    public void moverPieza(Pieza pieza, Point posicion) {
        componentes.put(pieza, posicion);
    }

    public void anyadirPieza(Pieza pieza, Point posicion) {
        componentes.put(pieza, posicion);
    }

    public void borrarPieza(Pieza pieza) {
        borrarConexionesPieza(pieza);
        componentes.remove(pieza);
    }

    public void borrarConexionesPieza(Pieza pieza) {
        getConexiones().stream().filter(con -> con.getDestino().getPieza().equals(pieza) ||
                con.getOrigen().getPieza().equals(pieza)).forEach(this::borrarConexion);
    }


    public Point getPosicionPieza(Pieza pieza) {
        return getComponentes().get(pieza);
    }

    public void addConexion(Conexion c) {
        this.conexiones.add(c);
    }

    public void borrarConexion(Conexion c) {
        this.conexiones.remove(c);
    }

}
