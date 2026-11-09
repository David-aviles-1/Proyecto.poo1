package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class PanelListas extends JPanel {

    private final DefaultListModel<String> modelo = new DefaultListModel<>();
    private final JList<String> listaNombres = new JList<>(modelo);
    private final JPanel detalle = new JPanel();

    private final Color FONDO = new Color(18, 18, 20);
    private final Color TARJETA = new Color(30, 30, 33);
    private final Color TEXTO = Color.WHITE;
    private final Color TEXTO_SUAVE = new Color(160, 160, 167);

    public PanelListas() {
        setLayout(new BorderLayout());
        setBackground(FONDO);

        listaNombres.setBackground(FONDO);
        listaNombres.setForeground(TEXTO);
        listaNombres.setFont(new Font("SansSerif", Font.BOLD, 14));
        listaNombres.setFixedCellHeight(44);
        listaNombres.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 6));
        listaNombres.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarLista(listaNombres.getSelectedValue());
            }
        });

        JButton nueva = new JButton("+  Nueva lista");
        nueva.setFocusPainted(false);
        nueva.setBorderPainted(false);
        nueva.setBackground(TARJETA);
        nueva.setForeground(TEXTO);
        nueva.setFont(new Font("SansSerif", Font.BOLD, 13));
        nueva.setHorizontalAlignment(SwingConstants.CENTER);
        nueva.setPreferredSize(new Dimension(100, 40));
        nueva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nueva.addActionListener(e -> crearListaNuevaVisual());

        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(8, 14, 16, 10));
        pie.add(nueva, BorderLayout.CENTER);

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBackground(FONDO);
        izquierda.setPreferredSize(new Dimension(220, 100));
        
        JScrollPane scrollLista = new JScrollPane(listaNombres);
        scrollLista.setBorder(null);
        scrollLista.getViewport().setBackground(FONDO);
        
        izquierda.add(scrollLista, BorderLayout.CENTER);
        izquierda.add(pie, BorderLayout.SOUTH);
        add(izquierda, BorderLayout.WEST);

        detalle.setLayout(new BoxLayout(detalle, BoxLayout.Y_AXIS));
        detalle.setBackground(FONDO);
        detalle.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 28));
        
        JScrollPane scrollDetalle = new JScrollPane(detalle);
        scrollDetalle.setBorder(null);
        scrollDetalle.getViewport().setBackground(FONDO);
        scrollDetalle.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollDetalle, BorderLayout.CENTER);

        cargarListasFalsas();
    }

    private void cargarListasFalsas() {
        modelo.clear();
        modelo.addElement("Favoritas");
        modelo.addElement("Para entrenar");
        modelo.addElement("Nocturnas");

        if (!modelo.isEmpty()) {
            listaNombres.setSelectedIndex(0);
        }
    }

    private void crearListaNuevaVisual() {
        String nombre = javax.swing.JOptionPane.showInputDialog(this, "Escribe el nombre de tu nueva lista:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            modelo.addElement(nombre.trim());
            listaNombres.setSelectedValue(nombre.trim(), true);
        }
    }

    private void mostrarLista(String nombre) {
        detalle.removeAll();
        if (nombre == null) {
            JLabel vacia = new JLabel("Crea tu primera lista con \"+ Nueva lista\".");
            vacia.setForeground(TEXTO_SUAVE);
            detalle.add(vacia);
        } else {
            JLabel titulo = new JLabel(nombre);
            titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
            titulo.setForeground(TEXTO);
            titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel total = new JLabel("0 canciones");
            total.setFont(new Font("SansSerif", Font.PLAIN, 13));
            total.setForeground(TEXTO_SUAVE);
            total.setAlignmentX(Component.LEFT_ALIGNMENT);

            detalle.add(titulo);
            detalle.add(total);
            detalle.add(Box.createVerticalStrut(18));

            JLabel vacia = new JLabel("Esta lista está vacía.");
            vacia.setFont(new Font("SansSerif", Font.PLAIN, 13));
            vacia.setForeground(TEXTO_SUAVE);
            vacia.setAlignmentX(Component.LEFT_ALIGNMENT);
            detalle.add(vacia);
        }
        detalle.revalidate();
        detalle.repaint();
    }
}