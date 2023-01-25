package modelica;

import constant.TipoConector;
import modelo.Circuito;
import modelo.Conector;
import modelo.Conexion;
import modelo.Pieza;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class ModelicaGenerator {
    public static final String BASIC = "B";
    public static final String LOGIC = "L";
    public static final String DIGITAL = "D";
    public static final String SOURCES = "S";
    private static final String DIGITAL_IMPORT = "Modelica.Electrical.Digital";
    private static final String BASIC_IMPORT = "Modelica.Electrical.Digital.Basic";
    private static final String LOGIC_IMPORT = "Modelica.Electrical.Digital.Interfaces.Logic";
    private static final String SOURCES_IMPORT = "Modelica.Electrical.Digital.Sources";

    public static String generarCodigoModelica(Circuito circuito) {
        StringBuilder codigoModelica = new StringBuilder();
        codigoModelica.append("model ").append(modelName(circuito)).append("\n\t");
        codigoModelica.append(imports()).append("\n\t");
        codigoModelica.append(declaraciones(circuito));
        codigoModelica.append("equation\n\t").append(connect(circuito));
        codigoModelica.append("end ").append(modelName(circuito)).append(";");

        return codigoModelica.toString();
    }

    private static String imports() {
        StringJoiner sj = new StringJoiner(";\n\t");
        sj.add("import " + DIGITAL + " = " + DIGITAL_IMPORT);
        sj.add("import " + BASIC + " = " + BASIC_IMPORT);
        sj.add("import " + LOGIC + " = " + LOGIC_IMPORT);
        sj.add("import " + SOURCES + " = " + SOURCES_IMPORT);
        sj.add("\n");
        return sj.toString();
    }

    private static String declaraciones(Circuito circuito) {
        StringJoiner sj = new StringJoiner(";\n\t");
        for (Pieza p : circuito.getComponentes()) {
            List<String> lineasDeclaracion = generarDeclaracionPieza(p);
            for (String s : lineasDeclaracion) {
                sj.add(s);
            }
        }

        sj.add("\n");
        return sj.toString();
    }

    private static List<String> generarDeclaracionPieza(Pieza p) {
        List<String> declaracion = new ArrayList<>();
        switch (p.getTipoPieza()) {

            case AND, OR -> declaracion.add(
                    String.format("%s %s (n=%d)", p.getTipoPieza().getClaseModelica(),
                            nombrePieza(p), nConectoresEntrada(p)));
            case SET -> {
                declaracion.add(
                        String.format("parameter %s %s = %s.'0'", LOGIC, nombreFuente(p), LOGIC));
                declaracion.add(String.format("%s %s (x=%s)", p.getTipoPieza().getClaseModelica(),
                        nombrePieza(p), nombreFuente(p)));
            }
            case NOT -> {
                declaracion.add(String.format("%s %s", p.getTipoPieza().getClaseModelica(),
                        nombrePieza(p)));
            }
        }
        return declaracion;
    }

    private static String nombreFuente(Pieza p) {
        return "src_" + nombrePieza(p);
    }

    private static long nConectoresEntrada(Pieza p) {
        return p.getConectores().stream()
                .filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA)).count();
    }

    private static String nombrePieza(Pieza p) {
        String clase = p.getTipoPieza().getClaseModelica().split("\\.")[1];
        int posicion = p.getCircuito().getComponentes().indexOf(p) + 1;
        return clase.toLowerCase(Locale.ROOT) + "_" + posicion;
    }

    private static String connect(Circuito circuito) {
        StringJoiner sj = new StringJoiner(";\n\t");
        for (Conexion c : circuito.getConexiones()) {
            Conector salida =
                    c.getOrigen().getTipoConector().equals(TipoConector.SALIDA) ? c.getOrigen() :
                            c.getDestino();
            Conector entrada =
                    c.getOrigen().getTipoConector().equals(TipoConector.ENTRADA) ? c.getOrigen() :
                            c.getDestino();

            if (entrada.getPieza().getTipoPieza().getConectoresEntradaMin() == 1 &&
                    entrada.getPieza().getTipoPieza().getConectoresEntradaMax() == 1) {
                sj.add(String.format("connect(%s,%s)", nombreConector(salida),
                        nombreConector(entrada)));
            } else {
                //Posicion del conector de entrada dentro de la pieza
                int indiceEntrada = entrada.getPieza().getConectores().stream()
                                           .filter(con -> con.getTipoConector()
                                                             .equals(TipoConector.ENTRADA)).toList()
                                           .indexOf(entrada) + 1;
                sj.add(String.format("connect(%s,%s[%d])", nombreConector(salida),
                        nombreConector(entrada), indiceEntrada));
            }
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
