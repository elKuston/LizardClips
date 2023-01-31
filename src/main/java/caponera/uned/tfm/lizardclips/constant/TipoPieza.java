package caponera.uned.tfm.lizardclips.constant;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

@Getter
public enum TipoPieza {
    NOT("NOT", ImageUtils.MEDIA_BASE_FOLDER + "/not.png", 1, 1, ModelicaGenerator.BASIC + ".Not"),
    AND("AND", ImageUtils.MEDIA_BASE_FOLDER + "/and.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".And"),
    NAND("NAND", ImageUtils.MEDIA_BASE_FOLDER + "/nand.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nand"),
    OR("OR", ImageUtils.MEDIA_BASE_FOLDER + "/or.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Or"),
    NOR("NOR", ImageUtils.MEDIA_BASE_FOLDER + "/nor.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nor"),
    SET("SET", ImageUtils.MEDIA_BASE_FOLDER + "/set.png", 0, 0, ModelicaGenerator.SOURCES + ".Set"),
    DIGITAL_CLOCK("DIGITAL_CLOCK", ImageUtils.MEDIA_BASE_FOLDER + "/clock.png", 0, 0,
            ModelicaGenerator.SOURCES + ".DigitalClock");
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
