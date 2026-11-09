package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import java.util.function.BiConsumer;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelTablaCanciones extends JPanel {
    private static final String[] COLUMNAS = {"Clave", "Título", "Artista", "Álbum", "Género", "Año", "Duración"};

    private final ControladorCanciones ctrl;
    private final BiConsumer<String, String> irA;
    private final DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final CampoTexto buscador = new CampoTexto("Buscar por clave, título, artista o álbum…");
    private final JLabel lblTotal = EstiloUI.etiqueta(" ", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE);

    public PanelTablaCanciones(ControladorCanciones ctrl, BiConsumer<String, String> irA) {
        this.ctrl = ctrl;
        this.irA = irA;
        setLayout(new BorderLayout(0, 14));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 28));

        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        titulo.add(EstiloUI.etiqueta("Canciones", 26, Font.BOLD, EstiloUI.TEXTO));
        titulo.add(lblTotal);
        buscador.setPreferredSize(new Dimension(340, 42));
        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setOpaque(false);
        arriba.add(titulo, BorderLayout.WEST);
        arriba.add(buscador, BorderLayout.EAST);
        add(arriba, BorderLayout.NORTH);
        buscador.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizar();
            }
        });

        configurarTabla();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setViewportBorder(BorderFactory.createEmptyBorder());
        tabla.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(EstiloUI.TARJETA);
        EstiloUI.estilizarScroll(scroll);
        PanelRedondeado marco = new PanelRedondeado(22, EstiloUI.TARJETA);
        marco.setLayout(new BorderLayout());
        marco.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        marco.add(scroll, BorderLayout.CENTER);
        add(marco, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pie.setOpaque(false);
        pie.add(botonAtajo("Consultar", "consultar"));
        pie.add(botonAtajo("Modificar", "modificar"));
        pie.add(botonAtajo("Eliminar", "eliminar"));
        add(pie, BorderLayout.SOUTH);

        actualizar();
    }

    private BotonMenu botonAtajo(String texto, String modulo) {
        BotonMenu b = new BotonMenu(texto, EstiloUI.TARJETA_HOVER);
        b.setActivo(true);
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setPreferredSize(new Dimension(120, 40));
        b.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                DialogoEstilo.mensaje(this, "Elige una canción", "Selecciona una fila de la tabla primero.");
                return;
            }
            irA.accept(modulo, (String) modelo.getValueAt(fila, 0));
        });
        return b;
    }

    private void configurarTabla() {
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setFillsViewportHeight(true);
        tabla.setBackground(EstiloUI.TARJETA);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFont(EstiloUI.fuente(Font.PLAIN, 13));

        DefaultTableCellRenderer celdas = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                    boolean foco, int fila, int columna) {
                super.getTableCellRendererComponent(t, valor, seleccionada, false, fila, columna);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
                setFont(EstiloUI.fuente(columna == 1 ? Font.BOLD : Font.PLAIN, 13));
                setForeground(seleccionada ? EstiloUI.ACENTO : EstiloUI.TEXTO);
                setBackground(seleccionada ? EstiloUI.TARJETA_HOVER : (fila % 2 == 0 ? EstiloUI.TARJETA : new Color(35, 35, 38)));
                return this;
            }
        };
        tabla.setDefaultRenderer(Object.class, celdas);

        DefaultTableCellRenderer cabecera = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor, boolean seleccionada,
                    boolean foco, int fila, int columna) {
                super.getTableCellRendererComponent(t, valor, false, false, fila, columna);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 6));
                setFont(EstiloUI.fuente(Font.BOLD, 12));
                setForeground(EstiloUI.TEXTO_SUAVE);
                setBackground(EstiloUI.FONDO_LATERAL);
                return this;
            }
        };
        tabla.getTableHeader().setDefaultRenderer(cabecera);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setPreferredSize(new Dimension(100, 38));
        int[] anchos = {70, 200, 150, 150, 105, 66, 96};
        int[] minimos = {60, 120, 100, 100, 80, 62, 92};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            tabla.getColumnModel().getColumn(i).setMinWidth(minimos[i]);
        }
    }

    public void actualizar() {
        String filtro = buscador.getText().trim().toLowerCase();
        modelo.setRowCount(0);
        int mostradas = 0;
        for (Cancion c : ctrl.listarTodas()) {
            String todo = (c.getClave() + " " + c.getTitulo() + " " + c.getArtista() + " " + c.getAlbum()).toLowerCase();
            if (filtro.isEmpty() || todo.contains(filtro)) {
                modelo.addRow(new Object[]{c.getClave(), c.getTitulo(), c.getArtista(), c.getAlbum(),
                    c.getGenero(), c.getAnio(), c.getDuracion()});
                mostradas++;
            }
        }
        int total = ctrl.total();
        lblTotal.setText(filtro.isEmpty() ? total + (total == 1 ? " canción registrada" : " canciones registradas")
                : mostradas + " de " + total + " canciones");
    }
}
