
package GUI;

/**
 *
 * @author oswaldo
 */


import java.awt.*;
import javax.swing.*;

public class AdminFrame extends JFrame {
    private final CardLayout cartas = new CardLayout();
    private final JPanel centro = new JPanel(cartas);

    private final JPanel panelTabla = new JPanel();
    private final JPanel panelAgregar = new JPanel();
    private final JPanel panelConsultar = new JPanel();
    private final JPanel panelModificar = new JPanel();
    private final JPanel panelEliminar = new JPanel();

    private final String[] vistas = {"tabla", "agregar", "consultar", "modificar", "eliminar"};
    private final String[] titulos = {"Canciones", "Agregar", "Consultar", "Modificar", "Eliminar"};
    private final JButton[] botones = new JButton[5]; 

    public AdminFrame() {
        setTitle("Mi Reproductor - Administrador");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1040, 660));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Color FONDO = new Color(240, 240, 240); 
        getContentPane().setBackground(FONDO);
        setLayout(new BorderLayout());

        configurarPanelProvisional(panelTabla, "Panel Tabla Canciones");
        configurarPanelProvisional(panelAgregar, "Panel Agregar");
        configurarPanelProvisional(panelConsultar, "Panel Consultar");
        configurarPanelProvisional(panelModificar, "Panel Modificar");
        configurarPanelProvisional(panelEliminar, "Panel Eliminar");

        centro.setBackground(FONDO);
        centro.add(panelTabla, "tabla");
        centro.add(panelAgregar, "agregar");
        centro.add(panelConsultar, "consultar");
        centro.add(panelModificar, "modificar");
        centro.add(panelEliminar, "eliminar");

        add(crearMenuLateral(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
        
        cartas.show(centro, "tabla");
    }
    
    private void configurarPanelProvisional(JPanel panel, String texto) {
        panel.setBackground(Color.WHITE);
        panel.add(new JLabel(texto));
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(220, 220, 220)); 
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = new JLabel("Administrador");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);
        
        JLabel sub = new JLabel("Control total del sistema");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        sub.setBorder(BorderFactory.createEmptyBorder(2, 16, 0, 0));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(sub);
        
        menu.add(Box.createVerticalStrut(26));

        for (int i = 0; i < botones.length; i++) {
            final String vista = vistas[i];
            botones[i] = new JButton(titulos[i]); 
            
            botones[i].addActionListener(e -> cartas.show(centro, vista));
            
            ajustar(botones[i]);
            menu.add(botones[i]);
            menu.add(Box.createVerticalStrut(6));
        }

        menu.add(Box.createVerticalGlue());

        JButton verReproductor = new JButton("Ver reproductor"); 
        ajustar(verReproductor);
        menu.add(verReproductor);
        menu.add(Box.createVerticalStrut(6));

        JButton salir = new JButton("Cerrar sesión");
        ajustar(salir);
        menu.add(salir);
        
        return menu;
    }

    private void ajustar(JButton b) {
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminFrame().setVisible(true);
        });
    }
}
