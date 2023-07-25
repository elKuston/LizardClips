package caponera.uned.tfm.lizardclips.gui;

import caponera.uned.tfm.lizardclips.controlador.ControladorCircuito;
import caponera.uned.tfm.lizardclips.modelo.Circuito;
import caponera.uned.tfm.lizardclips.utils.I18NUtils;
import caponera.uned.tfm.lizardclips.utils.ImageUtils;
import lombok.Getter;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class SelectorCircuito extends JComponent implements ItemListener {
    private static final int IMAGEVIEW_W = 300, IMAGEVIEW_H = 300;
    ControladorCircuito controladorCircuito;
    List<Circuito> circuitos;
    java.awt.List listaCircuitos;
    JLabel imageView;

    @Getter
    private Circuito circuitoSeleccionado;

    public SelectorCircuito(ControladorCircuito controladorCircuito, List<Circuito> circuitos) {
        this.controladorCircuito = controladorCircuito;
        this.circuitos = circuitos;
        setLayout(new FlowLayout());
        //Lista circuitos
        JPanel contenedorListaCircuitos = new JPanel();
        contenedorListaCircuitos.setLayout(
                new BoxLayout(contenedorListaCircuitos, BoxLayout.Y_AXIS));
        contenedorListaCircuitos.add(new JLabel(I18NUtils.getString("select_circuit_to_load_it")));
        listaCircuitos = new java.awt.List(circuitos.size(), false);
        for (Circuito c : circuitos) {
            listaCircuitos.add(c.getNombre());
        }
        listaCircuitos.addItemListener(this);
        JScrollPane scrollListaCircuitos = new JScrollPane(listaCircuitos);
        scrollListaCircuitos.setPreferredSize(new Dimension(IMAGEVIEW_W / 2, IMAGEVIEW_H));
        contenedorListaCircuitos.add(scrollListaCircuitos);
        this.add(contenedorListaCircuitos);
        JPanel contenedorImageView = new JPanel();
        contenedorImageView.setLayout(new BoxLayout(contenedorImageView, BoxLayout.Y_AXIS));
        contenedorImageView.add(new JLabel(I18NUtils.getString("circuit_preview")));
        imageView = new JLabel();
        imageView.setIcon(
                ImageUtils.cargarImagenEscalada(ImageUtils.MEDIA_BASE_FOLDER + "/lizardclips.png",
                        IMAGEVIEW_W, IMAGEVIEW_H));
        imageView.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        contenedorImageView.add(imageView);
        this.add(contenedorImageView);
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        circuitoSeleccionado = circuitos.get(listaCircuitos.getSelectedIndex());
        imageView.setIcon(ImageUtils.rescalarImagenPreserveRatio(
                ImageUtils.imageIconFromBytes(circuitoSeleccionado.getThumbnail()), IMAGEVIEW_W,
                IMAGEVIEW_H));
        repaint();
    }
}
