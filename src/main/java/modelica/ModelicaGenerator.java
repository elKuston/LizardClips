package modelica;

import constant.TipoConector;
import modelo.Circuito;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;

import java.util.StringJoiner;

public class ModelicaGenerator {
    public static final String IMPORT_BASIC = "B";
    public static final String IMPORT_LOGIC = "L";
    public static final String IMPORT_DIGITAL = "D";
    public static final String IMPORT_SOURCES = "S";
    private static final String DIGITAL = "Modelica.Electrical.Digital";
    private static final String BASIC = "Modelica.Electrical.Digital.Basic";
    private static final String LOGIC = "Modelica.Electrical.Digital.Interfaces.Logic";
    private static final String SOURCES = "Modelica.Electrical.Digital.Sources";

    public static String generarCodigoModelica(Circuito circuito) {
        StringBuilder codigoModelica = new StringBuilder();
        codigoModelica.append("model ").append(modelName(circuito)).append("\n\t");
        codigoModelica.append(declaraciones(circuito));
        codigoModelica.append("equation\n\t").append(connect(circuito));
        codigoModelica.append("end ").append(modelName(circuito)).append(";");

        return codigoModelica.toString();
    }

    private static String declaraciones(Circuito circuito) {
        StringJoiner sj = new StringJoiner(";\n\t");

        sj.add("import " + IMPORT_DIGITAL + " = " + DIGITAL);
        sj.add("import " + IMPORT_BASIC + " = " + BASIC);
        sj.add("import " + IMPORT_LOGIC + " = " + LOGIC);
        sj.add("import " + IMPORT_SOURCES + " = " + SOURCES);
        for (Pieza p : circuito.getComponentes()) {
            if (p.getClaseModelica().startsWith(IMPORT_SOURCES + ".")) {
                sj.add("parameter " + IMPORT_LOGIC + " " + nombreFuente(p) + " = " + IMPORT_LOGIC +
                        ".'0'");
                sj.add(p.getClaseModelica() + " " + nombrePieza(p) + "(x=" + nombreFuente(p) + ")");
            } else {
                sj.add(p.getClaseModelica() + " " + nombrePieza(p) + "( n=" +
                        nConectoresEntrada(p) + ")");
            }
        }

        sj.add("\n");
        return sj.toString();
    }

    private static String nombreFuente(Pieza p) {
        return "src_" + nombrePieza(p);
    }

    private static long nConectoresEntrada(Pieza p) {
        return p.getConectores().stream()
                .filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA)).count();
    }

    private static String nombrePieza(Pieza p) {
        String nombre;
        String clase = p.getClaseModelica().split("\\.")[1];
        if (p.getIdPieza() == null) {
            nombre = "p_" + clase + "_" + p.hashCode();
        } else {
            nombre = "p_" + clase + "_" + p.getIdPieza();
        }
        return nombre;
    }

    private static String connect(Circuito circuito) {
        StringJoiner sj = new StringJoiner(";\n\t");
        for (int i = 0; i < circuito.getConexiones().size(); i++) {
            Conexion c = circuito.getConexiones().get(i);
            sj.add("connect(" + nombreConector(c.getOrigen()) + ", " +
                    nombreConector(c.getDestino()) + "[" + (i + 1) + "])");
        }
        sj.add("\n");
        return sj.toString();
    }

    private static String nombreConector(Conector conector) {
        return nombrePieza(conector.getPieza()) + "." +
                (conector.getTipoConector().equals(TipoConector.ENTRADA) ? "x" : "y");
    }

    private static String modelName(Circuito circuito) {
        String modelName = "CircuitoAutogenerado";
        if (circuito.getNombre() != null && !circuito.getNombre().isBlank()) {
            modelName = circuito.getNombre();
        }

        return modelName;
    }
}
