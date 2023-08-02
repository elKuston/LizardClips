package caponera.uned.tfm.lizardclips.constant;

import caponera.uned.tfm.lizardclips.modelo.Conector;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConectorTemplate {
    private TipoConector tipo;
    private String nombre;
    private boolean multiple;
    private int minConectores, maxConectores;
    private String nombreNumPines;
    private double relativeX, relativeY;
    private boolean reposicionar = true;

    public ConectorTemplate(TipoConector tipo, String nombre, boolean multiple) {
        this(tipo, nombre, multiple, 1, Integer.MAX_VALUE, "n", tipo.equals(TipoConector.ENTRADA) ? 0 : 1, 0, true);
    }

    public ConectorTemplate(TipoConector tipo, String nombre) {
        this(tipo, nombre, false);
    }

    public ConectorTemplate(TipoConector tipo, String nombre, int minConectores) {
        this(tipo, nombre, true, minConectores, Integer.MAX_VALUE, "n", tipo.equals(TipoConector.ENTRADA) ? 0 : 1, 0,
                true);
    }

    public ConectorTemplate(TipoConector tipo, String nombre, double relativeX, double relativeY) {
        this(tipo, nombre, false);
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.reposicionar = false;
    }
}
