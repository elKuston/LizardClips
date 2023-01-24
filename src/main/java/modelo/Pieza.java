package modelo;

import constant.TipoConector;
import gui.PanelCircuito;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
import utils.ImageUtils;
import utils.Punto;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
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

    private String pathImagen;

    @OneToMany(mappedBy = "pieza", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Conector> conectores;

    private Dimension tamano;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private Circuito circuito;


    public Pieza(Circuito circuito, String pathImagen, int ancho, int alto, List<Conector> conectores) {
        conectores.forEach(con -> con.setPieza(this));

        this.pathImagen = pathImagen;
        this.circuito = circuito;
        this.conectores = conectores;
        imagen = ImageUtils.cargarImagenEscalada(pathImagen, ancho, alto);
        tamano = new Dimension(imagen.getIconWidth(), imagen.getIconHeight());
    }

    @PostLoad
    protected void cargarImagen() {
        imagen = ImageUtils.cargarImagenEscalada(pathImagen, tamano.width, tamano.height);
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
                    2 * Conector.RADIO, 2 * Conector.RADIO);
            g.setColor(Color.BLACK);
        }

        g.drawString("ID: " + getIdPieza(), posicion.getX(), posicion.getY());
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
