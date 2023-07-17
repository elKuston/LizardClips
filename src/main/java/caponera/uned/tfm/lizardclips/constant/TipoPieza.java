package caponera.uned.tfm.lizardclips.constant;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.modelo.Propiedad;
import caponera.uned.tfm.lizardclips.modelo.PropiedadLogic;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSimple;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import java.util.List;

@Getter
public enum TipoPieza {
    NOT("NOT", ImageUtils.pathImagenMedia("not.png"), 1, 1, ModelicaGenerator.BASIC + ".Not",
            List.of()), AND("AND", ImageUtils.pathImagenMedia("/and.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".And", List.of()),
    NAND("NAND", ImageUtils.pathImagenMedia("nand.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nand", List.of()),
    OR("OR", ImageUtils.pathImagenMedia("or.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Or", List.of()),
    NOR("NOR", ImageUtils.pathImagenMedia("nor.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Nor", List.of()),
    XOR("XOR", ImageUtils.pathImagenMedia("xor.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Xor", List.of()),
    XNOR("XNOR", ImageUtils.pathImagenMedia("xnor.png"), 2, Integer.MAX_VALUE,
            ModelicaGenerator.BASIC + ".Xnor", List.of()),
    SET("SET", ImageUtils.pathImagenMedia("set.png"), 0, 0, ModelicaGenerator.SOURCES + ".Set",
            List.of(new PropiedadLogic("x", ""))),
    STEP("STEP", ImageUtils.pathImagenMedia("step.png"), 0, 0, ModelicaGenerator.SOURCES + ".Step",
            List.of(new PropiedadLogic("before", "Logic value before step"),
                    new PropiedadLogic("after", "'1'", "Logic value after step"),
                    new PropiedadSimple("0", "stepTime", Propiedad.UNIDAD_REAL, "step time"))),
    DIGITAL_CLOCK("DIGITAL_CLOCK", ImageUtils.pathImagenMedia("clock.png"), 0, 0,
            ModelicaGenerator.SOURCES + ".DigitalClock",
            List.of(new PropiedadSimple("0", "startTime", Propiedad.UNIDAD_TIME,
                            "Output = offset for time < startTime [s]"),
                    new PropiedadSimple("1", "period", Propiedad.UNIDAD_TIME,
                            "Time for one period [s]"),
                    new PropiedadSimple("50", "width", Propiedad.UNIDAD_REAL,
                            "Width of pulses in % of period")));
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
