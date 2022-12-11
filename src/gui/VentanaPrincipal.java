package gui;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal {
    private JFrame frame;

    public VentanaPrincipal(int width, int height) {
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("LizardClips");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImagePanel res1 = new ImagePanel("media/res.png", new Point(20, 20), 200, 100);
        ImagePanel res2 = new ImagePanel("media/res.png", new Point(500, 100), 200, 100);
        PanelCircuito panel = new PanelCircuito();
        panel.addImagePanel(new Point(20, 20), res1);
        panel.addImagePanel(new Point(400, 200), res2);
        frame.add(panel);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
