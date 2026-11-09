package GUI;

import control.Cancion;
import control.ControladorCanciones;
import java.awt.*;
import javax.swing.*;

public class PanelEliminar extends JPanel {
    private final ControladorCanciones ctrl;
    private final Runnable alCambiar;
    private final BarraBusquedaClave barra;
    private final JPanel resultado = new JPanel(new BorderLayout());
    private Cancion encontrada = null;

    public PanelEliminar(ControladorCanciones ctrl, Runnable alCambiar) {
        this.ctrl = ctrl;
        this.alCambiar = alCambiar;
        setLayout(new BorderLayout(0, 16));
        setBackground(EstiloUI.FONDO);
        setBorder(BorderFactory.createEmptyBorder(24, 28, 18, 28));

        JPanel arriba = new JPanel(new BorderLayout(0, 14));
        arriba.setOpaque(false);
        JPanel titulo = new JPanel(new GridLayout(2, 1));
        titulo.setOpaque(false);
        titulo.add(EstiloUI.etiqueta("Eliminar canción", 26, Font.BOLD, EstiloUI.TEXTO));
        titulo.add(EstiloUI.etiqueta("Busca por clave. Se borra toda su información, su carátula y sus PDFs.", 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
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
            DialogoEstilo.mensaje(this, "Falta la clave", "Escribe la clave de la canción que quieres eliminar.");
            return;
        }
        Cancion c = ctrl.consultarCancion(clave);
        if (c == null) {
            encontrada = null;
            resultado.removeAll();
            resultado.revalidate();
            resultado.repaint();
            DialogoEstilo.mensaje(this, "No encontrada", "No existe una canción con la clave " + clave + ".");
            return;
        }
        mostrar(c);
    }

    private void mostrar(Cancion c) {
        encontrada = c;
        resultado.removeAll();

        PanelRedondeado tarjeta = new PanelRedondeado(28, EstiloUI.TARJETA);
        tarjeta.setLayout(new BorderLayout(24, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        tarjeta.add(new JLabel(EstiloUI.caratula(c, 170)), BorderLayout.WEST);

        JPanel info = new JPanel(new GridLayout(5, 1, 0, 4));
        info.setOpaque(false);
        info.add(EstiloUI.etiqueta(c.getTitulo(), 24, Font.BOLD, EstiloUI.TEXTO));
        info.add(EstiloUI.etiqueta(c.getArtista(), 15, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        info.add(EstiloUI.etiqueta(c.getAlbum() + "  ·  " + c.getAnio() + "  ·  " + c.getGenero(), 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));
        info.add(EstiloUI.etiqueta("Clave: " + c.getClave() + "   ·   Duración: " + c.getDuracion(), 13, Font.PLAIN, EstiloUI.TEXTO_SUAVE));

        BotonPrimario eliminar = new BotonPrimario("Eliminar canción");
        eliminar.setPreferredSize(new Dimension(170, 40));
        eliminar.addActionListener(e -> eliminar());
        JPanel zonaBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 6));
        zonaBoton.setOpaque(false);
        zonaBoton.add(eliminar);
        info.add(zonaBoton);
        tarjeta.add(info, BorderLayout.CENTER);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.add(tarjeta, BorderLayout.NORTH);
        resultado.add(contenedor, BorderLayout.CENTER);
        resultado.revalidate();
        resultado.repaint();
    }

    private void eliminar() {
        if (encontrada == null) {
            return;
        }
        boolean seguro = DialogoEstilo.confirmar(this, "¿Eliminar canción?",
                "Se borrará \"" + encontrada.getTitulo() + "\" de " + encontrada.getArtista()
                + " junto con su carátula y sus PDFs. Esta acción no se puede deshacer.");
        if (!seguro) {
            return;
        }
        String titulo = encontrada.getTitulo();
        if (ctrl.eliminarCancion(encontrada.getClave())) {
            DialogoEstilo.mensaje(this, "Canción eliminada", "\"" + titulo + "\" se eliminó correctamente.");
            limpiar();
            alCambiar.run();
        } else {
            DialogoEstilo.mensaje(this, "Error", "No se pudo eliminar la canción.");
        }
    }

    public void limpiar() {
        encontrada = null;
        barra.setClave("");
        resultado.removeAll();
        resultado.revalidate();
        resultado.repaint();
    }
}
