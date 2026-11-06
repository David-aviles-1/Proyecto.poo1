package proyecto;

import GUI.login;
import javax.swing.JFrame;

/**
 *
 * @author oswaldo
 */
public class Proyecto {

    public static void main(String args[]) {
        login marcoLogin = new login();
        marcoLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marcoLogin.setSize(450, 500); 
        marcoLogin.setLocationRelativeTo(null); 
        marcoLogin.setResizable(false);
        marcoLogin.setVisible(true); 
    } 
} 