package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class BarraReproduccion extends JPanel {

    public BarraReproduccion() {
        setPreferredSize(new Dimension(1100, 90));
        setBackground(new Color(24, 24, 24)); 
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelInfo.setOpaque(false); 
        
        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(60, 60));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.GRAY);
        
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Sin reproducción");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel lblArtista = new JLabel("Elige una canción");
        lblArtista.setForeground(Color.GRAY);
        lblArtista.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        textos.add(lblTitulo);
        textos.add(lblArtista);
        
        panelInfo.add(lblFoto);
        panelInfo.add(textos);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setOpaque(false);
        
        JPanel botonesPlay = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        botonesPlay.setOpaque(false);
        
        BotonIcono btnAnterior = new BotonIcono(BotonIcono.ANTERIOR, false, 34);
        BotonIcono btnPlay = new BotonIcono(BotonIcono.PLAY, true, 46);
        BotonIcono btnSiguiente = new BotonIcono(BotonIcono.SIGUIENTE, false, 34);
        
        botonesPlay.add(btnAnterior);
        botonesPlay.add(btnPlay);
        botonesPlay.add(btnSiguiente);
        
        JPanel panelTiempo = new JPanel(new BorderLayout(10, 0));
        panelTiempo.setOpaque(false);
        
        JLabel lblTiempoActual = new JLabel("0:00");
        lblTiempoActual.setForeground(Color.GRAY);
        
        JLabel lblTiempoTotal = new JLabel("0:00");
        lblTiempoTotal.setForeground(Color.GRAY);
        
        JSlider sliderTiempo = new JSlider(0, 100, 0);
        sliderTiempo.setOpaque(false);
        
        panelTiempo.add(lblTiempoActual, BorderLayout.WEST);
        panelTiempo.add(sliderTiempo, BorderLayout.CENTER);
        panelTiempo.add(lblTiempoTotal, BorderLayout.EAST);
        
        panelControles.add(botonesPlay);
        panelControles.add(panelTiempo);

        JPanel panelVolumen = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        panelVolumen.setOpaque(false);
        
        JButton btnLetra = new JButton("Letra");
        btnLetra.setBackground(new Color(40, 40, 40));
        btnLetra.setForeground(Color.WHITE);
        btnLetra.setFocusPainted(false);
        
        JSlider sliderVol = new JSlider(0, 100, 50);
        sliderVol.setOpaque(false);
        sliderVol.setPreferredSize(new Dimension(100, 20));
        
        panelVolumen.add(btnLetra);
        panelVolumen.add(sliderVol);

        add(panelInfo, BorderLayout.WEST);
        add(panelControles, BorderLayout.CENTER);
        add(panelVolumen, BorderLayout.EAST);
    }
}