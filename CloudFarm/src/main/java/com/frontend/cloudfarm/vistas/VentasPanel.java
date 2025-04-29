/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template


AGREGAR UN BOTON QUE PUEDA EXPORTAR LAS VENTAS EN UN DOCUMENTO PENDIENTE DE AÑADIR 
REVISARLO LUEGO ASI PARA LA MODIFCACION  22/4/25
 */
package com.frontend.cloudfarm.vistas;

import com.frontend.cloudfarm.datos.*;
import com.frontend.cloudfarm.procesos.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class VentasPanel extends JPanel {
    private final Color COLOR_FONDO = new Color(240, 248, 255);
    private final Color COLOR_BOTON_PRINCIPAL = new Color(0, 112, 192);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180);
    private final Color COLOR_TEXTO_BOTON = Color.BLACK;
    private final Color COLOR_BORDES = new Color(150, 180, 210);
    private final Color COLOR_ERROR = new Color(255, 220, 220);
    private final Color COLOR_EXITO = new Color(220, 255, 220);
    
    private JTable tablaVentas, tablaDetalles;
    private DefaultTableModel modeloVentas, modeloDetalles;
    private JTextField txtBuscar;
    private JButton btnNuevaVenta, btnRefrescar;
    private ProcesosVentas procesos;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public VentasPanel() {
        procesos = new ProcesosVentas();
        setBackground(COLOR_FONDO);
        initComponents();
        setupActions();
        cargarVentas();
        applyStyles();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior - Busqueda y botones
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(COLOR_FONDO);
        
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(COLOR_FONDO);
        
        txtBuscar = createStyledTextField(20);
        btnRefrescar = createFarmaButton("Refrescar", COLOR_BOTON_SECUNDARIO);
        
        panelBusqueda.add(new JLabel("Buscar venta por fecha:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnRefrescar);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(COLOR_FONDO);
        btnNuevaVenta = createFarmaButton("Nueva Venta", COLOR_BOTON_PRINCIPAL);
        panelBotones.add(btnNuevaVenta);
        
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.EAST);
        
        // Panel central - Tablas
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 15, 0));
        panelCentral.setBackground(COLOR_FONDO);
        
        // Tabla de ventas
        modeloVentas = new DefaultTableModel(new Object[]{"ID", "Fecha", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVentas = new JTable(modeloVentas);
        configurarTabla(tablaVentas);
        
        // Tabla de detalles
        modeloDetalles = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaDetalles = new JTable(modeloDetalles);
        configurarTabla(tablaDetalles);
        
        panelCentral.add(new JScrollPane(tablaVentas));
        panelCentral.add(new JScrollPane(tablaDetalles));
        
        // Agregar componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private void configurarTabla(JTable tabla) {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(25);
        tabla.setBorder(BorderFactory.createLineBorder(COLOR_BORDES));
        
        // Centrar contenido de celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    
    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDES, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }
    
    private JButton createFarmaButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(COLOR_TEXTO_BOTON);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
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
    
    private void setupActions() {
        btnRefrescar.addActionListener(e -> cargarVentas());
        btnNuevaVenta.addActionListener(this::mostrarDialogoNuevaVenta);
        
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetallesVentaSeleccionada();
            }
        });
    }
    
    private void cargarVentas() {
        modeloVentas.setRowCount(0);
        List<DatosVenta> ventas = procesos.obtenerTodasVentas();
        
        for (DatosVenta venta : ventas) {
            modeloVentas.addRow(new Object[]{
                venta.getIdVenta(),
                dateFormat.format(venta.getFecha()),
                String.format("$%.2f", venta.getTotal())
            });
        }
        
        // Limpiar detalles si no hay ventas seleccionadas
        if (ventas.isEmpty()) {
            modeloDetalles.setRowCount(0);
        }
    }
    
    private void cargarDetallesVentaSeleccionada() {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idVenta = (int) modeloVentas.getValueAt(filaSeleccionada, 0);
            List<DatosDetalleVenta> detalles = procesos.obtenerDetallesVenta(idVenta);
            
            modeloDetalles.setRowCount(0);
            for (DatosDetalleVenta detalle : detalles) {
                modeloDetalles.addRow(new Object[]{
                    detalle.getNombreProducto(),
                    detalle.getCantidad(),
                    String.format("$%.2f", detalle.getPrecio()),
                    String.format("$%.2f", detalle.getSubtotal())
                });
            }
        }
    }
    
    
    //ESTE CODIGO BASICAMENTE ES PARA VER SI  SE NECESITA REGISTRAR UNA VENTA ASI MANUAL
    //PERO SE TIENE QUE QUITAR REVISAR LUEGO  22/4/25 
    private void mostrarDialogoNuevaVenta(ActionEvent e) {
        // Crear diálogo para nueva venta
        JDialog dialogo = new JDialog();
        dialogo.setTitle("Nueva Venta");
        dialogo.setLayout(new BorderLayout());
        dialogo.setSize(600, 500);
        dialogo.setLocationRelativeTo(this);
        dialogo.setModal(true);
        
        // Panel para productos disponibles
        JPanel panelProductos = new JPanel(new BorderLayout());
        panelProductos.setBorder(BorderFactory.createTitledBorder("Productos Disponibles"));
        
        DefaultTableModel modeloProductos = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Cantidad"}, 0);
        JTable tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Cargar productos
        ProcesosProducto procProductos = new ProcesosProducto();
        List<DatosProducto> productos = procProductos.obtenerTodos();
        for (DatosProducto producto : productos) {
            modeloProductos.addRow(new Object[]{
                producto.getId_Producto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad()
            });
        }
        
        panelProductos.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        
        // Panel para detalles de la venta
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de Venta"));
        
        DefaultTableModel modeloDetallesVenta = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);
        JTable tablaDetallesVenta = new JTable(modeloDetallesVenta);
        
        panelDetalles.add(new JScrollPane(tablaDetallesVenta), BorderLayout.CENTER);
        
        // Panel para controles
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAgregar = createFarmaButton("Agregar Producto", COLOR_BOTON_SECUNDARIO);
        JButton btnFinalizar = createFarmaButton("Finalizar Venta", COLOR_BOTON_PRINCIPAL);
        
        btnAgregar.addActionListener(ev -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int idProducto = (int) modeloProductos.getValueAt(filaSeleccionada, 0);
                String nombre = (String) modeloProductos.getValueAt(filaSeleccionada, 1);
                double precio = (double) modeloProductos.getValueAt(filaSeleccionada, 2);
                int cantidad = (int) spinnerCantidad.getValue();
                double subtotal = precio * cantidad;
                
                modeloDetallesVenta.addRow(new Object[]{
                    nombre,
                    cantidad,
                    String.format("$%.2f", precio),
                    String.format("$%.2f", subtotal)
                });
            }
        });
        
        btnFinalizar.addActionListener(ev -> {
            if (modeloDetallesVenta.getRowCount() > 0) {
                // Calcular total
                double total = 0;
                for (int i = 0; i < modeloDetallesVenta.getRowCount(); i++) {
                    String subtotalStr = (String) modeloDetallesVenta.getValueAt(i, 3);
                    total += Double.parseDouble(subtotalStr.substring(1));
                }
                
                // Registrar venta
                DatosVenta venta = new DatosVenta(0, new Timestamp(System.currentTimeMillis()), total);
                
                // Crear lista de detalles
                // (Nota: En una implementación real, necesitarías mapear los nombres de producto a IDs)
                
                // Mostrar resumen
                showMessage("Venta registrada por un total de: $" + String.format("%.2f", total), 
                           "Venta Exitosa", COLOR_EXITO);
                dialogo.dispose();
                cargarVentas();
            } else {
                showMessage("Agregue al menos un producto a la venta", "Validación", COLOR_ERROR);
            }
        });
        
        panelControles.add(new JLabel("Cantidad:"));
        panelControles.add(spinnerCantidad);
        panelControles.add(btnAgregar);
        panelControles.add(btnFinalizar);
        
        // Organizar paneles en el diálogo
        JPanel panelPrincipal = new JPanel(new GridLayout(2, 1, 10, 10));
        panelPrincipal.add(panelProductos);
        panelPrincipal.add(panelDetalles);
        
        dialogo.add(panelPrincipal, BorderLayout.CENTER);
        dialogo.add(panelControles, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }
    
    private void applyStyles() {
        UIManager.put("OptionPane.background", COLOR_FONDO);
        UIManager.put("Panel.background", COLOR_FONDO);
        UIManager.put("Button.background", COLOR_BOTON_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_TEXTO_BOTON);
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