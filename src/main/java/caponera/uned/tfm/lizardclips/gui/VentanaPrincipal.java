package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.constant.TabPaleta;
import caponera.uned.tfm.lizardclips.constant.TipoPieza;
import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import caponera.uned.tfm.lizardclips.utils.I18NUtils;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class VentanaPrincipal {
    private static final int ANCHO_BOTONES_LATERALES = 50;
    private static final int ALTO_BOTONES_LATERALES = 50;

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
        frame.setTitle(I18NUtils.getString("app_name") + " - " + nombreCircuito);
    }

    private void setupLayout(int width, int height) {
        //Frame
        frame = new JFrame();
        frame.setIconImage(
                ImageUtils.cargarImageIcon(ImageUtils.MEDIA_BASE_FOLDER + "/lizardclips.png")
                          .getImage());
        frame.setSize(width, height);
        setNombreCircuito(I18NUtils.getString("untitled_circuit"));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Menu superior
        JMenuBar barraSuperior = new JMenuBar();
        //   Menu circuito
        JMenu menuCircuito = new JMenu(I18NUtils.getString("menu_circuito_title"));

        JMenuItem menuItemCircuitoNuevo =
                new JMenuItem(I18NUtils.getString("menuitem_new_circuit"));
        menuItemCircuitoNuevo.addActionListener(e -> controladorCircuito.nuevoCircuito());
        menuCircuito.add(menuItemCircuitoNuevo);

        JMenuItem menuItemCircuitoCargar =
                new JMenuItem(I18NUtils.getString("menuitem_load_circuit"));
        menuItemCircuitoCargar.addActionListener(e -> controladorCircuito.cargar());
        menuCircuito.add(menuItemCircuitoCargar);

        JMenuItem menuItemCircuitoGuardar =
                new JMenuItem(I18NUtils.getString("menuitem_save_circuit"));
        menuItemCircuitoGuardar.addActionListener(e -> controladorCircuito.guardar());
        menuCircuito.add(menuItemCircuitoGuardar);

        JMenuItem menuItemCircuitoGuardarComo =
                new JMenuItem(I18NUtils.getString("menuitem_save_circuit_as"));
        menuItemCircuitoGuardarComo.addActionListener(e -> controladorCircuito.guardar_como());
        menuCircuito.add(menuItemCircuitoGuardarComo);

        barraSuperior.add(menuCircuito);
        //   Menu modelica
        JMenu menuModelica = new JMenu(I18NUtils.getString("menu_modelica_title"));
        JMenuItem menuItemModelicaExportar =
                new JMenuItem(I18NUtils.getString("menuitem_export_modelica"));
        menuItemModelicaExportar.addActionListener(e -> controladorCircuito.exportarCodigo());
        menuModelica.add(menuItemModelicaExportar);

        JMenuItem menuItemModelicaVisualizar =
                new JMenuItem(I18NUtils.getString("menuitem_view_modelica"));
        menuItemModelicaVisualizar.addActionListener(e -> controladorCircuito.verCodigo());
        menuModelica.add(menuItemModelicaVisualizar);

        barraSuperior.add(menuModelica);

        //   Menu vista
        JMenu menuVista = new JMenu(I18NUtils.getString("menu_view_title"));
        JMenuItem menuItemVistaToggleNombres =
                new JMenuItem(I18NUtils.getString("menuitem_show_hide_piece_names"));
        menuItemVistaToggleNombres.addActionListener(
                e -> controladorCircuito.toggleNombresPiezas());
        menuVista.add(menuItemVistaToggleNombres);

        JMenuItem menuItemVistaToggleNombresPines =
                new JMenuItem(I18NUtils.getString("menuitem_show_hide_pin_names"));
        menuItemVistaToggleNombresPines.addActionListener(
                e -> controladorCircuito.toggleNombresPines());
        menuVista.add(menuItemVistaToggleNombresPines);

        barraSuperior.add(menuVista);

        frame.setJMenuBar(barraSuperior);


        //Panel
        frame.add(panelCircuito, BorderLayout.CENTER);


        //Sidebar
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY));

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.LEFT, JTabbedPane.SCROLL_TAB_LAYOUT);
        for (TabPaleta tab : TabPaleta.values()) {
            JPanel tabContent = new JPanel();
            tabContent.setLayout(new BoxLayout(tabContent, BoxLayout.Y_AXIS));
            for (TipoPieza tp : TipoPieza.values()) {
                if (tp.getTabPaleta().equals(tab)) {
                    JButton b = new JButton("",
                            ImageUtils.cargarImageneEscaladaPreserveRatio(tp.getPathImagen(),
                                    ANCHO_BOTONES_LATERALES, ALTO_BOTONES_LATERALES));
                    b.setToolTipText(tp.getNombre());
                    b.addActionListener(e -> controladorCircuito.generarPieza(tp));
                    tabContent.add(b);
                }
            }

            tabs.add(tab.getNombre(), tabContent);
        }

        lateral.add(tabs);

        JToggleButton borrar = new JToggleButton("", ImageUtils.cargarImageneEscaladaPreserveRatio(
                ImageUtils.MEDIA_BASE_FOLDER + "/trash.png", ANCHO_BOTONES_LATERALES,
                ALTO_BOTONES_LATERALES));
        borrar.addActionListener(e -> panelCircuito.toggleDeleteMode());
        lateral.add(borrar);


        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
