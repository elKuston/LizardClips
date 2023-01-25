package gui;

import controlador.ControladorCircuito;
import lombok.Getter;
import utils.ImageUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

        //Menu superior
        JMenuBar barraSuperior = new JMenuBar();
        //   Menu circuito
        JMenu menuCircuito = new JMenu("Circuito");

        JMenuItem menuItemCircuitoNuevo = new JMenuItem("Nuevo circuito");
        menuItemCircuitoNuevo.addActionListener(e -> controladorCircuito.nuevoCircuito());
        menuCircuito.add(menuItemCircuitoNuevo);

        JMenuItem menuItemCircuitoCargar = new JMenuItem("Cargar circuito");
        menuItemCircuitoCargar.addActionListener(e -> controladorCircuito.cargar());
        menuCircuito.add(menuItemCircuitoCargar);

        JMenuItem menuItemCircuitoGuardar = new JMenuItem("Guardar circuito");
        menuItemCircuitoGuardar.addActionListener(e -> controladorCircuito.guardar());
        menuCircuito.add(menuItemCircuitoGuardar);

        barraSuperior.add(menuCircuito);
        //   Menu modelica
        JMenu menuModelica = new JMenu("Modelica");
        JMenuItem menuItemModelicaExportar = new JMenuItem("Exportar código Modelica");
        menuItemModelicaExportar.addActionListener(e -> controladorCircuito.exportarCodigo());
        menuModelica.add(menuItemModelicaExportar);

        barraSuperior.add(menuModelica);
        frame.setJMenuBar(barraSuperior);


        //Panel
        frame.add(panelCircuito, BorderLayout.CENTER);


        //Sidebar
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY));

        JButton b = new JButton("",
                ImageUtils.cargarImagenEscalada(ImageUtils.MEDIA_BASE_FOLDER + "/or.png", 100,
                        100));
        b.addActionListener(e -> controladorCircuito.generarOr());
        lateral.add(b);

        JButton and = new JButton("",
                ImageUtils.cargarImagenEscalada(ImageUtils.MEDIA_BASE_FOLDER + "/and.png", 100,
                        50));
        and.addActionListener(e -> controladorCircuito.generarAnd());
        lateral.add(and);

        JButton set = new JButton("",
                ImageUtils.cargarImagenEscalada(ImageUtils.MEDIA_BASE_FOLDER + "/set.png", 100,
                        50));
        set.addActionListener(e -> controladorCircuito.generarSet());
        lateral.add(set);

        JToggleButton borrar = new JToggleButton("Borrar");
        borrar.addActionListener(e -> panelCircuito.toggleDeleteMode());
        lateral.add(borrar);


        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
