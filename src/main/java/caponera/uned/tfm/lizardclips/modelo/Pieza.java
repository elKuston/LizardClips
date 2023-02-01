package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.gui.PanelCircuito;
import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.utils.AnguloRotacion;
import caponera.uned.tfm.lizardclips.utils.I18NUtils;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Pieza implements Serializable {
    @Transient
    @Getter
    @Setter
    private static boolean renerNombresPiezas = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPieza;

    private String nombrePieza;

    private Punto posicion;

    private AnguloRotacion rotacion = AnguloRotacion.ROT_0;

    @Transient
    @ToString.Exclude
    private ImageIcon imagen;

    @OneToMany(mappedBy = "pieza", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Conector> conectores;

    private Dimension tamano;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_circuito")
    @ToString.Exclude
    private Circuito circuito;

    @Enumerated(EnumType.ORDINAL)
    private TipoPieza tipoPieza;

    public Pieza(Circuito circuito, TipoPieza tipoPieza, int nConectoresEntrada) {
        this.circuito = circuito;
        this.tipoPieza = tipoPieza;
        this.conectores = generarConectores(tipoPieza, nConectoresEntrada);
        this.conectores.forEach(con -> con.setPieza(this));
        setImagen(ImageUtils.cargarImageneEscaladaPreserveRatio(tipoPieza.getPathImagen(),
                ImageUtils.DEFAULT_IMAGE_WIDTH, ImageUtils.DEFAULT_IMAGE_HEIGHT));
    }

    private List<Conector> generarConectores(TipoPieza tipoPieza, int nConectoresEntrada) {
        if (nConectoresEntrada < tipoPieza.getConectoresEntradaMin() ||
                (tipoPieza.getConectoresEntradaMax() > 0 &&
                        nConectoresEntrada > tipoPieza.getConectoresEntradaMax())) {
            throw new RuntimeException("No se puede crear una pieza de este tipo");
        }
        List<Conector> lista = new ArrayList<>();
        for (int i = 0; i < nConectoresEntrada; i++) {
            float pos_y = (i + 1) / (nConectoresEntrada * 1f + 1);
            lista.add(new Conector(0, pos_y, TipoConector.ENTRADA));
        }
        lista.add(new Conector(1, 0.5, TipoConector.SALIDA));
        return lista;
    }

    @PostLoad
    protected void cargarImagen() {
        setImagen(ImageUtils.cargarImagenEscalada(tipoPieza.getPathImagen(), tamano.width,
                tamano.height));
    }

    public Punto getPosicionConectorEnPanel(Conector conector, Punto posicionPieza) {
        double posicionRelativaX = conector.getPosicionRelativaX();
        double posicionRelativaY = conector.getPosicionRelativaY();
        for (int i = 0; i < getRotacion().ordinal(); i++) {
            double temp = posicionRelativaX;
            posicionRelativaX = 1 - posicionRelativaY;
            posicionRelativaY = temp;

        }

        Punto posicionConector =
                new Punto((int) (posicionPieza.getX() + posicionRelativaX * getWidth()),
                        (int) (posicionPieza.getY() + posicionRelativaY * getHeight()));

        //Mantener posición dentro de los limites de la pieza
        if (posicionConector.getX() - Conector.RADIO < posicionPieza.getX()) {
            posicionConector.translate(Conector.RADIO, 0);
        }
        if (posicionConector.getX() + Conector.RADIO > posicionPieza.getX() + getWidth()) {
            posicionConector.translate(-Conector.RADIO, 0);
        }
        if (posicionConector.getY() - Conector.RADIO < posicionPieza.getY()) {
            posicionConector.translate(0, Conector.RADIO);
        }
        if (posicionConector.getY() + Conector.RADIO > posicionPieza.getY() + getHeight()) {
            posicionConector.translate(0, -Conector.RADIO);
        }
        return posicionConector;
    }

    public void rotar(boolean derecha) {
        int incr = derecha ? 1 : -1;
        rotacion = AnguloRotacion.values()[
                (rotacion.ordinal() + incr + AnguloRotacion.values().length) %
                        AnguloRotacion.values().length];
        setImagen(ImageUtils.rotar(imagen, 90 * incr));
    }

    public Punto getPosicionConectorEnPanel(Conector conector) {
        return getPosicionConectorEnPanel(conector, posicion);
    }


    public void dibujar(PanelCircuito panelCircuito, Graphics g, Punto posicion, boolean dibujarContorno, Map<Conector, Color> coloresConectores) {
        if (getImagen() != null) {
            getImagen().paintIcon(panelCircuito, g, (int) posicion.getX(), (int) posicion.getY());
        }
        if (dibujarContorno) {
            g.drawRect((int) posicion.getX(), (int) posicion.getY(), getWidth(), getHeight());
        }

        for (Conector c : conectores) {
            Color color;
            if (coloresConectores == null) {
                //color = c.getTipoConector().equals(TipoConector.ENTRADA) ? Color.BLUE : Color.green;
                color = Conector.colorConector;
            } else {
                color = coloresConectores.get(c);
            }
            g.setColor(color);
            Punto pos = getPosicionConectorEnPanel(c, posicion);
            g.fillOval((int) (pos.getX() - Conector.RADIO), (int) (pos.getY() - Conector.RADIO),
                    (2 * Conector.RADIO), (2 * Conector.RADIO));
            g.setColor(Color.BLACK);
        }
        if (isRenerNombresPiezas()) {
            g.drawString(ModelicaGenerator.nombrePieza(this), posicion.getX(), posicion.getY() - 2);
        }
    }

    public void addConectorEntrada() {
        if (conectores.size() > tipoPieza.getConectoresEntradaMax()) {
            throw new RuntimeException(I18NUtils.getString("max_input_connectors") +
                    tipoPieza.getConectoresEntradaMax());
        }
        Conector c = new Conector(0, 1, TipoConector.ENTRADA);
        c.setPieza(this);
        //Size-2 porque siempre queremos que el conector de salida sea el último
        conectores.add(conectores.size() - 1, c);
        reposicionarConectores();
    }

    public void removeConectorEntrada() {
        if (conectores.size() - 2 < tipoPieza.getConectoresEntradaMin()) {
            throw new RuntimeException(I18NUtils.getString("min_input_connectors") +
                    tipoPieza.getConectoresEntradaMin());
        }
        Conector c = conectores.get(conectores.size() - 2);
        circuito.borrarConexionesConector(c);
        conectores.remove(c);
        reposicionarConectores();
    }

    public void reposicionarConectores() {
        for (int i = 0; i < conectores.size() - 1; i++) {
            float pos_y = (i + 1) / (conectores.size() * 1f);
            conectores.get(i).setPosicionRelativaY(pos_y);
        }
    }

    public int getWidth() {
        return (int) tamano.getWidth();
    }

    public int getHeight() {
        return (int) tamano.getHeight();
    }

    public Rectangle getBounds() {
        return new Rectangle(posicion.getPoint(), getTamano());
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
        this.tamano = new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Pieza pieza = (Pieza) o;

        if (idPieza != null ? !idPieza.equals(pieza.idPieza) : pieza.idPieza != null)
            return false;
        if (posicion != null ? !posicion.equals(pieza.posicion) : pieza.posicion != null)
            return false;
        if (imagen != null ? !imagen.equals(pieza.imagen) : pieza.imagen != null)
            return false;
        if (conectores != null ? !conectores.equals(pieza.conectores) : pieza.conectores != null)
            return false;
        if (tamano != null ? !tamano.equals(pieza.tamano) : pieza.tamano != null)
            return false;
        if (circuito != null ? !circuito.equals(pieza.circuito) : pieza.circuito != null)
            return false;
        return tipoPieza == pieza.tipoPieza;
    }

    @Override
    public int hashCode() {
        int result = idPieza != null ? idPieza.hashCode() : 0;
        result = 31 * result + (posicion != null ? posicion.hashCode() : 0);
        result = 31 * result + (imagen != null ? imagen.hashCode() : 0);
        result = 31 * result + (conectores != null ? conectores.hashCode() : 0);
        result = 31 * result + (tamano != null ? tamano.hashCode() : 0);
        result = 31 * result + (circuito != null ? circuito.hashCode() : 0);
        result = 31 * result + (tipoPieza != null ? tipoPieza.hashCode() : 0);
        return result;
    }
}
