
package GUI;

/**
 *
 * @author oswaldo
 */


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

public class AdminFrame extends JFrame {

    private final CardLayout cartas = new CardLayout();
    private final JPanel centro = new JPanel(cartas);
    
    private final ControladorCanciones ctrl = new ControladorCanciones();
    
    private final PanelAgregar panelAgregar;
    private final JPanel panelConsultar;
    private final JPanel panelModificar;
    private final JPanel panelEliminar;
    
    private final BotonMenu btnCanciones = new BotonMenu("Canciones");
    private final BotonMenu btnAgregar = new BotonMenu("Agregar");
    private final BotonMenu btnConsultar = new BotonMenu("Consultar");
    private final BotonMenu btnModificar = new BotonMenu("Modificar");
    private final BotonMenu btnEliminar = new BotonMenu("Eliminar");

    public AdminFrame() {
        setTitle("Administrador - Control total del sistema");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1040, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(18, 18, 20)); 
        setLayout(new BorderLayout());

        panelAgregar = new PanelAgregar(ctrl, () -> {});
        
        panelConsultar = new JPanel();
        panelModificar = new JPanel();
        panelEliminar = new JPanel();

        configurarPanelProvisional(panelConsultar, "Panel Consultar");
        configurarPanelProvisional(panelModificar, "Panel Modificar");
        configurarPanelProvisional(panelEliminar, "Panel Eliminar");

        centro.setBackground(new Color(18, 18, 20));
        centro.add(panelAgregar, "agregar");
        centro.add(panelConsultar, "consultar");
        centro.add(panelModificar, "modificar");
        centro.add(panelEliminar, "eliminar");

        add(crearMenuLateralAdmin(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> mostrar("agregar"));
        btnConsultar.addActionListener(e -> mostrar("consultar"));
        btnModificar.addActionListener(e -> mostrar("modificar"));
        btnEliminar.addActionListener(e -> mostrar("eliminar"));
        
        mostrar("agregar");
    }

    private void configurarPanelProvisional(JPanel panel, String texto) {
        panel.setBackground(new Color(18, 18, 20));
        JLabel label = new JLabel(texto);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 24));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));
        panel.add(label);
    }

    private JPanel crearMenuLateralAdmin() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(new Color(11, 11, 12)); 
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = new JLabel("Administrador");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);

        JLabel subtitulo = new JLabel("Control total del sistema");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitulo.setForeground(new Color(160, 160, 167));
        subtitulo.setBorder(BorderFactory.createEmptyBorder(2, 16, 0, 0));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(subtitulo);
        
        menu.add(Box.createVerticalStrut(30));

        for (BotonMenu b : new BotonMenu[]{btnAgregar, btnConsultar, btnModificar, btnEliminar}) {
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setPreferredSize(new Dimension(180, 40));
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            menu.add(b);
            menu.add(Box.createVerticalStrut(6));
        }

        menu.add(Box.createVerticalGlue());

        BotonMenu salir = new BotonMenu("Cerrar sesión");
        salir.setAlignmentX(Component.LEFT_ALIGNMENT);
        salir.setPreferredSize(new Dimension(180, 40));
        salir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        salir.setForeground(new Color(160, 160, 167));
        salir.addActionListener(e -> {
            new login().setVisible(true);
            dispose();
        });
        menu.add(salir);
        
        return menu;
    }

    private void mostrar(String vista) {
        cartas.show(centro, vista);
        btnAgregar.setActivo(vista.equals("agregar"));
        btnConsultar.setActivo(vista.equals("consultar"));
        btnModificar.setActivo(vista.equals("modificar"));
        btnEliminar.setActivo(vista.equals("eliminar"));
    }
}