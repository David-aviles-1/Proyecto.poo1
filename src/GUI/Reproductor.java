
package GUI;

import java.awt.CardLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author oswaldo
 */
public class Reproductor extends JFrame {
   private final CardLayout cartas = new CardLayout();
   private final JPanel centro = new JPanel(cartas);
   
   private final BarraReproduccion barra;
   private final PanelAlbumes panelALbumes;
   private final PanelArtistas panelArtistas;
   private final PanelListas panelListas;
   private final PanelReproduciendo panelReproduciendo;
   private final BotonMenu btnAlbumes = new BotonMenu("Albumes");
   private final BotonMenu btnArtistas = new BotonMenu("Artistas");
   private final BotonMenu btnListas = new BotonMenu("Listas de reproduccion");
   
   
   public Reproductor() {
       setTitle("Reproductor");
       setSize(1100,720);
       setMinimumSize(new Dimension(1040,640));
       setLocationRelativeTo(null);
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
       
       
       
       
       
       
       
       
   }
   
   
    
    
}
