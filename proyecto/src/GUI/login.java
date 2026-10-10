package GUI;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;

/**
 *
 * @author oswaldo
 */
public class login extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txcontrasena;
    private JButton btnSignIn;

    
    public login() {
        super("Login - Reproductor");

        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(25, 20, 30));
        panelPrincipal.setLayout(null); 

        
        JPanel cardPanel = new JPanel();
        cardPanel.setBounds(40, 40, 355, 380);
        cardPanel.setBackground(new Color(45, 35, 55));
        cardPanel.setLayout(null);

        
        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBounds(20, 30, 315, 35);
        cardPanel.add(lblTitulo);

        
        txtUsuario = new JTextField();
        txtUsuario.setBounds(40, 100, 275, 40);
        txtUsuario.setBackground(new Color(70, 55, 80));
        txtUsuario.setForeground(Color.WHITE);
        txtUsuario.setCaretColor(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cardPanel.add(txtUsuario);

        
        txcontrasena= new JPasswordField();
        txcontrasena.setBounds(40, 160, 275, 40);
        txcontrasena.setBackground(new Color(70, 55, 80));
        txcontrasena.setForeground(Color.WHITE);
        txcontrasena.setCaretColor(Color.WHITE);
        txcontrasena.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        cardPanel.add(txcontrasena);

        
        btnSignIn = new JButton("Ingresar");
        btnSignIn.setBounds(40, 230, 275, 42);
        btnSignIn.setBackground(Color.WHITE);
        btnSignIn.setForeground(Color.BLACK);
        btnSignIn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSignIn.setFocusPainted(false);
        cardPanel.add(btnSignIn);

        
        btnSignIn.addActionListener(
                new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent evento) {
                validarLogin();
            }
        } 
        ); 
       
        JLabel lblRolInfo = new JLabel("Admin o usuario normal según tu cuenta", SwingConstants.CENTER);
        lblRolInfo.setForeground(new Color(170, 160, 180));
        lblRolInfo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblRolInfo.setBounds(20, 290, 315, 20);
        cardPanel.add(lblRolInfo);

        panelPrincipal.add(cardPanel);
        add(panelPrincipal);
    } 
    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txcontrasena.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean esAdmin = false;
        if (usuario.equalsIgnoreCase("admin") && password.equals("admin123")) {
            esAdmin = true;
        } else if (usuario.equalsIgnoreCase("usuario") && password.equals("usuario123")) {
            esAdmin = false;
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "¡Bienvenido! : " + (esAdmin ? "Administrador" : "Usuario Normal"));
        this.dispose(); 
    }
} 