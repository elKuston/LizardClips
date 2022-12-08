package gui;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal {
    private JFrame frame;

    public VentanaPrincipal(int width, int height){
        frame = new JFrame();
        frame.setSize(width,height);
        frame.setTitle("LizardClips");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(new ImagePanel("media/res.png", new Point(20,20), 200,200));
        frame.add(new ImagePanel("media/res.png", new Point(100,100), 200,200));
    }

    public void mostrar(){
        frame.setVisible(true);
    }
}
