package gui;

import utils.ImageUtils;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal {
    private JFrame frame;

    public VentanaPrincipal(int width, int height) {
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("LizardClips");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setupLayout();
    }

    private void setupLayout() {
        PanelCircuito panel = new PanelCircuito();

        //Sidebar
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        JButton b = new JButton("", ImageUtils.cargarImagenEscalada("media/res.png", 100, 50));
        b.addActionListener(e -> panel.addImagePanelByDragging(new ImagePanel("media/res.png", 200, 100)));
        JButton and = new JButton("", ImageUtils.cargarImagenEscalada("media/and.png", 100, 50));
        and.addActionListener(e -> panel.addImagePanelByDragging(new ImagePanel("media/and.png", 200, 100)));
        lateral.add(and);
        lateral.add(b);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(lateral, BorderLayout.WEST);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
