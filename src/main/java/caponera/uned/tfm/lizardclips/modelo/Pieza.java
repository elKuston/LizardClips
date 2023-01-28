package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.gui.PanelCircuito;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
@Entity
@NoArgsConstructor
public class Pieza implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPieza;

    private Punto posicion;

    @Transient
    @ToString.Exclude
    private ImageIcon imagen;

    @OneToMany(mappedBy = "pieza", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Conector> conectores;

    private Dimension tamano;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Circuito circuito;

    @Enumerated(EnumType.ORDINAL)
    private TipoPieza tipoPieza;

    public Pieza(Circuito circuito, TipoPieza tipoPieza, int nConectoresEntrada) {
        this.circuito = circuito;
        this.tipoPieza = tipoPieza;
        this.conectores = generarConectores(tipoPieza, nConectoresEntrada);
        this.conectores.forEach(con -> con.setPieza(this));
        setImagen(ImageUtils.cargarImagenEscalada(tipoPieza.getPathImagen(),
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

        Punto posicionConector = new Punto(
                (int) (posicionPieza.getX() + conector.getPosicionRelativaX() * getWidth()),
                (int) (posicionPieza.getY() + conector.getPosicionRelativaY() * getHeight()));
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

    public Punto getPosicionConectorEnPanel(Conector conector) {
        return getPosicionConectorEnPanel(conector, posicion);
    }


    public void dibujar(PanelCircuito panelCircuito, Graphics g, Punto posicion, boolean dibujarContorno, Map<Conector, Color> coloresConectores) {
        getImagen().paintIcon(panelCircuito, g, (int) posicion.getX(), (int) posicion.getY());
        if (dibujarContorno) {
            g.drawRect((int) posicion.getX(), (int) posicion.getY(), getWidth(), getHeight());
        }

        for (Conector c : conectores) {
            Color color;
            if (coloresConectores == null) {
                color = c.getTipoConector().equals(TipoConector.ENTRADA) ? Color.BLUE : Color.green;
            } else {
                color = coloresConectores.get(c);
            }
            g.setColor(color);
            Punto pos = getPosicionConectorEnPanel(c, posicion);
            g.fillOval((int) (pos.getX() - Conector.RADIO), (int) (pos.getY() - Conector.RADIO),
                    (2 * Conector.RADIO), (2 * Conector.RADIO));
            g.setColor(Color.BLACK);
        }

        g.drawString("ID: " + getIdPieza(), posicion.getX(), posicion.getY());
    }

    public void addConectorEntrada() {
        if (conectores.size() > tipoPieza.getConectoresEntradaMax()) {
            throw new RuntimeException("El número máximo de entradas para este tipo de pieza es " +
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
            throw new RuntimeException("El número mínimo de entradas para este tipo de pieza es " +
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

        Pieza that = (Pieza) o;

        if (!imagen.equals(that.imagen))
            return false;
        if (!tamano.equals(that.tamano))
            return false;
        return circuito.equals(that.circuito);
    }


}
