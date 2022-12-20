package componentes;

import constant.TipoConector;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Conector extends Componente {
    public static final int RADIO = 5;

    private final double posicionRelativaX, posicionRelativaY;
    private final TipoConector tipoConector;

    public Conector(double posicionRelativaX, double posicionRelativaY, TipoConector tipoConector) {
        this.posicionRelativaX = posicionRelativaX;
        this.posicionRelativaY = posicionRelativaY;
        this.tipoConector = tipoConector;
    }
}
