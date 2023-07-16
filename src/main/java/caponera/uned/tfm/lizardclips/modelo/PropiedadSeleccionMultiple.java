package caponera.uned.tfm.lizardclips.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PropiedadSeleccionMultiple extends Propiedad<String> {
    public static final List<String> SELECCION_MULTIPLE_LOGICAL =
            List.of("'U'", "'X'", "'0'", "'1'", "'Z'", "'W'", "'L'", "'H'", "'-'");
    public static final String PREFIX_SELECCION_MULTIPLE_LOCICAL = "L.";

    @Getter
    @Setter
    private List<String> valoresPosibles;
    @Getter
    private String prefix;

    public PropiedadSeleccionMultiple(String valor, String nombre, List<String> valoresPosibles, String unidad) {
        super(valor, nombre, unidad);
        this.valoresPosibles = valoresPosibles;
    }

    public PropiedadSeleccionMultiple(String valor, String nombre, List<String> valoresPosibles, String prefix, String unidad) {
        this(prefix + valor, nombre, valoresPosibles.stream().map(vp -> prefix + vp).toList(),
                unidad);
    }
}
