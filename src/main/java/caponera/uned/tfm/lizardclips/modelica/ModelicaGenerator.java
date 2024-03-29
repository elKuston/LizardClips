package caponera.uned.tfm.lizardclips.modelica;

import caponera.uned.tfm.lizardclips.constant.TipoConector;
import caponera.uned.tfm.lizardclips.modelo.Circuito;
import caponera.uned.tfm.lizardclips.modelo.Conector;
import caponera.uned.tfm.lizardclips.modelo.Conexion;
import caponera.uned.tfm.lizardclips.modelo.Pieza;
import caponera.uned.tfm.lizardclips.modelo.Propiedad;
import caponera.uned.tfm.lizardclips.utils.Punto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

public class ModelicaGenerator {
    public static final String BASIC = "B";
    public static final String LOGIC = "L";
    public static final String DIGITAL = "D";
    public static final String SOURCES = "S";
    public static final String GATES = "G";
    public static final String SI = "SI";
    private static final String DIGITAL_IMPORT = "Modelica.Electrical.Digital";
    private static final String BASIC_IMPORT = "Modelica.Electrical.Digital.Basic";
    private static final String LOGIC_IMPORT = "Modelica.Electrical.Digital.Interfaces.Logic";
    private static final String SOURCES_IMPORT = "Modelica.Electrical.Digital.Sources";
    private static final String GATES_IMPORT = "Modelica.Electrical.Digital.Gates";
    private static final String SI_IMPORT = "Modelica.Units.SI";

    private static final double ESCALA_ANNOTATION = 0.3;

    public static String generarCodigoModelica(Circuito circuito) {
        StringBuilder codigoModelica = new StringBuilder();
        codigoModelica.append("model ").append(modelName(circuito)).append("\n\t");
        codigoModelica.append(imports()).append("\n\t");
        codigoModelica.append(declaraciones(circuito));
        codigoModelica.append("equation\n\t").append(connect(circuito)).append("\n\t");
        codigoModelica.append(generarAnotacionesConexiones(circuito)).append(";\n");
        codigoModelica.append("end ").append(modelName(circuito)).append(";");

        return codigoModelica.toString();
    }

