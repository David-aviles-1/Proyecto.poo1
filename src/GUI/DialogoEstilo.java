package GUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class DialogoEstilo {
    public static void mensaje(Component padre, String titulo, String texto) {
        mostrar(padre, titulo, texto, null, false, null);
    }

    public static boolean confirmar(Component padre, String titulo, String texto) {
        return mostrar(padre, titulo, texto, null, true, null);
    }

    public static String pedirTexto(Component padre, String titulo, String pregunta) {
        CampoTexto campo = new CampoTexto();
        campo.setPreferredSize(new Dimension(320, 44));
        return mostrar(padre, titulo, pregunta, campo, true, campo) ? campo.getText() : null;
    }

    public static String elegir(Component padre, String titulo, String pregunta, String[] opciones) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (String o : opciones) {
            modelo.addElement(o);
        }
        JList<String> lista = new JList<>(modelo);
        lista.setBackground(EstiloUI.TARJETA);
        lista.setCellRenderer(new RenderizadorLista(EstiloUI.TARJETA_HOVER));
        lista.setFixedCellHeight(40);
        lista.setSelectedIndex(0);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(EstiloUI.TARJETA);
        EstiloUI.estilizarScroll(scroll);
        int filas = Math.min(opciones.length, 4);
        scroll.setPreferredSize(new Dimension(320, 40 * filas + 4));

        return mostrar(padre, titulo, pregunta, scroll, true, lista) ? lista.getSelectedValue() : null;
    }

    private static boolean mostrar(Component padre, String titulo, String texto,
            JComponent medio, boolean conCancelar, JComponent enfocar) {
        Window ventana = padre == null ? null : SwingUtilities.getWindowAncestor(padre);
        JDialog dialogo = new JDialog(ventana, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setUndecorated(true);
        try {
            dialogo.setBackground(new Color(0, 0, 0, 0));
        } catch (Exception e) {
        }

        PanelRedondeado tarjeta = new PanelRedondeado(26, EstiloUI.TARJETA) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                EstiloUI.calidad(g2);
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 52, 52);
                g2.dispose();
            }
        };
        tarjeta.setLayout(new BorderLayout(0, 14));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(24, 26, 20, 26));

        String seguro = texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        JPanel cabecera = new JPanel(new BorderLayout(0, 8));
        cabecera.setOpaque(false);
        cabecera.add(EstiloUI.etiqueta(titulo, 18, Font.BOLD, EstiloUI.TEXTO), BorderLayout.NORTH);
        cabecera.add(EstiloUI.etiqueta("<html><div style='width:300px'>" + seguro + "</div></html>",
                13, Font.PLAIN, EstiloUI.TEXTO_SUAVE), BorderLayout.CENTER);
        tarjeta.add(cabecera, BorderLayout.NORTH);

        if (medio != null) {
            tarjeta.add(medio, BorderLayout.CENTER);
        }

        final boolean[] aceptado = {false};
        BotonPrimario aceptar = new BotonPrimario("Aceptar");
        aceptar.setPreferredSize(new Dimension(110, 38));
        aceptar.addActionListener(e -> {
            aceptado[0] = true;
            dialogo.dispose();
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        if (conCancelar) {
            BotonMenu cancelar = new BotonMenu("Cancelar");
            cancelar.setHorizontalAlignment(SwingConstants.CENTER);
            cancelar.setPreferredSize(new Dimension(96, 38));
            cancelar.addActionListener(e -> dialogo.dispose());
            botones.add(cancelar);
        }
        botones.add(aceptar);
        tarjeta.add(botones, BorderLayout.SOUTH);

        JPanel marco = new JPanel(new BorderLayout());
        marco.setOpaque(false);
        marco.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        marco.add(tarjeta, BorderLayout.CENTER);
        dialogo.setContentPane(marco);

        dialogo.getRootPane().setDefaultButton(aceptar);
        dialogo.getRootPane().registerKeyboardAction(e -> dialogo.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialogo.pack();
        dialogo.setLocationRelativeTo(padre);
        if (enfocar != null) {
            dialogo.addWindowListener(new WindowAdapter() {
                @Override
                public void windowOpened(WindowEvent e) {
                    enfocar.requestFocusInWindow();
                }
            });
        }
        dialogo.setVisible(true);
        return aceptado[0];
    }
}
