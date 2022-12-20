package gui;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

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
        b.addActionListener(e -> panel.addImagePanelByDragging(new PanelPieza("media/res.png", 200, 100)));
        lateral.add(b);

        JButton and = new JButton("", ImageUtils.cargarImagenEscalada("media/and.png", 100, 50));
        and.addActionListener(e -> panel.addImagePanelByDragging(new PanelPieza("media/and.png", 200, 100)));
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
