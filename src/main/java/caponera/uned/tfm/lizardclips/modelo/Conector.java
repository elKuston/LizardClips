package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.constant.ConectorTemplate;
import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.utils.Punto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Conector implements Serializable {
    private static final int RADIO = 3;
    public static final Color colorConector = Color.BLACK; //new Color(119, 0, 200);

    @ToString.Exclude
    private double posicionRelativaX;

    @ToString.Exclude
    private double posicionRelativaY;

    @Enumerated(EnumType.STRING)
    private TipoConector tipoConector;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConector;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pieza")
    @ToString.Exclude
    @Setter
    private Pieza pieza;

    private String nombreConector;

    private boolean reposicionar = true;

    public Conector(double posicionRelativaX, double posicionRelativaY, TipoConector tipoConector, String nombre) {
        this.posicionRelativaX = posicionRelativaX;
        this.posicionRelativaY = posicionRelativaY;
        this.tipoConector = tipoConector;
        this.nombreConector = nombre;
    }

    public Conector(ConectorTemplate template, int index) {
        this(template.getRelativeX(), template.getRelativeY(), template.getTipo(),
                template.getNombre() + "[" + index + "]");
        this.reposicionar = template.isReposicionar();
    }

    public Conector(ConectorTemplate template) {
        this(template.getRelativeX(), template.getRelativeY(), template.getTipo(),
                template.getNombre());
        this.reposicionar = template.isReposicionar();

    }

    public static int getRadio() {
        return (int) (RADIO * (1 + Math.max((Punto.getEscala() - 1), 0)));//se agranda pero no se reduce
    }

    public Punto getPosicionEnPanel() {
        return pieza.getPosicionConectorEnPanel(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conector conector = (Conector) o;
        return getTipoConector() == conector.getTipoConector() &&
                Objects.equals(getPieza(), conector.getPieza()) &&
                Objects.equals(getNombreConector(), conector.getNombreConector());
    }

    @Override public int hashCode() {
        return Objects.hash(getTipoConector(), getPieza(), getNombreConector());
    }
}
