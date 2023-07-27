package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.constant.ConectorTemplate;
import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.gui.PanelCircuito;
import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.utils.AnguloRotacion;
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.*;

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

    @Transient
    @Getter
    @Setter
    private static boolean renderNombresPines = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPieza;

    private String nombrePieza;

    private Punto posicion;

    @Enumerated(EnumType.ORDINAL)
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

    private String[] valoresPropiedades;

    private int[] nPinesConectoresMultiples;

    public Pieza(Circuito circuito, TipoPieza tipoPieza) {
        commonSetup(circuito, tipoPieza);
        setupConectores();
        this.valoresPropiedades =
                tipoPieza.getPropiedades().stream().map(prop -> prop.getValor().toString())
                        .toArray(String[]::new);

    }

    private void commonSetup(Circuito circuito, TipoPieza tipoPieza) {
        this.circuito = circuito;
        this.tipoPieza = tipoPieza;
        setImagen(ImageUtils.cargarImagenEscalada(tipoPieza.getPathImagen(), Punto.getEscala()));
    }

    private void setupConectores() {
        setupConectores(
                tipoPieza.getConectoresConNombre().stream().filter(ConectorTemplate::isMultiple)
                        .mapToInt(ConectorTemplate::getMinConectores).toArray());

    }

    private void setupConectores(int[] nPinesConectoresMultiples) {
        this.nPinesConectoresMultiples = nPinesConectoresMultiples.clone();
        this.conectores = generarConectores(tipoPieza);
        this.conectores.forEach(con -> con.setPieza(this));
        reposicionarConectores();

    }

    public Pieza(Circuito circ, Pieza otra) {
        commonSetup(circ, otra.getTipoPieza());
        this.setNombrePieza(otra.getNombrePieza());
        this.setPosicion(new Punto(otra.getPosicion()));
        this.setRotacion(otra.getRotacion());
        this.setValoresPropiedades(otra.valoresPropiedades.clone());
        setupConectores(otra.getNPinesConectoresMultiples());

    }

    public void setValorPropiedad(int nPropiedad, String valorPropiedad) {
        valoresPropiedades[nPropiedad] = valorPropiedad;
    }

    public int getNPinesConectorMultiple(int indiceConectorMultiple) {
        return nPinesConectoresMultiples[indiceConectorMultiple];
    }

    public void setNPinesConectorMultiple(int indiceConectorMultiple, int nPines) {
        nPinesConectoresMultiples[indiceConectorMultiple] = nPines;
    }

    private List<Conector> generarConectores(TipoPieza tipoPieza) {
        List<Conector> lista = new ArrayList<>();

        //Conectores con nombre

        List<ConectorTemplate> conectoresMultiples =
                tipoPieza.getConectoresConNombre().stream().filter(ConectorTemplate::isMultiple)
                        .toList();
        List<ConectorTemplate> conectoresIndividuales =
                tipoPieza.getConectoresConNombre().stream().filter(con -> !con.isMultiple())
                        .toList();

        for (int i = 0; i < conectoresMultiples.size(); i++) {
            ConectorTemplate cm = conectoresMultiples.get(i);
            double posRelX = cm.getTipo().equals(TipoConector.ENTRADA) ? 0 : 1;
            for (int c = 0; c < nPinesConectoresMultiples[i]; c++) {
                lista.add(new Conector(cm, c + 1));
            }
        }

        for (ConectorTemplate ci : conectoresIndividuales) {
            double posRelX = ci.getTipo().equals(TipoConector.ENTRADA) ? 0 : 1;
            lista.add(new Conector(posRelX, 0.5, ci.getTipo(), ci.getNombre()));
        }
        return lista;
    }

    @PostLoad
    protected void postLoad() {
        setImagen(ImageUtils.rotar(
                ImageUtils.cargarImagenEscalada(tipoPieza.getPathImagen(), tamano.width,
                        tamano.height), -rotacion.getAngulo()));
    }

    public Punto getPosicionConectorEnPanel(Conector conector, Punto posicionPieza) {
        double posicionRelativaX = conector.getPosicionRelativaX();
        double posicionRelativaY = conector.getPosicionRelativaY();
        for (int i = 0; i < getRotacion().ordinal(); i++) {
            double temp = posicionRelativaX;
            posicionRelativaX = posicionRelativaY;
            posicionRelativaY = 1 - temp;

        }

        Punto posicionConector =
                new Punto((int) (posicionPieza.getX() + posicionRelativaX * getWidth()),
                        (int) (posicionPieza.getY() + posicionRelativaY * getHeight()));

        //Mantener posición dentro de los limites de la pieza
        if (posicionConector.getX() - Conector.getRadio() < posicionPieza.getX()) {
            posicionConector.translate(Conector.getRadio(), 0);
        }
        if (posicionConector.getX() + Conector.getRadio() > posicionPieza.getX() + getWidth()) {
            posicionConector.translate(-Conector.getRadio(), 0);
        }
        if (posicionConector.getY() - Conector.getRadio() < posicionPieza.getY()) {
            posicionConector.translate(0, Conector.getRadio());
        }
        if (posicionConector.getY() + Conector.getRadio() > posicionPieza.getY() + getHeight()) {
            posicionConector.translate(0, -Conector.getRadio());
        }
        return posicionConector;
    }

    public void rotar(boolean derecha) {
        int incr = derecha ? -1 : 1;
        rotacion = AnguloRotacion.values()[
                (rotacion.ordinal() + incr + AnguloRotacion.values().length) %
                        AnguloRotacion.values().length];
        setImagen(ImageUtils.rotar(imagen, 90 * -incr));
    }

    public Punto getPosicionConectorEnPanel(Conector conector) {
        return getPosicionConectorEnPanel(conector, posicion);
    }


    public void dibujar(PanelCircuito panelCircuito, Graphics g, Punto posicion, boolean dibujarContorno,
                        Map<Conector, Color> coloresConectores) {
        if (getImagen() != null) {
            getImagen().paintIcon(panelCircuito, g, (int) posicion.getX(), (int) posicion.getY());
        }
        if (dibujarContorno) {
            g.drawRect((int) posicion.getX(), (int) posicion.getY(), getWidth(), getHeight());
        }

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 3 * Conector.getRadio()));

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
            g.fillOval((int) (pos.getX() - Conector.getRadio()),
                    (int) (pos.getY() - Conector.getRadio()), (2 * Conector.getRadio()),
                    (2 * Conector.getRadio()));
            g.setColor(Color.BLACK);

            if (isRenderNombresPines()) {
                int desplazamientoX, desplazamientoY;
                if (getRotacion().equals(AnguloRotacion.ROT_0) ||
                        getRotacion().equals(AnguloRotacion.ROT_180)) {
                    desplazamientoY = -2 * Conector.getRadio();
                    desplazamientoX = (c.getNombreConector().length() + 2) * Conector.getRadio();
                    double centro = getPosicion().getX() + getWidth() / 2;
                    if (pos.getX() < centro) {
                        desplazamientoX *= -1;
                        desplazamientoX -= c.getNombreConector().length() * Conector.getRadio();
                    }
                } else {
                    desplazamientoX = 0;
                    desplazamientoY = 3 * Conector.getRadio();
                    double centro = getPosicion().getY() + getHeight() / 2;
                    if (pos.getY() < centro) {
                        desplazamientoY *= -1;
                        desplazamientoY += Conector.getRadio();
                    }
                }
                g.drawString(c.getNombreConector(), pos.getX() + desplazamientoX,
                        pos.getY() + desplazamientoY);
            }
        }
        if (isRenerNombresPiezas()) {
            g.drawString(ModelicaGenerator.nombrePieza(this), posicion.getX(),
                    posicion.getY() - 4 * Conector.getRadio());
        }
    }

    public void reposicionarConectores() {
        List<Conector> entradas =
                conectores.stream().filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA))
                        .toList();
        List<Conector> salidas =
                conectores.stream().filter(c -> c.getTipoConector().equals(TipoConector.SALIDA))
                        .toList();

        for (int i = 0; i < entradas.size(); i++) {
            float pos_y = (i + 1) / ((entradas.size() + 1) * 1f);
            entradas.get(i).setPosicionRelativaY(pos_y);
        }

        for (int i = 0; i < salidas.size(); i++) {
            float pos_y = (i + 1) / ((salidas.size() + 1) * 1f);
            salidas.get(i).setPosicionRelativaY(pos_y);
        }
    }


    public void actualizarConectores(int[] newNPinesConectoresMultiples) {
        int counter = 0;
        for (int i = 0; i < nPinesConectoresMultiples.length; i++) {
            ConectorTemplate ct = tipoPieza.getConectoresConNombre().get(i);
            int n = nPinesConectoresMultiples[i];
            int newN = newNPinesConectoresMultiples[i];
            counter += Math.min(newN, n);
            int diff = newN - n;
            for (int d = 0; d < Math.abs(diff); d++) {
                if (diff > 0) {
                    Conector c = new Conector(ct, n + d + 1);
                    c.setPieza(this);
                    conectores.add(counter, c);
                    counter++;
                } else {
                    Conector c = conectores.get(counter);
                    circuito.borrarConexionesConector(c);
                    conectores.remove(c);
                }
            }

        }
        this.nPinesConectoresMultiples = newNPinesConectoresMultiples.clone();

        reposicionarConectores();
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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pieza pieza = (Pieza) o;
        return Objects.equals(getNombrePieza(), pieza.getNombrePieza()) &&
                Objects.equals(getPosicion(), pieza.getPosicion()) && getRotacion() == pieza.getRotacion() &&
                getTipoPieza().equals(pieza.getTipoPieza()) &&
                Arrays.equals(getValoresPropiedades(), pieza.getValoresPropiedades()) &&
                Arrays.equals(nPinesConectoresMultiples, pieza.nPinesConectoresMultiples);
    }

    @Override public int hashCode() {
        int result = Objects.hash(getNombrePieza(), getPosicion(), getRotacion(), getTipoPieza());
        result = 31 * result + Arrays.hashCode(getValoresPropiedades());
        result = 31 * result + Arrays.hashCode(nPinesConectoresMultiples);
        return result;
    }
}
