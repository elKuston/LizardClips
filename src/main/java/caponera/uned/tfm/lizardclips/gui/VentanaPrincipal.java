package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            JOptionPane.showMessageDialog(panelCircuito, e.getMessage(), "Error",
                    JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        });
    }

    public void setNombreCircuito(String nombreCircuito) {
        frame.setTitle("LizardClips - " + nombreCircuito);
    }

    private void setupLayout(int width, int height) {
        //Frame
        frame = new JFrame();
        frame.setIconImage(new ImageIcon(ImageUtils.MEDIA_BASE_FOLDER + "/and.png").getImage());
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
        //   Menu caponera.uned.tfm.lizardclips.modelica
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

        for (TipoPieza tp : TipoPieza.values()) {
            JButton b =
                    new JButton("", ImageUtils.cargarImagenEscalada(tp.getPathImagen(), 100, 100));
            b.setToolTipText(tp.getNombre());
            b.addActionListener(
                    e -> controladorCircuito.generarPieza(tp, tp.getConectoresEntradaMin()));
            lateral.add(b);
        }

        JToggleButton borrar = new JToggleButton("Borrar");
        borrar.addActionListener(e -> panelCircuito.toggleDeleteMode());
        lateral.add(borrar);


        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
