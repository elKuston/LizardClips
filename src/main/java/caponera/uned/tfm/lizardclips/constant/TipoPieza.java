package caponera.uned.tfm.lizardclips.constant;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.modelo.Propiedad;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSeleccionMultiple;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSimple;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum TipoPieza {
    NOT("NOT", ImageUtils.MEDIA_BASE_FOLDER + "/not.png", 1, 1, ModelicaGenerator.BASIC + ".Not",
            List.of(new PropiedadSimple("0", "prueba", "Real"),
                    new PropiedadSimple("jeje", "prueba2", "Real"),
                    new PropiedadSeleccionMultiple("hola", "jeje", List.of("hola", "que", "tal"),
                            "Real"))),
    AND("AND", ImageUtils.MEDIA_BASE_FOLDER + "/and.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".And", new ArrayList<>()),
    NAND("NAND", ImageUtils.MEDIA_BASE_FOLDER + "/nand.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nand", new ArrayList<>()),
    OR("OR", ImageUtils.MEDIA_BASE_FOLDER + "/or.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Or", new ArrayList<>()),
    NOR("NOR", ImageUtils.MEDIA_BASE_FOLDER + "/nor.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nor", new ArrayList<>()),
    XOR("XOR", ImageUtils.MEDIA_BASE_FOLDER + "/xor.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Xor", new ArrayList<>()),
    XNOR("XNOR", ImageUtils.MEDIA_BASE_FOLDER + "/xnor.png", 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Xnor", new ArrayList<>()),
    SET("SET", ImageUtils.MEDIA_BASE_FOLDER + "/set.png", 0, 0, ModelicaGenerator.SOURCES + ".Set",
            List.of(new PropiedadSeleccionMultiple("'0'", "x",
                    PropiedadSeleccionMultiple.SELECCION_MULTIPLE_LOGICAL,
                    PropiedadSeleccionMultiple.PREFIX_SELECCION_MULTIPLE_LOCICAL,
                    ModelicaGenerator.LOGIC))),
    DIGITAL_CLOCK("DIGITAL_CLOCK", ImageUtils.MEDIA_BASE_FOLDER + "/clock.png", 0, 0,
            ModelicaGenerator.SOURCES + ".DigitalClock",
            List.of(new PropiedadSimple("0", "startTime", Propiedad.UNIDAD_TIME),
                    new PropiedadSimple("1", "period", Propiedad.UNIDAD_TIME),
                    new PropiedadSimple("50", "width", Propiedad.UNIDAD_REAL)));
    final String nombre;
    final String pathImagen;
    final String claseModelica;
    final int conectoresEntradaMin;
    final int conectoresEntradaMax;
    final List<Propiedad> propiedades;

    TipoPieza(String nombre, String pathImagen, int conectoresEntradaMin, int conectoresEntradaMax, String claseModelica, List<Propiedad> propiedades) {
        this.nombre = nombre;
        this.pathImagen = pathImagen;
        this.conectoresEntradaMin = conectoresEntradaMin;
        this.conectoresEntradaMax = conectoresEntradaMax;
        this.claseModelica = claseModelica;
        this.propiedades = propiedades;
    }
}
