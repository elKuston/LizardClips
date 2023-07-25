package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.utils.LineUtils;
import caponera.uned.tfm.lizardclips.utils.Punto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Conexion implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_conector_origen")
    private Conector origen;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_conector_destino")
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
    private Integer idConexion;


    public Conexion(Conector origen) {
        this.origen = origen;
        this.puntosIntermedios = new ArrayList<>();
    }

    public Conexion(Conexion otra, List<Pieza> componentes) {
        this.puntosIntermedios =
                new ArrayList<>(otra.puntosIntermedios.stream().map(Punto::new).toList());
        this.puntosIntermediosX = new ArrayList<>();
        this.puntosIntermediosY = new ArrayList<>();

        List<Conector> conectoresPiezaOrigenOtra = otra.getOrigen().getPieza().getConectores();
        List<Conector> conectoresPiezaDestinoOtra = otra.getDestino().getPieza().getConectores();
        int indexConectorOrigen = conectoresPiezaOrigenOtra.indexOf(otra.getOrigen());
        int indexConectorDestino = conectoresPiezaDestinoOtra.indexOf(otra.getDestino());
        Pieza piezaOrigen = componentes.get(
                otra.getOrigen().getPieza().getCircuito().getComponentes()
                    .indexOf(otra.getOrigen().getPieza()));
        Pieza piezaDestino = componentes.get(
                otra.getDestino().getPieza().getCircuito().getComponentes()
                    .indexOf(otra.getDestino().getPieza()));
        this.origen = piezaOrigen.getConectores().get(indexConectorOrigen);
        this.destino = piezaDestino.getConectores().get(indexConectorDestino);
    }

    public Conexion() {
        this.puntosIntermedios = new ArrayList<>();
        puntosIntermediosX = new ArrayList<>();
        puntosIntermediosY = new ArrayList<>();
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

    public List<Integer> getPuntosIntermediosX() {
        return puntosIntermedios.stream().map(Punto::getX).toList();
    }

    public List<Integer> getPuntosIntermediosY() {
        return puntosIntermedios.stream().map(Punto::getY).toList();
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Conexion conexion = (Conexion) o;

        if (origen != null ? !origen.equals(conexion.origen) : conexion.origen != null)
            return false;
        if (destino != null ? !destino.equals(conexion.destino) : conexion.destino != null)
            return false;
        if (puntosIntermedios != null ? !puntosIntermedios.equals(conexion.puntosIntermedios) :
                conexion.puntosIntermedios != null)
            return false;
        if (puntosIntermediosX != null ? !puntosIntermediosX.equals(conexion.puntosIntermediosX) :
                conexion.puntosIntermediosX != null)
            return false;
        if (puntosIntermediosY != null ? !puntosIntermediosY.equals(conexion.puntosIntermediosY) :
                conexion.puntosIntermediosY != null)
            return false;
        return idConexion != null ? idConexion.equals(conexion.idConexion) :
                conexion.idConexion == null;
    }

    @Override
    public int hashCode() {
        int result = origen != null ? origen.hashCode() : 0;
        result = 31 * result + (destino != null ? destino.hashCode() : 0);
        result = 31 * result + (puntosIntermedios != null ? puntosIntermedios.hashCode() : 0);
        result = 31 * result + (puntosIntermediosX != null ? puntosIntermediosX.hashCode() : 0);
        result = 31 * result + (puntosIntermediosY != null ? puntosIntermediosY.hashCode() : 0);
        result = 31 * result + (idConexion != null ? idConexion.hashCode() : 0);
        return result;
    }
}
