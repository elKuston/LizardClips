package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.modelica.ModelicaGenerator;
import caponera.uned.tfm.lizardclips.modelo.Pieza;
import caponera.uned.tfm.lizardclips.modelo.Propiedad;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSeleccionMultiple;
import caponera.uned.tfm.lizardclips.modelo.PropiedadSimple;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorPropiedadesPieza extends JComponent {
    private Pieza pieza;
    private Map<Propiedad, JComponent> mapaPropiedades;

    private JTextField tfNombrePieza;

    public EditorPropiedadesPieza(Pieza pieza) {
        this.pieza = pieza;
        mapaPropiedades = new HashMap<>();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        tfNombrePieza = new JTextField(ModelicaGenerator.nombrePieza(pieza));
        addPropiedad("nombre", null, tfNombrePieza);


        List<Propiedad> propiedades = pieza.getTipoPieza().getPropiedades();
        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            JComponent rightSide;
            if (p instanceof PropiedadSeleccionMultiple) {
                List<String> valoresPosibles =
                        ((PropiedadSeleccionMultiple) p).getValoresPosibles();
                rightSide = new JComboBox<String>(valoresPosibles.toArray(new String[0]));
                ((JComboBox) rightSide).setSelectedIndex(
                        valoresPosibles.indexOf(pieza.getValoresPropiedades()[i]));

            } else if (p instanceof PropiedadSimple) {
                rightSide = new JTextField(pieza.getValoresPropiedades()[i]);
            } else {
                throw new RuntimeException("Propiedad de tipo desconocido: " + p);
            }

            mapaPropiedades.put(p, rightSide);

            addPropiedad(p.getNombre(), p.getTooltipDescription(), rightSide);
        }

    }

    private void addPropiedad(String name, String tooltipDescription, JComponent rightSide) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        JLabel nameLabel = new JLabel(name);
        if (tooltipDescription != null) {
            nameLabel.setToolTipText(tooltipDescription);
        }
        row.add(nameLabel);
        row.add(rightSide);
        add(row);
    }

    public void actualizarValorPropiedades() {
        String nuevoNombre = tfNombrePieza.getText();
        if (nuevoNombre != null && !nuevoNombre.isEmpty() &&
                !nuevoNombre.equals(ModelicaGenerator.nombrePieza(pieza))) {
            pieza.getCircuito().getControlador().renombrarPieza(pieza, nuevoNombre);
        }
        List<Propiedad> propiedades = pieza.getTipoPieza().getPropiedades();
        for (int i = 0; i < propiedades.size(); i++) {
            Propiedad p = propiedades.get(i);
            String nuevoValor;
            if (p instanceof PropiedadSeleccionMultiple) {
                nuevoValor = ((PropiedadSeleccionMultiple) p).getValoresPosibles()
                                                             .get(((JComboBox) mapaPropiedades.get(
                                                                     p)).getSelectedIndex());
            } else if (p instanceof PropiedadSimple) {
                nuevoValor = ((JTextField) mapaPropiedades.get(p)).getText();
            } else {
                throw new RuntimeException("Propiedad de tipo desconocido: " + p);
            }
            pieza.setValorPropiedad(i, nuevoValor);
        }
    }
}
