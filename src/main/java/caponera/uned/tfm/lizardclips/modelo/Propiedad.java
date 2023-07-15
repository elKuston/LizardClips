package caponera.uned.tfm.lizardclips.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Propiedad<T> {
    @Getter
    @Setter
    private T valor;

    @Getter
    private String descripcion;


}
