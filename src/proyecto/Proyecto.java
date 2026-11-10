package proyecto;

import GUI.loginFrame;
import javax.swing.JFrame;

/**
 *
 * @author oswaldo
 */
public class Proyecto {

    public static void main(String args[]) {
        loginFrame marcoLogin = new loginFrame();
        marcoLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marcoLogin.setSize(450, 500); 
        marcoLogin.setLocationRelativeTo(null); 
        marcoLogin.setResizable(false);
        marcoLogin.setVisible(true); 
    } 
} 