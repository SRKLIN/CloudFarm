/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import com.frontend.cloudfarm.datos.CajaDatos;
import com.frontend.cloudfarm.procesos.CajaProceso;
import com.frontend.cloudfarm.util.ReportGenerator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CajaPanel extends JPanel {

    // Paleta de colores
    private final Color COLOR_FONDO = new Color(240, 248, 255);
    private final Color COLOR_BOTON = new Color(0, 112, 192);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180);
    private final Color COLOR_TEXTO = Color.WHITE;

    // Componentes
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtCodigo, txtCantidad, txtSubtotal, txtDescuento, txtTotal;
    private JButton btnAgregar, btnEliminar, btnPagar, btnFactura;

    public CajaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        initComponents();
    }

    private void initComponents() {
        // Panel superior (búsqueda y agregar)
        JPanel panelSuperior = new JPanel(new GridLayout(1, 4, 10, 10));
        panelSuperior.setBackground(COLOR_FONDO);

        // Campo código
        JPanel panelCodigo = new JPanel(new BorderLayout(5, 5));
        panelCodigo.add(new JLabel("Código Producto:"), BorderLayout.NORTH);
        txtCodigo = new JTextField();
        panelCodigo.add(txtCodigo, BorderLayout.CENTER);
        panelSuperior.add(panelCodigo);

        // Campo cantidad
        JPanel panelCantidad = new JPanel(new BorderLayout(5, 5));
        panelCantidad.add(new JLabel("Cantidad:"), BorderLayout.NORTH);
        txtCantidad = new JTextField();
        panelCantidad.add(txtCantidad, BorderLayout.CENTER);
        panelSuperior.add(panelCantidad);

        // Botón agregar
        btnAgregar = crearBoton("AGREGAR", COLOR_BOTON);
        btnAgregar.addActionListener(this::agregarProducto);
        panelSuperior.add(btnAgregar);

        // Botón eliminar
        btnEliminar = crearBoton("ELIMINAR", COLOR_BOTON_SECUNDARIO);
        btnEliminar.addActionListener(this::eliminarProducto);
        panelSuperior.add(btnEliminar);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(new Object[]{"Código", "Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Panel inferior (totales y botones)
        JPanel panelInferior = new JPanel(new GridLayout(2, 1, 10, 10));
        panelInferior.setBackground(COLOR_FONDO);

        // Panel de totales
        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 10, 10));
        panelTotales.setBackground(COLOR_FONDO);

        // Subtotal
        panelTotales.add(new JLabel("Subtotal:"));
        txtSubtotal = new JTextField();
        txtSubtotal.setEditable(false);
        panelTotales.add(txtSubtotal);

        // Descuento
        panelTotales.add(new JLabel("Descuento (5% si >= $20):"));
        txtDescuento = new JTextField();
        txtDescuento.setEditable(false);
        panelTotales.add(txtDescuento);

        // Total
        panelTotales.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false);
        panelTotales.add(txtTotal);

        panelInferior.add(panelTotales);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setBackground(COLOR_FONDO);

        btnPagar = crearBoton("PAGAR", COLOR_BOTON);
        btnPagar.addActionListener(this::pagarVenta);
        panelBotones.add(btnPagar);

        btnFactura = crearBoton("FACTURA", COLOR_BOTON_SECUNDARIO);
        btnFactura.setEnabled(false);
        btnFactura.addActionListener(this::generarFactura);
        panelBotones.add(btnFactura);

        panelInferior.add(panelBotones);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        return boton;
    }

    private void agregarProducto(ActionEvent e) {
        String codigo = txtCodigo.getText().trim();
        String strCantidad = txtCantidad.getText().trim();

        if (codigo.isEmpty() || strCantidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar código y cantidad", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(strCantidad);
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }

            // Buscar producto en la base de datos
            CajaDatos.Producto producto = CajaDatos.buscarProducto(codigo);
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (producto.getStock() < cantidad) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Disponible: " + producto.getStock(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Agregar a la tabla
            double subtotal = CajaProceso.calcularSubtotal(cantidad, producto.getPrecio());
            modeloTabla.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                cantidad,
                producto.getPrecio(),
                subtotal
            });

            // Limpiar campos y actualizar totales
            txtCodigo.setText("");
            txtCantidad.setText("");
            actualizarTotales();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser un número positivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto(ActionEvent e) {
        int fila = tablaProductos.getSelectedRow();
        if (fila >= 0) {
            modeloTabla.removeRow(fila);
            actualizarTotales();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pagarVenta(ActionEvent e) {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear lista de detalles de venta
        List<CajaProceso.DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String codigo = (String) modeloTabla.getValueAt(i, 0);
            int cantidad = (Integer) modeloTabla.getValueAt(i, 2);
            double precio = (Double) modeloTabla.getValueAt(i, 3);

            detalles.add(new CajaProceso.DetalleVenta(codigo, cantidad, precio));
        }

        // Registrar venta en la base de datos
        boolean exito = CajaProceso.registrarVenta(detalles);

        if (exito) {
            btnPagar.setEnabled(false);
            btnFactura.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //agregue el boton para generar facturas ya con el jasperreport aber si funciona 
    //no le muevan si sirve

    private void generarFactura(ActionEvent e) {
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay productos para facturar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Preparar datos para el reporte
        List<Map<String, Object>> datosFactura = new ArrayList<>();
        double totalVenta = 0.0; 

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("codigo", modeloTabla.getValueAt(i, 0));
            item.put("producto", modeloTabla.getValueAt(i, 1));
            item.put("cantidad", modeloTabla.getValueAt(i, 2));
            item.put("precio", modeloTabla.getValueAt(i, 3));
            item.put("subtotal", modeloTabla.getValueAt(i, 4));

            datosFactura.add(item);
            totalVenta += (Double) modeloTabla.getValueAt(i, 4);
        }

        // 2. Obtener total
       String totalFormateado = String.format("%.2f", totalVenta);

        // 3. Generar factura
        ReportGenerator.generarFactura(datosFactura, totalFormateado);

        // 4. Reiniciar venta
        reiniciarVenta();
    }

    private void reiniciarVenta() {
        modeloTabla.setRowCount(0);
        txtSubtotal.setText("");
        txtDescuento.setText("");
        txtTotal.setText("");
        btnPagar.setEnabled(true);
        btnFactura.setEnabled(false);
    }

    private void actualizarTotales() {
        double subtotal = 0;

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            subtotal += (Double) modeloTabla.getValueAt(i, 4);
        }

        double descuento = CajaProceso.calcularDescuento(subtotal);
        double total = CajaProceso.calcularTotal(subtotal, descuento);

        txtSubtotal.setText(String.format("%.2f", subtotal));
        txtDescuento.setText(String.format("%.2f", descuento));
        txtTotal.setText(String.format("%.2f", total));
    }
}
