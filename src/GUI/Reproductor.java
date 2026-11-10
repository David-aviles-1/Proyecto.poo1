package GUI;

import control.ControladorCanciones;
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
    
    private final ControladorCanciones ctrl;
    private final BarraReproduccion barra;
    private final PanelAlbumes panelAlbumes;
    private final PanelArtistas panelArtistas;
    private final PanelListas panelListas;
    private final PanelReproduciendo panelReproduciendo;
    
    private final BotonMenu btnAlbumes = new BotonMenu("Álbumes");
    private final BotonMenu btnArtistas = new BotonMenu("Artistas");
    private final BotonMenu btnListas = new BotonMenu("Listas de reproducción");

    private String ultimaVista = "albumes";
    
    public Reproductor() {
        this(new ControladorCanciones());
    }

    public Reproductor(ControladorCanciones ctrl) {
        this.ctrl = ctrl;
        setTitle("Reproductor");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1040, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 20)); 
        setLayout(new BorderLayout());

        barra = new BarraReproduccion(ctrl);
        panelAlbumes = new PanelAlbumes(ctrl, barra::reproducir);
        panelArtistas = new PanelArtistas(ctrl, barra::reproducir);
        panelListas = new PanelListas(ctrl, barra::reproducir);
        panelReproduciendo = new PanelReproduciendo(() -> mostrar(ultimaVista));

        centro.setBackground(new Color(18, 18, 20));
        centro.add(panelAlbumes, "albumes");
        centro.add(panelArtistas, "artistas");
        centro.add(panelListas, "listas");
        centro.add(panelReproduciendo, "reproduciendo");

        add(crearMenuLateral(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
        
        add(barra, BorderLayout.SOUTH);

        btnAlbumes.addActionListener(e -> mostrar("albumes"));
        btnArtistas.addActionListener(e -> mostrar("artistas"));
        btnListas.addActionListener(e -> mostrar("listas"));

        barra.setAlCambiarCancion(panelReproduciendo::mostrar);
        barra.setAlAbrirDetalle(this::mostrarDetalle);
        
        mostrar("albumes");
    }
    
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(11, 11, 12)); 
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = new JLabel("Mi Música");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);
        menu.add(Box.createVerticalStrut(30));

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
        salir.setForeground(new Color(160, 160, 167));
        salir.addActionListener(e -> {
            new loginFrame().setVisible(true);
            dispose();
        });
        menu.add(salir);
        
        return menu;
    }

    private void mostrar(String vista) {
        ultimaVista = vista;
        switch (vista) {
            case "albumes":
                panelAlbumes.actualizar();
                break;
            case "artistas":
                panelArtistas.actualizar();
                break;
            default:
                panelListas.actualizar();
                break;
        }
        cartas.show(centro, vista);
        btnAlbumes.setActivo(vista.equals("albumes"));
        btnArtistas.setActivo(vista.equals("artistas"));
        btnListas.setActivo(vista.equals("listas"));
    }

    private void mostrarDetalle() {
        cartas.show(centro, "reproduciendo");
        btnAlbumes.setActivo(false);
        btnArtistas.setActivo(false);
        btnListas.setActivo(false);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Reproductor().setVisible(true);
        });
    }
}