/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.frontend.cloudfarm.procesos.ProcesosProducto;
import com.frontend.cloudfarm.datos.DatosProducto;

//parte de omar integrada al nuevo codigo 
public class ProductoPanel extends JPanel {
    private JTextField txtcodigo, txtnombre, txtcategoria, txtcantidad, txtprecio;
    private JButton btnagregar, btnmodificar, btnlimpiar, btnbuscar;
    private ProcesosProducto procesos;
    
    // Colores para la interfaz del panel y botones 
    private final Color COLOR_FONDO = new Color(240, 248, 255); // Azul claro muy suave
    private final Color COLOR_BOTON_PRINCIPAL = new Color(0, 112, 192); // Azul farmacéutico
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180); // Azul medio
    private final Color COLOR_TEXTO_BOTON = Color.BLACK;
    private final Color COLOR_BORDES = new Color(150, 180, 210);
    private final Color COLOR_ERROR = new Color(255, 220, 220); // Fondo rojo claro para errores
    private final Color COLOR_EXITO = new Color(220, 255, 220); // Fondo verde claro para éxito

    public ProductoPanel() {
        procesos = new ProcesosProducto();
        setBackground(COLOR_FONDO);
        initComponents();
        setupActions();
        applyStyles();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_FONDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos de texto
        txtcodigo = createStyledTextField(20);
        txtnombre = createStyledTextField(20);
        txtcategoria = createStyledTextField(20);
        txtcantidad = createStyledTextField(20);
        txtprecio = createStyledTextField(20);
        
        // los campos para poner
        addField(formPanel, txtcodigo, "Código:", gbc, 0);
        addField(formPanel, txtnombre, "Nombre:", gbc, 1);
        addField(formPanel, txtcategoria, "Categoría:", gbc, 2);
        addField(formPanel, txtcantidad, "Cantidad:", gbc, 3);
        addField(formPanel, txtprecio, "Precio:", gbc, 4);
        
        // Panel de botones  se separan para identificar 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(COLOR_FONDO);
        
        btnagregar = createFarmaButton("Agregar", COLOR_BOTON_PRINCIPAL);
        btnmodificar = createFarmaButton("Modificar", COLOR_BOTON_SECUNDARIO);
        btnlimpiar = createFarmaButton("Limpiar", COLOR_BOTON_SECUNDARIO);
        btnbuscar = createFarmaButton("Buscar", COLOR_BOTON_SECUNDARIO);
        
        buttonPanel.add(btnagregar);
        buttonPanel.add(btnmodificar);
        buttonPanel.add(btnlimpiar);
        buttonPanel.add(btnbuscar);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    //para los estilos 

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    private void addField(JPanel panel, JTextField field, String label, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    //creacion de botones en la interfaz y se agrega el efecto hover que cuando pones el mouse cambia
    private JButton createFarmaButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(COLOR_TEXTO_BOTON);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    //se agrega los estilos 

    private void applyStyles() {
        // Estilo para diálogos
        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("Button.background", COLOR_BOTON_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_TEXTO_BOTON);
    }

    private void setupActions() {
        btnagregar.addActionListener(this::agregarProducto);
        btnmodificar.addActionListener(this::modificarProducto);
        btnlimpiar.addActionListener(e -> limpiarCampos());
    }
    
    //si se agrego el producto al almacen correctamente

    private void agregarProducto(ActionEvent e) {
        DatosProducto producto = capturarDatos();
        if (producto != null) {
            procesos.agregarProducto(producto);
            showMessage("Producto agregado correctamente", "Éxito", COLOR_EXITO);
            limpiarCampos();
        }
    }

    private void modificarProducto(ActionEvent e) {
        DatosProducto producto = capturarDatos();
        if (producto != null) {
            boolean modificado = procesos.modificarProducto(producto);
            if (modificado) {
                showMessage("Producto modificado correctamente", "Éxito", COLOR_EXITO);
            } else {
                showMessage("No se pudo modificar el producto", "Error", COLOR_ERROR);
            }
            limpiarCampos();
        }
    }

    private void showMessage(String message, String title, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label);
        
        JOptionPane.showMessageDialog(this, panel, title, JOptionPane.PLAIN_MESSAGE);
    }
    //captura los datos

    private DatosProducto capturarDatos() {
        try {
            String codigo = txtcodigo.getText().trim();
            String nombre = txtnombre.getText().trim();
            String categoria = txtcategoria.getText().trim();
            int cantidad = Integer.parseInt(txtcantidad.getText().trim());
            double precio = Double.parseDouble(txtprecio.getText().trim());

            if (codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty()) {
                showMessage("Complete todos los campos", "Validación", COLOR_ERROR);
                return null;
            }

            return new DatosProducto(codigo, nombre, categoria, cantidad, precio);
        } catch (NumberFormatException ex) {
            showMessage("Cantidad o precio inválidos. Use números válidos", "Error de formato", COLOR_ERROR);
            return null;
        }
    }
    //limpieza de datos

    private void limpiarCampos() {
        txtcodigo.setText("");
        txtnombre.setText("");
        txtcategoria.setText("");
        txtcantidad.setText("");
        txtprecio.setText("");
        txtcodigo.requestFocusInWindow();
    }
}