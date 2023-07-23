package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.utils.I18NUtils;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class VentanaVisualizarCodigo extends JFrame {
    public VentanaVisualizarCodigo(String codigo, int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        setSize(width, height);
        setTitle(I18NUtils.getString("code_viewer"));
        setIconImage(ImageUtils.cargarImageIcon(ImageUtils.MEDIA_BASE_FOLDER + "/lizardclips.png")
                               .getImage());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        AbstractTokenMakerFactory atmf =
                (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/modelica",
                "caponera.uned.tfm.lizardclips.modelica.ModelicaTokenMaker");
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);

        textArea.setSyntaxEditingStyle("text/modelica");
        textArea.setEditable(false);
        textArea.setCodeFoldingEnabled(true);
        textArea.setText(codigo);
        panel.add(textArea);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);

    }
}
