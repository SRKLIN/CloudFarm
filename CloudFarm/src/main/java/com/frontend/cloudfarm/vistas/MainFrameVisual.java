/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrameVisual extends JFrame {
    
    //creacion de los paneles que se usaran para el menu principal  mas colores 
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JButton btnAlmacen, btnAgregarProducto, btnUsuarios;
    private Color colorFondo = new Color(203, 232, 247); // Celeste claro
    private Color colorBotones = new Color(12, 123, 175); // Azul celeste más oscuro

    public MainFrameVisual() {
        initComponents();
        setupLayout();
        configActions();
        applyStyles(); // Aplicar estilos personalizados
    }
    
    //iniciamos todo cuando arranque el menu
    private void initComponents() {
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        btnAlmacen = new JButton("Almacén");
        btnAgregarProducto = new JButton("Agregar Producto");
        btnUsuarios = new JButton("Usuarios");
    }
    
    //defininedo lo que se mirara 
    
    private void setupLayout() {
        setTitle("CLOUDFARM");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel izquierdo (menú) con color celeste
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));
        leftPanel.setBackground(colorFondo);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // Título del menú
        JLabel titulo = new JLabel("Farmacia CloudFarm");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(0, 82, 136)); // Azul oscuro
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        leftPanel.add(titulo);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Añadir botones en el orden especificado
        leftPanel.add(createStyledButton(btnAlmacen));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(createStyledButton(btnAgregarProducto));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(createStyledButton(btnUsuarios));
        
        // Panel derecho con fondo blanco aqui se pondran los paneles que se agregaran despues
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.add(new JLabel("Seleccione una opción del menú", SwingConstants.CENTER), BorderLayout.CENTER);
        
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
    
    //creacion de lo visual de los botones  revisar luego si no parecen los colores o el estilo +hover
    private JButton createStyledButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 50));
        button.setPreferredSize(new Dimension(220, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(colorBotones);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para los botones
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(colorBotones.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(colorBotones);
            }
        });
        
        return button;
    }
    //aplicamos los estilos 
    
    private void applyStyles() {
        try {
            // Estilo para los componentes
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Panel.background", colorFondo);
            UIManager.put("OptionPane.background", colorFondo);
            UIManager.put("Button.background", colorBotones);
            UIManager.put("Button.foreground", Color.WHITE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    //se configuran las acciones 
    private void configActions() {
        btnAlmacen.addActionListener(this::mostrarAlmacen);
        btnAgregarProducto.addActionListener(this::mostrarAgregarProducto);
        btnUsuarios.addActionListener(this::mostrarUsuarios);
    }
    
    //modificar luego cuando se suba la parte correspondiente en github ultimo visto 22/4/25 8PM
    private void mostrarAlmacen(ActionEvent e) {
        rightPanel.removeAll();
        AlmacenPanel almacen = new AlmacenPanel();
        rightPanel.add(almacen, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    //parte agregada para mostrar el panel ya subido  se muestra el agregar producto revisar que sirva todo 
    private void mostrarAgregarProducto(ActionEvent e) {
        rightPanel.removeAll();
        ProductoPanel productoPanel = new ProductoPanel();
        rightPanel.add(productoPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    //parte para agregar  cuando se tenga la base de datos mostrara la lista de usuarios agregados a forma de demostracion eliminar luego
    //22/4/25
    private void mostrarUsuarios(ActionEvent e) {
        rightPanel.removeAll();
        rightPanel.add(new JLabel("Gestión de Usuarios", SwingConstants.CENTER), BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    //corre el programa
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrameVisual frame = new MainFrameVisual();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}