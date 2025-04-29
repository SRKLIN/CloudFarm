/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import com.frontend.cloudfarm.procesos.ProcesoLogin;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JFrame {

    // Paleta de colores
    private final Color COLOR_FONDO = new Color(240, 248, 255); // Azul claro
    private final Color COLOR_BOTON = new Color(0, 112, 192);   // Azul farmacéutico
    private final Color COLOR_TEXTO_BOTON = Color.WHITE;

    public LoginPanel() {
        setTitle("CloudFarm - Login");
        setSize(900, 500); // Tamaño original
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Panel principal (BorderLayout)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Panel izquierdo (Imagen + Copyright) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(COLOR_FONDO);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Imagen centrada
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/Recursos/LogoFarmacia 50peque.png"));
        JLabel lblLogo = new JLabel(logoIcon);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblLogo);

        // Espacio entre imagen y copyright
        leftPanel.add(Box.createVerticalStrut(20));

        // Copyright debajo de la imagen
        JLabel lblCopyright = new JLabel("<html><center>© 2025 CloudFarm<br>Derechos reservados<br><br>Sistema de gestión farmacéutica</center></html>");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(lblCopyright);

        // --- Panel derecho (Login) ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(COLOR_FONDO);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        JLabel lblLogin = new JLabel("INICIAR SESIÓN");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(lblLogin, gbc);

        // Campo Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        rightPanel.add(lblUsuario, gbc);

        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        rightPanel.add(txtUsuario, gbc);

        // Campo Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(lblPassword, gbc);

        JPasswordField txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        rightPanel.add(txtPassword, gbc);

        // Botón Entrar
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(COLOR_BOTON);
        btnEntrar.setForeground(COLOR_TEXTO_BOTON);
        btnEntrar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(btnEntrar, gbc);
        
        // --- Acción del botón (MODIFICADA PARA CAJERO) ---
        btnEntrar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String contraseña = new String(txtPassword.getPassword()).trim();

            if (usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Usuario y contraseña son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Verificar credenciales en la base de datos
                String cargo = ProcesoLogin.validarUsuario(usuario, contraseña);

                if (cargo != null) {
                    // Cierra la ventana de login
                    this.dispose();

                    // Redirige según el cargo (CON CAJERO INTEGRADO)
                    switch (cargo.toLowerCase()) {
                        case "gerente":
                            new MainFrameVisual().setVisible(true);
                            break;
                        case "cajero":
                            abrirPanelCajero();
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, 
                                "Rol no reconocido: " + cargo, 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Usuario o contraseña incorrectos", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al conectar con la base de datos: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Enlace "Olvidé contraseña"
        JLabel lblOlvidePassword = new JLabel("<html><u>¿Olvidaste tu contraseña?</u></html>");
        lblOlvidePassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOlvidePassword.setForeground(Color.BLUE.darker());
        lblOlvidePassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        rightPanel.add(lblOlvidePassword, gbc);

        // Ensamblar paneles
        mainPanel.add(leftPanel, BorderLayout.WEST);   // Imagen + Copyright
        mainPanel.add(rightPanel, BorderLayout.EAST); // Login

        add(mainPanel);
    }

    // Método para abrir el panel de cajero (NUEVO)
    private void abrirPanelCajero() {
        JFrame frameCajero = new JFrame("CloudFarm - Punto de Venta (Modo Cajero)");
        frameCajero.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameCajero.setSize(1000, 700);
        frameCajero.setLocationRelativeTo(null);
        
        // Aquí integras tu CajaPanel
        CajaPanel cajaPanel = new CajaPanel();
        frameCajero.add(cajaPanel);
        
        frameCajero.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPanel login = new LoginPanel();
            login.setVisible(true);
        });
    }
}