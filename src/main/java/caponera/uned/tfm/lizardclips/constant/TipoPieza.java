package caponera.uned.tfm.lizardclips.constant;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.modelo.Conector;
import caponera.uned.tfm.lizardclips.modelo.Propiedad;
import caponera.uned.tfm.lizardclips.modelo.PropiedadLogic;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSimple;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import java.util.List;

@Getter
public enum TipoPieza {
    //region Basic
    NOT("NOT", ImageUtils.pathImagenMedia("not.png"), ModelicaGenerator.BASIC + ".Not",
            TabPaleta.BASIC, List.of(), List.of(new ConectorTemplate(TipoConector.ENTRADA, "x"),
            new ConectorTemplate(TipoConector.SALIDA, "y"))),
    AND("AND", ImageUtils.pathImagenMedia("and.png"),
            ModelicaGenerator.BASIC + ".And", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    NAND("NAND", ImageUtils.pathImagenMedia("nand.png"),
            ModelicaGenerator.BASIC + ".Nand", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    OR("OR", ImageUtils.pathImagenMedia("or.png"),
            ModelicaGenerator.BASIC + ".Or", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    NOR("NOR", ImageUtils.pathImagenMedia("nor.png"),
            ModelicaGenerator.BASIC + ".Nor", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    XOR("XOR", ImageUtils.pathImagenMedia("xor.png"),
            ModelicaGenerator.BASIC + ".Xor", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    XNOR("XNOR", ImageUtils.pathImagenMedia("xnor.png"),
            ModelicaGenerator.BASIC + ".Xnor", TabPaleta.BASIC, List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))), //endregion
    //region Gates
    INVGATE("INVGATE", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.GATES + ".InvGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    ANDGATE("ANDGATE", ImageUtils.pathImagenMedia("andgate.png"),
            ModelicaGenerator.GATES + ".AndGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    NANDGATE("ANDGATE", ImageUtils.pathImagenMedia("nandgate.png"),
            ModelicaGenerator.GATES + ".NandGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    ORGATE("ORGATE", ImageUtils.pathImagenMedia("orgate.png"),
            ModelicaGenerator.GATES + ".OrGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    NORGATE("NORGATE", ImageUtils.pathImagenMedia("norgate.png"),
            ModelicaGenerator.GATES + ".NorGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    XORGATE("XORGATE", ImageUtils.pathImagenMedia("xorgate.png"),
            ModelicaGenerator.GATES + ".XorGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    XNORGATE("XNORGATE", ImageUtils.pathImagenMedia("xnorgate.png"),
            ModelicaGenerator.GATES + ".XnorGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 2),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    BUFGATE("BUFGATE", ImageUtils.pathImagenMedia("bufgate.png"),
            ModelicaGenerator.GATES + ".BufGate", TabPaleta.GATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "rise inertial delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "fall inertial delay [s]"),
                    new PropiedadLogic("y0", "'U'", "initial value of output")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))), //endregion
    //region Sources
    SET("SET", ImageUtils.pathImagenMedia("set.png"), ModelicaGenerator.SOURCES + ".Set",
            TabPaleta.SOURCES, List.of(new PropiedadLogic("x", "")),
            List.of(new ConectorTemplate(TipoConector.SALIDA, "y"))),
    STEP("STEP", ImageUtils.pathImagenMedia("step.png"), ModelicaGenerator.SOURCES + ".Step",
            TabPaleta.SOURCES, List.of(new PropiedadLogic("before", "Logic value before step"),
            new PropiedadLogic("after", "'1'", "Logic value after step"),
            new PropiedadSimple("0", "stepTime", Propiedad.UNIDAD_REAL, "step time")),
            List.of(new ConectorTemplate(TipoConector.SALIDA, "y"))),
    DIGITAL_CLOCK("DIGITAL_CLOCK", ImageUtils.pathImagenMedia("clock.png"),
            ModelicaGenerator.SOURCES + ".DigitalClock", TabPaleta.SOURCES,
            List.of(new PropiedadSimple("0", "startTime", Propiedad.UNIDAD_TIME,
                            "Output = offset for time < startTime [s]"),
                    new PropiedadSimple("1", "period", Propiedad.UNIDAD_TIME,
                            "Time for one period [s]"),
                    new PropiedadSimple("50", "width", Propiedad.UNIDAD_REAL,
                            "Width of pulses in % of period")),
            List.of(new ConectorTemplate(TipoConector.SALIDA, "y"))),
    //endregion
    //region Tristates
    NXFERGATE("NXFERGATE", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".NXFERGATE", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    NRXFERGATE("NRXFERGATE", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".NRXFERGATE", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    PXFERGATE("PXFERGATE", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".PXFERGATE", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    PRXFERGATE("PRXFERGATE", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".PRXFERGATE", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    BUF3S("BUF3S", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".BUF3S", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]"),
                    new PropiedadSimple("1", "strength", Propiedad.UNIDAD_REAL, "Output strength")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    BUF3SL("BUF3SL", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".BUF3SL", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]"),
                    new PropiedadSimple("1", "strength", Propiedad.UNIDAD_REAL, "Output strength")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    INV3S("INV3S", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".INV3S", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]"),
                    new PropiedadSimple("1", "strength", Propiedad.UNIDAD_REAL, "Output strength")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    INV3SL("INV3SL", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".INV3SL", TabPaleta.TRISTATES,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]"),
                    new PropiedadSimple("1", "strength", Propiedad.UNIDAD_REAL, "Output strength")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "enable"),
                    new ConectorTemplate(TipoConector.ENTRADA, "x"),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    WiredX("WiredX", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.TRISTATES + ".WiredX", TabPaleta.TRISTATES,
            List.of(),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "x", 1),
                    new ConectorTemplate(TipoConector.SALIDA, "y"))),
    //endregion
    //region Multiplexers
    MUX2x1("MUX2x1", ImageUtils.pathImagenMedia("invgate.png"),
            ModelicaGenerator.MULTIPLEXERS + ".MUX2x1", TabPaleta.MULTIPLEXERS,
            List.of(new PropiedadSimple("0", "tLH", Propiedad.UNIDAD_TIME,
                            "Low->High delay [s]"),
                    new PropiedadSimple("0", "tHL", Propiedad.UNIDAD_TIME,
                            "High->Low delay [s]"),
                    new PropiedadSimple("1", "strength", Propiedad.UNIDAD_REAL, "Output strength")),
            List.of(new ConectorTemplate(TipoConector.ENTRADA, "in1"),
                    new ConectorTemplate(TipoConector.ENTRADA, "in0"),
                    new ConectorTemplate(TipoConector.ENTRADA, "sel"),
                    new ConectorTemplate(TipoConector.SALIDA, "out"))),
    //endregion
    ;
    final String nombre;
    final String pathImagen;
    final String claseModelica;
    final TabPaleta tabPaleta;
    final List<Propiedad> propiedades;
    final List<ConectorTemplate> conectoresConNombre;

    TipoPieza(String nombre, String pathImagen, String claseModelica, TabPaleta tabPaleta, List<Propiedad> propiedades,
              List<ConectorTemplate> conectoresConNombre) {
        this.nombre = nombre;
        this.pathImagen = pathImagen;
        this.claseModelica = claseModelica;
        this.tabPaleta = tabPaleta;
        this.propiedades = propiedades;
        this.conectoresConNombre = conectoresConNombre;
    }
}
