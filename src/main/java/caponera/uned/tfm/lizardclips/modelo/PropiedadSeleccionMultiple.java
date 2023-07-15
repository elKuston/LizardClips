package caponera.uned.tfm.lizardclips.modelo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PropiedadSeleccionMultiple extends Propiedad<String> {
    @Getter
    @Setter
    private List<String> valoresPosibles;

    public PropiedadSeleccionMultiple(String valor, String descripcion, List<String> valoresPosibles) {
        super(valor, descripcion);
        this.valoresPosibles = valoresPosibles;
    }
}
