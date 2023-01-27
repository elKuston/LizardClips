package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import caponera.uned.tfm.lizardclips.utils.Punto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@ToString
@Entity
public class Circuito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCircuito;

    /*@Transient
    private Map<Pieza, Punto> componentes;*/

    @Transient
    private ControladorCircuito controlador;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Conexion> conexiones;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "circuito")
    private List<Pieza> componentes;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @ToString.Exclude
    private byte[] thumbnail;

    private String nombre = "";

    public Circuito() {
        componentes = new ArrayList<>();
        conexiones = new ArrayList<>();
    }

    public byte[] getThumbnail() {
        return ImageUtils.bytesFromBufferedImage(controlador.generarThumbnail());
    }

    public void moverPieza(Pieza pieza, Punto posicion) {
        pieza.setPosicion(posicion);
    }

    public void borrarPieza(Pieza pieza) {
        borrarConexionesPieza(pieza);
        componentes.remove(pieza);
    }

    public void borrarConexionesPieza(Pieza pieza) {
        conexiones.removeIf(con -> (con.getDestino().getPieza().equals(pieza) ||
                con.getOrigen().getPieza().equals(pieza)));
    }

    public void addConexion(Conexion c) {
        this.conexiones.add(c);
    }

    public void borrarConexion(Conexion c) {
        this.conexiones.remove(c);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Circuito circuito = (Circuito) o;
        return idCircuito != null && Objects.equals(idCircuito, circuito.idCircuito);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void colocarPieza(Pieza pieza, Punto posicion) {
        componentes.add(pieza);
        pieza.setPosicion(posicion);
    }

    public void borrarConexionesConector(Conector c) {
        conexiones.removeIf(con -> con.getOrigen().equals(c) || con.getDestino().equals(c));
    }

    public void cancelarConexion() {
        Optional<Conexion> enCurso = conexiones.stream().filter(Conexion::enCurso).findFirst();
        enCurso.ifPresent(this::borrarConexion);
    }
}
