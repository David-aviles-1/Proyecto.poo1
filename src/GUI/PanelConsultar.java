package GUI;

import control.Cancion;
import control.ControladorCanciones;
import archivos.ManejadorArchivos;
import java.awt.*;
import java.io.File;
import javax.swing.*;

public class PanelConsultar extends JPanel {
    private final ControladorCanciones ctrl;
    private final BarraBusquedaClave barra;
    private final JPanel resultado = new JPanel(new BorderLayout());
    private Cancion mostrada = null;

    public PanelConsultar(ControladorCanciones ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout(0, 16));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 28));

        JPanel arriba = new JPanel(new BorderLayout(0, 14));
        arriba.setOpaque(false);
        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        titulo.add(EstiloUI.etiqueta("Consultar canción", 26, Font.BOLD, EstiloUI.TEXTO));
        titulo.add(EstiloUI.etiqueta("Busca por la clave de la canción.", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        barra = new BarraBusquedaClave(this::buscar);
        arriba.add(titulo, BorderLayout.NORTH);
        arriba.add(barra, BorderLayout.CENTER);
        add(arriba, BorderLayout.NORTH);

        resultado.setOpaque(false);
        add(resultado, BorderLayout.CENTER);
    }

    public void buscar(String clave) {
        barra.setClave(clave);
        if (clave.isEmpty()) {
            DialogoEstilo.mensaje(this, "Falta la clave", "Escribe la clave de la canción que quieres consultar.");
            return;
        }
        Cancion c = ctrl.consultarCancion(clave);
        if (c == null) {
            mostrada = null;
            resultado.removeAll();
            resultado.revalidate();
            resultado.repaint();
            DialogoEstilo.mensaje(this, "No encontrada", "No existe una canción con la clave " + clave + ".");
            return;
        }
        mostrar(c);
    }

    private void mostrar(Cancion c) {
        mostrada = c;
        resultado.removeAll();

        PanelRedondeado tarjeta = new PanelRedondeado(28, EstiloUI.TARJETA);
        tarjeta.setLayout(new BorderLayout(24, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JPanel izq = new JPanel(new BorderLayout());
        izq.setOpaque(false);
        izq.add(new JLabel(EstiloUI.caratula(c, 200)), BorderLayout.NORTH);
        tarjeta.add(izq, BorderLayout.WEST);

        JPanel datos = new JPanel(new GridBagLayout());
        datos.setOpaque(false);
        String[][] filas = {
            {"Clave", c.getClave()}, {"Título", c.getTitulo()}, {"Género", c.getGenero()},
            {"Artista", c.getArtista()}, {"Año", String.valueOf(c.getAnio())}, {"Álbum", c.getAlbum()},
            {"Duración", c.getDuracion()}, {"Imagen", c.getRutaImagen()}, {"PDF de la letra", c.getRutaLetraPdf()}};
        for (int i = 0; i < filas.length; i++) {
            GridBagConstraints g = new GridBagConstraints();
            g.gridy = i;
            g.anchor = GridBagConstraints.WEST;
            g.insets = new Insets(3, 0, 3, 18);
            g.gridx = 0;
            datos.add(EstiloUI.etiqueta(filas[i][0], 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE), g);
            g.gridx = 1;
            g.weightx = 1;
            g.insets = new Insets(3, 0, 3, 0);
            datos.add(EstiloUI.etiqueta(filas[i][1] == null ? "-" : filas[i][1], 14, i == 1 ? Font.BOLD : Font.PLAIN, EstiloUI.TEXTO), g);
        }

        JTextArea letra = new JTextArea(c.getLetra() == null || c.getLetra().isEmpty()
                ? "(Esta canción usa un PDF propio; no tiene letra en texto.)" : c.getLetra());
        letra.setEditable(false);
        letra.setOpaque(false);
        letra.setForeground(EstiloUI.TEXTO);
        letra.setFont(EstiloUI.fuente(Font.PLAIN, 13));
        letra.setLineWrap(true);
        letra.setWrapStyleWord(true);
        letra.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        letra.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(letra);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        EstiloUI.estilizarScroll(scroll);
        PanelRedondeado cajaLetra = new PanelRedondeado(14, EstiloUI.TARJETA_HOVER);
        cajaLetra.setLayout(new BorderLayout());
        cajaLetra.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        cajaLetra.add(scroll, BorderLayout.CENTER);

        BotonMenu abrirPdf = new BotonMenu("Abrir PDF", EstiloUI.TARJETA_HOVER);
        abrirPdf.setActivo(true);
        abrirPdf.setHorizontalAlignment(SwingConstants.CENTER);
        abrirPdf.setPreferredSize(new Dimension(130, 38));
        abrirPdf.addActionListener(e -> abrirPdf());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pie.setOpaque(false);
        pie.add(abrirPdf);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.setOpaque(false);
        centro.add(datos, BorderLayout.NORTH);
        centro.add(cajaLetra, BorderLayout.CENTER);
        centro.add(pie, BorderLayout.SOUTH);
        tarjeta.add(centro, BorderLayout.CENTER);

        resultado.add(tarjeta, BorderLayout.CENTER);
        resultado.revalidate();
        resultado.repaint();
    }

    private void abrirPdf() {
        if (mostrada == null || mostrada.getRutaLetraPdf() == null || !new File(mostrada.getRutaLetraPdf()).exists()) {
            DialogoEstilo.mensaje(this, "Sin PDF", "No se encontró el PDF de esta canción.");
            return;
        }
        String error = ManejadorArchivos.abrirConSistema(new File(mostrada.getRutaLetraPdf()));
        if (error != null) {
            DialogoEstilo.mensaje(this, "No se pudo abrir", error);
        }
    }

    public void limpiar() {
        mostrada = null;
        barra.setClave("");
        resultado.removeAll();
        resultado.revalidate();
        resultado.repaint();
    }
}
