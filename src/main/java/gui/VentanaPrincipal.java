package gui;

import controlador.ControladorCircuito;
import lombok.Getter;
import utils.ImageUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class VentanaPrincipal {
    @Getter
    private JFrame frame;
    private ControladorCircuito controladorCircuito;
    private PanelCircuito panelCircuito;

    public VentanaPrincipal(int width, int height, ControladorCircuito controlador, PanelCircuito panelCircuito) {
        this.controladorCircuito = controlador;
        controladorCircuito.setVentanaPrincipal(this);
        this.panelCircuito = panelCircuito;
        setupLayout(width, height);
    }

    public void setNombreCircuito(String nombreCircuito) {
        frame.setTitle("LizardClips - " + nombreCircuito);
    }

    private void setupLayout(int width, int height) {
        //Frame
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("LizardClips - Circuito sin nombre");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //Panel
        frame.add(panelCircuito, BorderLayout.CENTER);

        //Sidebar
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY));

        JButton b = new JButton("", ImageUtils.cargarImagenEscalada("media/res.png", 100, 50));
        b.addActionListener(e -> controladorCircuito.generarResistor());
        lateral.add(b);

        JButton and = new JButton("", ImageUtils.cargarImagenEscalada("media/and.png", 100, 50));
        and.addActionListener(e -> controladorCircuito.generarAnd());
        lateral.add(and);

        JToggleButton borrar = new JToggleButton("Borrar");
        borrar.addActionListener(e -> panelCircuito.toggleDeleteMode());
        lateral.add(borrar);

        JButton guardar = new JButton("Guardar");
        guardar.addActionListener(e -> controladorCircuito.guardar());
        lateral.add(guardar);

        JButton cargar = new JButton("Cargar");
        cargar.addActionListener(e -> controladorCircuito.cargar());
        lateral.add(cargar);


        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
