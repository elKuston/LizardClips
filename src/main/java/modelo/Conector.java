package modelo;

import constant.TipoConector;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.awt.Point;

@EqualsAndHashCode
@Data
public class Conector {
    public static final int RADIO = 5;

    private final double posicionRelativaX, posicionRelativaY;
    private final TipoConector tipoConector;
    private Pieza pieza;

    public Conector(double posicionRelativaX, double posicionRelativaY, TipoConector tipoConector) {
        this.posicionRelativaX = posicionRelativaX;
        this.posicionRelativaY = posicionRelativaY;
        this.tipoConector = tipoConector;
    }

    public Point getPosicionEnPanel() {
        return pieza.getPosicionConectorEnPanel(this);
    }
}
