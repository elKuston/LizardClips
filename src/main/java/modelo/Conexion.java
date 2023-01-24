package modelo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.ToString;
import utils.LineUtils;
import utils.Punto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Conexion implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Conector origen;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Conector destino;
    @Transient
    @ToString.Exclude
    private List<Punto> puntosIntermedios;

    @ToString.Exclude
    private List<Integer> puntosIntermediosX;
    @ToString.Exclude
    private List<Integer> puntosIntermediosY;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConexion;


    public Conexion(Conector origen) {
        this.origen = origen;
        this.puntosIntermedios = new ArrayList<>();
    }

    public Conexion() {
        this.puntosIntermedios = new ArrayList<>();
    }

    @PostLoad
    protected void postLoad() {
        //Cargar los puntos intermedios
        if (puntosIntermediosX != null && puntosIntermediosY != null) {
            for (int i = 0; i < puntosIntermediosX.size(); i++) {
                puntosIntermedios.add(
                        new Punto(puntosIntermediosX.get(i), puntosIntermediosY.get(i)));
            }
        }
    }

    @PreUpdate
    @PrePersist
    protected void preUpdatePersist() {
        this.puntosIntermediosX = puntosIntermedios.stream().map(Punto::getX).toList();
        this.puntosIntermediosY = puntosIntermedios.stream().map(Punto::getY).toList();
    }

    public void addPoint(Punto punto) {
        puntosIntermedios.add(punto);
    }

    public void cerrar(Conector destino) {
        this.destino = destino;
    }

    public List<Punto> getPuntos() {
        List<Punto> puntos = new ArrayList<>(List.of(origen.getPosicionEnPanel()));
        puntos.addAll(puntosIntermedios);
        if (isComplete()) {
            puntos.add(destino.getPosicionEnPanel());
        }
        return puntos;
    }

    public List<Punto> getPuntosManhattan() {
        return LineUtils.getPuntosManhattan(getPuntos());
    }

    public boolean isComplete() {
        return destino != null;
    }

    public boolean enCurso() {
        return !isComplete();
    }

    public String toString() {
        return getPuntos().toString();
    }
}
