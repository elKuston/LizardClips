package caponera.uned.tfm.lizardclips;

import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import caponera.uned.tfm.lizardclips.gui.PanelCircuito;
import caponera.uned.tfm.lizardclips.gui.VentanaPrincipal;
import caponera.uned.tfm.lizardclips.modelo.Circuito;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.Arrays;


public class Main {
    private static final String LOOK_AND_FEEL_PREFERIDO =
            "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel";

    public static void main(String[] args) {


        setLAF();

        Circuito circuito = new Circuito();
        PanelCircuito panelCircuito = new PanelCircuito();
        ControladorCircuito controlador = new ControladorCircuito(circuito, panelCircuito);

        VentanaPrincipal ventanaPrincipal =
                new VentanaPrincipal(1000, 800, controlador, panelCircuito);
        ventanaPrincipal.mostrar();

    }

    private static void setLAF() {
        try {
            if (Arrays.stream(UIManager.getInstalledLookAndFeels())
                      .map(UIManager.LookAndFeelInfo::getClassName)
                      .anyMatch(s -> s.equals(LOOK_AND_FEEL_PREFERIDO))) {
                UIManager.setLookAndFeel(LOOK_AND_FEEL_PREFERIDO);
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }
}
