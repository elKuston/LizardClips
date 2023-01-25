package constant;

import lombok.Getter;
import modelica.ModelicaGenerator;
import utils.ImageUtils;

@Getter
public enum TipoPieza {
    AND("AND", ImageUtils.MEDIA_BASE_FOLDER + "/and.png", 2, -1, ModelicaGenerator.BASIC + ".And"),
    OR("OR", ImageUtils.MEDIA_BASE_FOLDER + "/or.png", 2, -1, ModelicaGenerator.BASIC + ".Or"),
    SET("SET", ImageUtils.MEDIA_BASE_FOLDER + "/set.png", 0, 0, ModelicaGenerator.SOURCES + ".Set");
    final String nombre;
    final String pathImagen;
    final String claseModelica;
    final int conectoresEntradaMin;
    final int conectoresEntradaMax;

    TipoPieza(String nombre, String pathImagen, int conectoresEntradaMin, int conectoresEntradaMax, String claseModelica) {
        this.nombre = nombre;
        this.pathImagen = pathImagen;
        this.conectoresEntradaMin = conectoresEntradaMin;
        this.conectoresEntradaMax = conectoresEntradaMax;
        this.claseModelica = claseModelica;
    }
}
