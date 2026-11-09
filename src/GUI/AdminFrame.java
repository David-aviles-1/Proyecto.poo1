package GUI;

import control.ControladorCanciones;
import java.awt.*;
import javax.swing.*;

public class AdminFrame extends JFrame {
    private final ControladorCanciones ctrl;
    private final CardLayout cartas = new CardLayout();
    private final JPanel centro = new JPanel(cartas);

    private final PanelTablaCanciones panelTabla;
    private final PanelAgregar panelAgregar;
    private final PanelConsultar panelConsultar;
    private final PanelModificar panelModificar;
    private final PanelEliminar panelEliminar;

    private final String[] vistas = {"tabla", "agregar", "consultar", "modificar", "eliminar"};
    private final String[] titulos = {"Canciones", "Agregar", "Consultar", "Modificar", "Eliminar"};
    private final BotonMenu[] botones = new BotonMenu[5];

    public AdminFrame(ControladorCanciones ctrl) {
        this.ctrl = ctrl;
        setTitle("Mi Reproductor - Administrador");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1040, 660));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(EstiloUI.FONDO);
        setLayout(new BorderLayout());

        Runnable refrescar = this::refrescarTabla;
        panelTabla = new PanelTablaCanciones(ctrl, this::irA);
        panelAgregar = new PanelAgregar(ctrl, refrescar);
        panelConsultar = new PanelConsultar(ctrl);
        panelModificar = new PanelModificar(ctrl, refrescar);
        panelEliminar = new PanelEliminar(ctrl, refrescar);

        centro.setBackground(EstiloUI.FONDO);
        centro.add(panelTabla, "tabla");
        centro.add(panelAgregar, "agregar");
        centro.add(panelConsultar, "consultar");
        centro.add(panelModificar, "modificar");
        centro.add(panelEliminar, "eliminar");

        add(crearMenuLateral(), BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
        mostrar("tabla");
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(EstiloUI.FONDO_LATERAL);
        menu.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));
        menu.setPreferredSize(new Dimension(230, 100));

        JLabel titulo = EstiloUI.etiqueta("Administrador", 22, Font.BOLD, EstiloUI.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(titulo);
        JLabel sub = EstiloUI.etiqueta("Control total del sistema", 12, Font.PLAIN, EstiloUI.TEXTO_SUAVE);
        sub.setBorder(BorderFactory.createEmptyBorder(2, 16, 0, 0));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(sub);
        menu.add(Box.createVerticalStrut(26));

        for (int i = 0; i < botones.length; i++) {
            final String vista = vistas[i];
            botones[i] = new BotonMenu(titulos[i]);
            botones[i].addActionListener(e -> mostrar(vista));
            ajustar(botones[i]);
            menu.add(botones[i]);
            menu.add(Box.createVerticalStrut(6));
        }

        menu.add(Box.createVerticalGlue());

        BotonMenu verReproductor = new BotonMenu("Ver reproductor");
        verReproductor.setForeground(EstiloUI.TEXTO_SUAVE);
        verReproductor.addActionListener(e -> new ReproductorFrame(ctrl).setVisible(true));
        ajustar(verReproductor);
        menu.add(verReproductor);
        menu.add(Box.createVerticalStrut(6));

        BotonMenu salir = new BotonMenu("Cerrar sesión");
        salir.setForeground(EstiloUI.TEXTO_SUAVE);
        salir.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        ajustar(salir);
        menu.add(salir);
        return menu;
    }

    private void ajustar(BotonMenu b) {
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setPreferredSize(new Dimension(100, 44));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
    }

    private void mostrar(String vista) {
        if (vista.equals("tabla")) {
            panelTabla.actualizar();
        } else if (vista.equals("consultar")) {
            panelConsultar.limpiar();
        } else if (vista.equals("modificar")) {
            panelModificar.limpiar();
        } else if (vista.equals("eliminar")) {
            panelEliminar.limpiar();
        }
        cartas.show(centro, vista);
        for (int i = 0; i < botones.length; i++) {
            botones[i].setActivo(vistas[i].equals(vista));
        }
    }

    private void irA(String vista, String clave) {
        mostrar(vista);
        if (vista.equals("consultar")) {
            panelConsultar.buscar(clave);
        } else if (vista.equals("modificar")) {
            panelModificar.buscar(clave);
        } else if (vista.equals("eliminar")) {
            panelEliminar.buscar(clave);
        }
    }

    private void refrescarTabla() {
        panelTabla.actualizar();
    }
}