    private static String imports() {
        StringJoiner sj = new StringJoiner(";\n\t");
        sj.add("import " + DIGITAL + " = " + DIGITAL_IMPORT);
        sj.add("import " + BASIC + " = " + BASIC_IMPORT);
        sj.add("import " + LOGIC + " = " + LOGIC_IMPORT);
        sj.add("import " + SOURCES + " = " + SOURCES_IMPORT);
        sj.add("import " + SI + " = " + SI_IMPORT);
        sj.add("import " + GATES + " = " + GATES_IMPORT);
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

    private static String generarAnotacionesConexiones(Circuito circuito) {
        StringJoiner sj = new StringJoiner(",\n\t\t");
        for (Conexion c : circuito.getConexiones()) {
            sj.add(generarAnotacion(c));
        }

        return String.format("annotation (Diagram(graphics={%s}))", sj);
    }

    private static String generarAnotacion(Conexion c) {
        StringJoiner sj = new StringJoiner(",");
        c.getPuntosManhattan().stream().map(p -> escalarPunto(p, true))
         .map(p -> String.format("{%d,%d}", p.getX(), p.getY())).forEachOrdered(sj::add);
        return String.format("Line(points={%s}, color={0,0,0})", sj);
    }

    private static Punto escalarPunto(Punto punto, boolean invertirY) {
        int multiplicadorY = invertirY ? -1 : 1;
        return new Punto((int) (punto.getX() * ESCALA_ANNOTATION),
                (int) (punto.getY() * ESCALA_ANNOTATION * multiplicadorY));
    }

    private static String generarAnotacion(Pieza p) {
        Punto pos = p.getPosicion();
        Punto bottom_left = new Punto(pos.getX(), pos.getY() + p.getHeight());
        Punto top_right = new Punto(pos.getX() + p.getWidth(), pos.getY());
        Punto bottom_left_scaled = escalarPunto(bottom_left, true);
        Punto top_right_scaled = escalarPunto(top_right, true);
        double offset_x = 0, offset_y = 0;

        switch (p.getRotacion()) {
            case ROT_0 -> {

            }
            case ROT_90 -> {
                offset_x = -p.getHeight() * ESCALA_ANNOTATION;
                //offset_x = -p.getWidth() * ESCALA_ANNOTATION;
            }
            case ROT_180 -> {
                offset_y = -p.getHeight() * ESCALA_ANNOTATION;
                offset_x = -p.getWidth() * ESCALA_ANNOTATION;
            }
            case ROT_270 -> {
                offset_y = -p.getHeight() * ESCALA_ANNOTATION;
            }
        }
        return String.format(
                "annotation (Placement(transformation(extent={{%d,%d},{%d,%d}}, rotation=%d, origin={%d,%d})))",
                (int) (0 + offset_x), //bottom left x
                (int) (-p.getHeight() * ESCALA_ANNOTATION - offset_y), //bottom left y
                (int) (p.getWidth() * ESCALA_ANNOTATION + offset_x), //top right x
                (int) (0 - offset_y), //top right y
                p.getRotacion().getAngulo(), //rotation
                (int) (pos.getX() * ESCALA_ANNOTATION),//origin x
                (int) (-pos.getY() * ESCALA_ANNOTATION));//origin y
    }

    private static String nombrePropiedad(Pieza p, Propiedad prop) {
        return nombrePieza(p) + "_" + prop.getNombre();
    }

    private static String generarAsignacionParametrosPieza(Pieza p) {
        boolean requiresN = p.getTipoPieza().getConectoresEntradaMin() !=
                p.getTipoPieza().getConectoresEntradaMax();
        List<Propiedad> propiedadesPieza = p.getTipoPieza().getPropiedades();
        String res = "";
        if (propiedadesPieza.size() > 0 || requiresN) {
            StringJoiner sj = new StringJoiner(", ");
            for (int i = 0; i < propiedadesPieza.size(); i++) {
                Propiedad prop = propiedadesPieza.get(i);
                sj.add(prop.getNombre() + " = " + nombrePropiedad(p, prop));
            }

            if (requiresN) {
                sj.add("n=" + p.getConectores().stream()
                               .filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA))
                               .count());
            }

            res = "(" + sj + ")";
        }
        return res;
    }

    private static List<String> generarDeclaracionPieza(Pieza p) {
        List<String> declaracion = new ArrayList<>();
        List<Propiedad> propiedadesPieza = p.getTipoPieza().getPropiedades();
        for (int i = 0; i < propiedadesPieza.size(); i++) {
            Propiedad prop = propiedadesPieza.get(i);
            declaracion.add(String.format("parameter %s %s = %s", prop.getUnidad(),
                    nombrePropiedad(p, prop), p.getValoresPropiedades()[i]));
        }

        declaracion.add(
                String.format("%s %s %s %s", p.getTipoPieza().getClaseModelica(), nombrePieza(p),
                        generarAsignacionParametrosPieza(p), generarAnotacion(p)));

        /*switch (p.getTipoPieza()) {

            case AND, NAND, OR, NOR, XOR, XNOR -> {
                declaracion.add(
                        String.format("%s %s (n=%d) %s", p.getTipoPieza().getClaseModelica(),
                                nombrePieza(p), nConectoresEntrada(p), generarAnotacion(p)));
            }
            case SET -> {
                declaracion.add(
                        String.format("parameter %s %s = %s.'0'", LOGIC, nombreFuente(p), LOGIC));
                declaracion.add(
                        String.format("%s %s (x=%s) %s", p.getTipoPieza().getClaseModelica(),
                                nombrePieza(p), nombreFuente(p), generarAnotacion(p)));
            }
            case NOT -> {
                declaracion.add(String.format("%s %s %s", p.getTipoPieza().getClaseModelica(),
                        nombrePieza(p), generarAnotacion(p)));
            }
            case DIGITAL_CLOCK -> {
                declaracion.add(String.format(
                        "%s %s (startTime=%s_startTime, period=%s_period,width=%s_width) %s",
                        p.getTipoPieza().getClaseModelica(), nombrePieza(p), nombrePieza(p),
                        nombrePieza(p), nombrePieza(p), generarAnotacion(p)));
            }
            default -> throw new RuntimeException("error_generating_component_code");
        }*/
        return declaracion;
    }

    private static String nombreFuente(Pieza p) {
        return "src_" + nombrePieza(p);
    }

    private static long nConectoresEntrada(Pieza p) {
        return p.getConectores().stream()
                .filter(c -> c.getTipoConector().equals(TipoConector.ENTRADA)).count();
    }

    public static String nombrePieza(Pieza p) {
        if (p.getNombrePieza() != null) {
            return p.getNombrePieza();
        }
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
