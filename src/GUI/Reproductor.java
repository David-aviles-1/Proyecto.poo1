package GUI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    
    private final JButton btnAlbumes = new JButton("Albumes");
    private final JButton btnArtistas = new JButton("Artistas");
    private final JButton btnListas = new JButton("Listas de reproduccion");
    
    private String ultimaVista = "albumes";
    
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

        centro.setBackground(Color.DARK_GRAY);
        centro.add(panelAlbumes, "albumes");
        centro.add(panelArtistas, "artistas");
        centro.add(panelListas, "listas");
        centro.add(panelReproduciendo, "reproduciendo");

        add(crearMenuLateral(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
        
        barra.setPreferredSize(new Dimension(1100, 80));
        barra.setBackground(Color.BLACK);
        add(barra, BorderLayout.SOUTH);

        btnAlbumes.addActionListener(e -> mostrar("albumes"));
        btnArtistas.addActionListener(e -> mostrar("artistas"));
        btnListas.addActionListener(e -> mostrar("listas"));
        
        mostrar("albumes");
    }
    
    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Color.GRAY);
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = new JLabel("Mi Musica");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);
        menu.add(Box.createVerticalStrut(30));

        for (JButton b : new JButton[]{btnAlbumes, btnArtistas, btnListas}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setPreferredSize(new Dimension(180, 44));
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            menu.add(b);
            menu.add(Box.createVerticalStrut(6));
        }

        menu.add(Box.createVerticalGlue());

        JButton salir = new JButton("Cerrar sesion");
        salir.setAlignmentX(Component.LEFT_ALIGNMENT);
        salir.setPreferredSize(new Dimension(180, 44));
        salir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        salir.addActionListener(e -> {
            dispose();
        });
        menu.add(salir);
        return menu;
    }

    private void mostrar(String vista) {
        ultimaVista = vista;
        cartas.show(centro, vista);
    }
}