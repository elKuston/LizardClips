package modelica;

import constant.TipoConector;
import modelo.Circuito;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;

import java.util.StringJoiner;

public class ModelicaGenerator {
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
        String importsName = "B";
        sj.add("import " + importsName + " = Modelica.Electrical.Digital.Basic");

        for (Pieza p : circuito.getComponentes()) {
            sj.add(importsName + "." + p.getClaseModelica() + " " + nombrePieza(p) + "( n=" +
                    nConectoresEntrada(p) + ")");
        }

        sj.add("\n");
        return sj.toString();
    }

    private static long nConectoresEntrada(Pieza p) {
        return p.getConectores().stream()
                .filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA)).count();
    }

    private static String nombrePieza(Pieza p) {
        String nombre;
        if (p.getIdPieza() == null) {
            nombre = "p_" + p.getClaseModelica() + "_" + p.hashCode();
        } else {
            nombre = "p_" + p.getClaseModelica() + "_" + p.getIdPieza();
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
