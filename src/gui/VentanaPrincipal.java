package gui;

import componentes.Conector;
import constant.TipoConector;
import utils.ImageUtils;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.util.List;

public class VentanaPrincipal {
    private JFrame frame;

    public VentanaPrincipal(int width, int height) {
        setupLayout(width, height);
    }

    private void setupLayout(int width, int height) {
        //Frame
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("LizardClips");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        //Panel
        PanelCircuito panel = new PanelCircuito();
        frame.add(panel, BorderLayout.CENTER);

        //Sidebar
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));

        JButton b = new JButton("", ImageUtils.cargarImagenEscalada("media/res.png", 100, 50));
        b.addActionListener(
                e -> panel.addImagePanelByDragging(new PanelPieza("media/res.png", 200, 100,
                        List.of(new Conector(0, 0.5, TipoConector.ENTRADA),
                                new Conector(1, 0.5, TipoConector.SALIDA)))));
        lateral.add(b);

        JButton and = new JButton("", ImageUtils.cargarImagenEscalada("media/and.png", 100, 50));
        and.addActionListener(
                e -> panel.addImagePanelByDragging(new PanelPieza("media/and.png", 200, 100,
                        List.of(new Conector(0, 0.25, TipoConector.ENTRADA),
                                new Conector(0, 0.75, TipoConector.ENTRADA),
                                new Conector(1, 0.5, TipoConector.SALIDA)))));
        lateral.add(and);

        JToggleButton borrar = new JToggleButton("Borrar");
        borrar.addActionListener(e -> panel.toggleDeleteMode());
        lateral.add(borrar);

        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
