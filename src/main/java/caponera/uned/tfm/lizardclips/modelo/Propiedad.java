package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Propiedad<T> {
    public static final String UNIDAD_REAL = "Real";
    public static final String UNIDAD_TIME = ModelicaGenerator.SI + ".Time";
    @Getter
    @Setter
    private T valor;

    @Getter
    private String nombre;

    @Getter
    @Setter
    private String unidad;


}
