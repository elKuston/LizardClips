package caponera.uned.tfm.lizardclips.modelo;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;

public class PropiedadLogic extends PropiedadSeleccionMultiple {


    public PropiedadLogic(String nombre, String valor, String tooltipDescription) {
        super(valor, nombre, PropiedadSeleccionMultiple.SELECCION_MULTIPLE_LOGICAL,
                PropiedadSeleccionMultiple.PREFIX_SELECCION_MULTIPLE_LOCICAL,
                ModelicaGenerator.LOGIC, tooltipDescription);
    }

    public PropiedadLogic(String nombre, String tooltipDescription) {
        this(nombre, "'0'", tooltipDescription);
    }
}
