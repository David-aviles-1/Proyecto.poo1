package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class PanelAgregar extends JPanel {

    private final FormularioCancion formulario = new FormularioCancion(true);

    public PanelAgregar() {
        setLayout(new BorderLayout(0, 14));
        setBackground(new Color(18, 18, 20));
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 24));

        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        
        JLabel lblTitulo = new JLabel("Agregar canción");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        
        JLabel lblSub = new JLabel("Todos los campos son obligatorios.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(160, 160, 167));
        
        titulo.add(lblTitulo);
        titulo.add(lblSub);
        add(titulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(formulario);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        JButton guardar = new JButton("Guardar canción");
        guardar.setFocusPainted(false);
        guardar.setBorderPainted(false);
        guardar.setBackground(new Color(40, 120, 220));
        guardar.setForeground(Color.WHITE);
        guardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        guardar.setPreferredSize(new Dimension(170, 42));
        guardar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        guardar.addActionListener(e -> guardar());

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(guardar);
        add(pie, BorderLayout.SOUTH);
    }

    private void guardar() {
        javax.swing.JOptionPane.showMessageDialog(this, "¡Canción registrada visualmente con éxito!", "Éxito", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        formulario.limpiar();
    }
}