package GUI;

import control.ControladorCanciones;
import control.ControladorUsuarios;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoClave;
    private final ControladorUsuarios usuarios = new ControladorUsuarios();

    public LoginFrame() {
        setTitle("Reproductor - Login");
        setSize(420, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        FondoDegradado panelFondo = new FondoDegradado();
        panelFondo.setLayout(new GridBagLayout());
        setContentPane(panelFondo);

        TarjetaVidrio tarjeta = new TarjetaVidrio();
        tarjeta.setLayout(new GridBagLayout());
        tarjeta.setPreferredSize(new Dimension(320, 390));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.insets = new Insets(10, 30, 10, 30);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Bienvenido");
        titulo.setFont(EstiloUI.fuente(Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        gc.gridy = 0;
        gc.insets = new Insets(40, 30, 20, 30);
        tarjeta.add(titulo, gc);

        campoUsuario = crearCampoTexto("Usuario");
        gc.gridy = 1;
        gc.insets = new Insets(10, 30, 10, 30);
        tarjeta.add(campoUsuario, gc);

        campoClave = crearCampoClave("Contraseña");
        gc.gridy = 2;
        tarjeta.add(campoClave, gc);

        JButton botonEntrar = crearBotonPrincipal("Sign in");
        gc.gridy = 3;
        gc.insets = new Insets(25, 30, 10, 30);
        tarjeta.add(botonEntrar, gc);

        JLabel info = new JLabel("Entra con tu usuario y contraseña");
        info.setFont(EstiloUI.fuente(Font.PLAIN, 11));
        info.setForeground(new Color(255, 255, 255, 160));
        gc.gridy = 4;
        gc.insets = new Insets(5, 30, 20, 30);
        tarjeta.add(info, gc);

        panelFondo.add(tarjeta);

        panelFondo.setFocusable(true);
        SwingUtilities.invokeLater(() -> panelFondo.requestFocusInWindow());

        botonEntrar.addActionListener(e -> validarLogin());
        getRootPane().setDefaultButton(botonEntrar);
    }

    private void validarLogin() {
        String usuario = campoUsuario.getText().trim();
        String clave = new String(campoClave.getPassword());

        if (usuario.isEmpty() || clave.isEmpty()) {
            DialogoEstilo.mensaje(this, "Datos incompletos", "Debes llenar el usuario y la contraseña.");
            return;
        }

        String rol = usuarios.iniciarSesion(usuario, clave);
        if (rol == null) {
            DialogoEstilo.mensaje(this, "Datos incorrectos", "El usuario o la contraseña no son correctos.");
            campoClave.setText("");
            return;
        }

        ControladorCanciones canciones = new ControladorCanciones();
        if (rol.equals(ControladorUsuarios.ROL_ADMIN)) {
            new AdminFrame(canciones).setVisible(true);
        } else {
            new ReproductorFrame(canciones).setVisible(true);
        }
        this.dispose();
    }

    private JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField();
        estilizarCampo(campo, placeholder);
        return campo;
    }

    private JPasswordField crearCampoClave(String placeholder) {
        JPasswordField campo = new JPasswordField();
        estilizarCampo(campo, placeholder);
        return campo;
    }

    private void estilizarCampo(JTextField campo, String placeholder) {
        campo.setOpaque(false);
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setFont(EstiloUI.fuente(Font.PLAIN, 14));
        campo.setBorder(new EmptyBorder(10, 15, 10, 15));
        campo.setPreferredSize(new Dimension(260, 42));

        campo.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            public void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                EstiloUI.calidad(g2);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fill(new RoundRectangle2D.Float(0, 0, campo.getWidth(), campo.getHeight(), 18, 18));
                g2.setColor(new Color(255, 255, 255, 120));
                g2.draw(new RoundRectangle2D.Float(0, 0, campo.getWidth() - 1, campo.getHeight() - 1, 18, 18));

                if (campo.getDocument().getLength() == 0 && !campo.isFocusOwner()) {
                    g2.setColor(new Color(255, 255, 255, 140));
                    g2.setFont(campo.getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, 15, (campo.getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }
                g2.dispose();
                super.paintSafely(g);
            }
        });

        campo.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                campo.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                campo.repaint();
            }
        });
    }

    private JButton crearBotonPrincipal(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                EstiloUI.calidad(g2);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 18, 18));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(new Color(20, 20, 20));
        boton.setFont(EstiloUI.fuente(Font.BOLD, 15));
        boton.setPreferredSize(new Dimension(260, 44));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private static class FondoDegradado extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            EstiloUI.calidad(g2);
            int w = getWidth(), h = getHeight();

            g2.setColor(new Color(12, 12, 14));
            g2.fillRect(0, 0, w, h);

            GradientPaint gp1 = new GradientPaint(0, 0, new Color(90, 90, 98, 110), w, h * 0.7f, new Color(30, 30, 34, 0));
            g2.setPaint(gp1);
            g2.fillRect(0, 0, w, h);

            GradientPaint gp2 = new GradientPaint(w, h, new Color(70, 70, 78, 90), 0, 0, new Color(20, 20, 24, 0));
            g2.setPaint(gp2);
            g2.fillRect(0, 0, w, h);

            g2.dispose();
        }
    }

    private static class TarjetaVidrio extends JPanel {
        TarjetaVidrio() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            EstiloUI.calidad(g2);
            int w = getWidth(), h = getHeight();

            g2.setColor(new Color(255, 255, 255, 25));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 28, 28));

            g2.setColor(new Color(255, 255, 255, 60));
            g2.draw(new RoundRectangle2D.Float(0, 0, w - 1, h - 1, 28, 28));

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
