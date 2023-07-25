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

    public ConectorTemplate(TipoConector tipo, String nombre, boolean multiple) {
        this(tipo, nombre, multiple, 1, Integer.MAX_VALUE, "n");
    }

    public ConectorTemplate(TipoConector tipo, String nombre) {
        this(tipo, nombre, false);
    }

    public ConectorTemplate(TipoConector tipo, String nombre, int minConectores) {
        this(tipo, nombre, true, minConectores, Integer.MAX_VALUE, "n");
    }
}
