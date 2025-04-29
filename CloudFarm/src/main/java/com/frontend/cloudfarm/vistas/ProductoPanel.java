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

public class ProductoPanel extends JPanel {
    private JTextField txtcodigo, txtnombre, txtcategoria, txtcantidad, txtprecio;
    private JButton btnagregar, btnmodificar, btnlimpiar, btnbuscar;
    private ProcesosProducto procesos;
    
    // Colores para la interfaz
    private final Color COLOR_FONDO = new Color(240, 248, 255);
    private final Color COLOR_BOTON_PRINCIPAL = new Color(0, 112, 192);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180);
    private final Color COLOR_TEXTO_BOTON = Color.BLACK;
    private final Color COLOR_BORDES = new Color(150, 180, 210);
    private final Color COLOR_ERROR = new Color(255, 220, 220);
    private final Color COLOR_EXITO = new Color(220, 255, 220);

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
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(COLOR_FONDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtcodigo = createStyledTextField(20);
        txtnombre = createStyledTextField(20);
        txtcategoria = createStyledTextField(20);
        txtcantidad = createStyledTextField(20);
        txtprecio = createStyledTextField(20);
        
        addField(formPanel, txtcodigo, "Código:", gbc, 0);
        addField(formPanel, txtnombre, "Nombre:", gbc, 1);
        addField(formPanel, txtcategoria, "Categoría:", gbc, 2);
        addField(formPanel, txtcantidad, "Cantidad:", gbc, 3);
        addField(formPanel, txtprecio, "Precio:", gbc, 4);
        
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

    private void applyStyles() {
        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("Button.background", COLOR_BOTON_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_TEXTO_BOTON);
    }

    private void setupActions() {
        btnagregar.addActionListener(this::agregarProducto);
        btnmodificar.addActionListener(this::modificarProducto);
        btnlimpiar.addActionListener(e -> limpiarCampos());
        btnbuscar.addActionListener(this::buscarProducto);
    }
    
    private void agregarProducto(ActionEvent e) {
        DatosProducto producto = capturarDatos();
        if (producto != null) {
            try {
                procesos.agregarProducto(producto);
                showMessage("Producto agregado correctamente", "Éxito", COLOR_EXITO);
                limpiarCampos();
            } catch (Exception ex) {
                showMessage("Error al agregar: " + ex.getMessage(), "Error", COLOR_ERROR);
            }
        }
    }

    private void modificarProducto(ActionEvent e) {
        DatosProducto producto = capturarDatos();
        if (producto != null) {
            if (procesos.modificarProducto(producto)) {
                showMessage("Producto modificado correctamente", "Éxito", COLOR_EXITO);
            } else {
                showMessage("No se pudo modificar el producto. Verifique que el código exista.", "Error", COLOR_ERROR);
            }
            limpiarCampos();
        }
    }
    
    private void buscarProducto(ActionEvent e) {
        String idText = txtcodigo.getText().trim();
        
        if (idText.isEmpty()) {
            showMessage("Por favor ingrese un código de producto para buscar", "Campo vacío", COLOR_ERROR);
            txtcodigo.requestFocusInWindow();
            return;
        }
        
        try {
            int idProducto = Integer.parseInt(idText);
            DatosProducto producto = procesos.buscarProducto(idProducto);
            
            if (producto != null) {
                mostrarProductoEnFormulario(producto);
                showMessage("Producto encontrado", "Éxito", COLOR_EXITO);
            } else {
                showMessage("No se encontró el producto con ID: " + idProducto, "Error", COLOR_ERROR);
                limpiarCamposExceptoCodigo();
            }
        } catch (NumberFormatException ex) {
            showMessage("ID inválido. Debe ser un número", "Error de formato", COLOR_ERROR);
        }
    }
    
    private void limpiarCampos() {
        txtcodigo.setText("");
        txtnombre.setText("");
        txtcategoria.setText("");
        txtcantidad.setText("");
        txtprecio.setText("");
        txtcodigo.requestFocusInWindow();
    }
    
    private DatosProducto capturarDatos() {
        try {
            String nombre = txtnombre.getText().trim();
            String categoria = txtcategoria.getText().trim();
            int cantidad = Integer.parseInt(txtcantidad.getText().trim());
            double precio = Double.parseDouble(txtprecio.getText().trim());

            if (nombre.isEmpty() || categoria.isEmpty() || txtcodigo.getText().trim().isEmpty()) {
                showMessage("Complete todos los campos", "Validación", COLOR_ERROR);
                return null;
            }

            int idProducto = Integer.parseInt(txtcodigo.getText().trim());

            return new DatosProducto(idProducto, nombre, categoria, cantidad, precio);
        } catch (NumberFormatException ex) {
            showMessage("Código, cantidad o precio inválidos. Use números válidos", "Error de formato", COLOR_ERROR);
            return null;
        }
    }
    
    private void mostrarProductoEnFormulario(DatosProducto producto) {
        txtcodigo.setText(String.valueOf(producto.getId_Producto()));
        txtnombre.setText(producto.getNombre());
        txtcategoria.setText(producto.getCategoria());
        txtcantidad.setText(String.valueOf(producto.getCantidad()));
        txtprecio.setText(String.valueOf(producto.getPrecio()));
    }

    private void limpiarCamposExceptoCodigo() {
        String codigo = txtcodigo.getText();
        limpiarCampos();
        txtcodigo.setText(codigo);
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
}