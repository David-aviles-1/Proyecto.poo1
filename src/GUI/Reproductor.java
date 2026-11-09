package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
/**
 *
 * @author oswaldo
 */

public class Reproductor extends JFrame {

    private final CardLayout cartas = new CardLayout();
    private final JPanel centro = new JPanel(cartas);
    
    private final JPanel barra;
    private final JPanel panelAlbumes;
    private final JPanel panelArtistas;
    private final JPanel panelListas;
    private final JPanel panelReproduciendo;
    
   
    private final BotonMenu btnAlbumes = new BotonMenu("Álbumes");
    private final BotonMenu btnArtistas = new BotonMenu("Artistas");
    private final BotonMenu btnListas = new BotonMenu("Listas de reproducción");
    
    public Reproductor() {
        setTitle("Reproductor");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1040, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        barra = new JPanel();
        panelAlbumes = new JPanel();
        panelArtistas = new JPanel();
        panelListas = new JPanel();
        panelReproduciendo = new JPanel();

        configurarPanelProvisional(panelAlbumes, "Vista de Álbumes");
        configurarPanelProvisional(panelArtistas, "Vista de Artistas");
        configurarPanelProvisional(panelListas, "Vista de Listas de Reproducción");
        configurarPanelProvisional(panelReproduciendo, "Vista de Reproducción (Detalle)");

        centro.setBackground(Color.DARK_GRAY);
        centro.add(panelAlbumes, "albumes");
        centro.add(panelArtistas, "artistas");
        centro.add(panelListas, "listas");
        centro.add(panelReproduciendo, "reproduciendo");

        add(crearMenuLateral(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
        
        barra.setPreferredSize(new Dimension(1100, 80));
        barra.setBackground(Color.BLACK);
        barra.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 25));
        JLabel lblControles = new JLabel("[Aquí irán los botones de Play, Pausa, Siguiente]");
        lblControles.setForeground(Color.WHITE);
        barra.add(lblControles);
        add(barra, BorderLayout.SOUTH);

        btnAlbumes.addActionListener(e -> mostrar("albumes"));
        btnArtistas.addActionListener(e -> mostrar("artistas"));
        btnListas.addActionListener(e -> mostrar("listas"));
        
        mostrar("albumes");
    }
    
    private void configurarPanelProvisional(JPanel panel, String texto) {
        panel.setBackground(Color.DARK_GRAY);
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 24));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        panel.add(label);
    }
    
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        // Fondo oscuro para el menú lateral
        menu.setBackground(new Color(18, 18, 18)); 
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = new JLabel("Mi Música");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);
        menu.add(Box.createVerticalStrut(30));

        // AQUÍ USAMOS BotonMenu en el arreglo
        for (BotonMenu b : new BotonMenu[]{btnAlbumes, btnArtistas, btnListas}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setPreferredSize(new Dimension(180, 44));
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            menu.add(b);
            menu.add(Box.createVerticalStrut(6));
        }

        menu.add(Box.createVerticalGlue());

       
        BotonMenu salir = new BotonMenu("Cerrar sesión");
        salir.setAlignmentX(Component.LEFT_ALIGNMENT);
        salir.setPreferredSize(new Dimension(180, 44));
        salir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        salir.addActionListener(e -> dispose());
        menu.add(salir);
        
        return menu;
    }

    private void mostrar(String vista) {
        cartas.show(centro, vista);
        
        
        btnAlbumes.setActivo(vista.equals("albumes"));
        btnArtistas.setActivo(vista.equals("artistas"));
        btnListas.setActivo(vista.equals("listas"));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Reproductor().setVisible(true);
        });
    }
}