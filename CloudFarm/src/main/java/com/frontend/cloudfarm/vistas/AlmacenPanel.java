/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import com.frontend.cloudfarm.datos.AlmacenProducto;
import com.frontend.cloudfarm.procesos.AlmacenGestor;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class AlmacenPanel extends JPanel {
    // Paleta de colores
    private final Color COLOR_FONDO = new Color(240, 248, 255);
    private final Color COLOR_BOTON_PRINCIPAL = new Color(0, 112, 192);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180);
    private final Color COLOR_TEXTO = Color.BLACK;
    private final Color COLOR_BORDES = new Color(150, 180, 210);
    private final Color COLOR_CABECERA_TABLA = new Color(200, 220, 255);
    private final Color COLOR_FILA_PAR = new Color(240, 248, 255);
    private final Color COLOR_FILA_IMPAR = Color.WHITE;
    private final Color COLOR_SELECCION = new Color(144, 238, 144);
    private final Color COLOR_EXITO = new Color(220, 255, 220);
    private final Color COLOR_ERROR = new Color(255, 220, 220);

    private AlmacenGestor gestor = new AlmacenGestor();
    private DefaultTableModel modeloTabla;
    private JTable tabla;
    private JTextField txtBusqueda, txtProducto, txtCategoria, txtCantidad, txtPrecio;
    private JButton btnBuscar, btnGuardar, btnEliminar, btnModificar, btnLimpiar;
    private int productoSeleccionadoId = -1;

    public AlmacenPanel() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(10, 10));
        initComponents();
        configurarTabla();
        cargarDatosEnTabla();
    }

    private void initComponents() {
        // Panel superior (título y búsqueda)
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(COLOR_FONDO);
        
        JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO - BODEGA", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(COLOR_TEXTO);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBusqueda.setBackground(COLOR_FONDO);
        
        txtBusqueda = crearCampoTexto(15);
        btnBuscar = crearBoton("Buscar", COLOR_BOTON_SECUNDARIO);
        
        panelBusqueda.add(new JLabel("ID Producto:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelSuperior.add(panelBusqueda, BorderLayout.CENTER);

        // Tabla de productos
        tabla = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(900, 500));

        // Panel de formulario (derecha)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(COLOR_FONDO);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblAcciones = new JLabel("Acciones de Inventario");
        lblAcciones.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblAcciones.setForeground(COLOR_TEXTO);
        panelFormulario.add(lblAcciones);
        panelFormulario.add(Box.createVerticalStrut(15));

        // Campos del formulario
        txtProducto = crearCampoTexto(20);
        txtCategoria = crearCampoTexto(20);
        txtCantidad = crearCampoTexto(20);
        txtPrecio = crearCampoTexto(20);

        panelFormulario.add(crearFilaFormulario("Producto:", txtProducto));
        panelFormulario.add(crearFilaFormulario("Categoría:", txtCategoria));
        panelFormulario.add(crearFilaFormulario("Cantidad:", txtCantidad));
        panelFormulario.add(crearFilaFormulario("Precio:", txtPrecio));

        // Botones CRUD
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnGuardar = crearBoton("Guardar Cambios", COLOR_BOTON_PRINCIPAL);
        btnEliminar = crearBoton("Eliminar Producto", COLOR_BOTON_SECUNDARIO);
        btnModificar = crearBoton("Actualizar Stock", COLOR_BOTON_SECUNDARIO);
        btnLimpiar = crearBoton("Limpiar Campos", COLOR_BOTON_SECUNDARIO);
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        panelFormulario.add(Box.createVerticalStrut(15));
        panelFormulario.add(panelBotones);

        // Organización final
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelCentral.add(panelFormulario, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        // Conectar eventos
        conectarEventos();
    }

    private void conectarEventos() {
        btnBuscar.addActionListener(e -> buscarProductoPorId());
        btnGuardar.addActionListener(e -> guardarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnModificar.addActionListener(e -> modificarProducto());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarProductoDeTabla();
            }
        });

        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarProductoPorId();
                }
            }
        });
    }

    private void seleccionarProductoDeTabla() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            productoSeleccionadoId = (int) modeloTabla.getValueAt(fila, 0);
            txtProducto.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtCategoria.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtPrecio.setText(modeloTabla.getValueAt(fila, 4).toString().replace("$", ""));
        }
    }

    private JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(COLOR_TEXTO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDES, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return campo;
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorFondo);
        boton.setForeground(COLOR_TEXTO);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(colorFondo.brighter());
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(colorFondo);
            }
        });
        return boton;
    }

    private JPanel crearFilaFormulario(String etiqueta, JTextField campo) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setBackground(COLOR_FONDO);
        
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(COLOR_TEXTO);
        
        fila.add(lbl, BorderLayout.WEST);
        fila.add(campo, BorderLayout.CENTER);
        fila.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        return fila;
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Categoría");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Precio Unitario");
        
        tabla.setModel(modeloTabla);
        
        // Estilo de la tabla
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setForeground(COLOR_TEXTO);
        tabla.getTableHeader().setBackground(COLOR_CABECERA_TABLA);
        tabla.getTableHeader().setForeground(COLOR_TEXTO);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.setRowHeight(30);
        tabla.setSelectionBackground(COLOR_SELECCION);
        tabla.setSelectionForeground(COLOR_TEXTO);
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setShowGrid(true);
        
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(COLOR_TEXTO);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(COLOR_FILA_PAR);
                    } else {
                        c.setBackground(COLOR_FILA_IMPAR);
                    }
                }
                
                if (column == 3 || column == 4) {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });
        
        // Ajustar ancho de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);
    }

    private void cargarDatosEnTabla() {
        modeloTabla.setRowCount(0);
        for (AlmacenProducto p : gestor.obtenerProductos()) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCategoria(),
                p.getCantidad(),
                String.format("$%.2f", p.getPrecio())
            });
        }
    }

    private void buscarProductoPorId() {
        String idBuscado = txtBusqueda.getText().trim();
        if (!idBuscado.isEmpty()) {
            try {
                int id = Integer.parseInt(idBuscado);
                AlmacenProducto producto = gestor.buscarProductoPorId(id);
                
                if (producto != null) {
                    for (int i = 0; i < tabla.getRowCount(); i++) {
                        if ((int) tabla.getValueAt(i, 0) == id) {
                            tabla.setRowSelectionInterval(i, i);
                            tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                            break;
                        }
                    }
                    
                    productoSeleccionadoId = producto.getId();
                    txtProducto.setText(producto.getNombre());
                    txtCategoria.setText(producto.getCategoria());
                    txtCantidad.setText(String.valueOf(producto.getCantidad()));
                    txtPrecio.setText(String.valueOf(producto.getPrecio()));
                } else {
                    mostrarMensaje("No se encontró producto con ID: " + id, "Búsqueda", COLOR_ERROR);
                }
            } catch (NumberFormatException e) {
                mostrarMensaje("El ID debe ser un número", "Error", COLOR_ERROR);
            }
        } else {
            mostrarMensaje("Ingrese un ID de producto", "Advertencia", COLOR_ERROR);
        }
    }

    private void guardarProducto() {
        if (validarCampos()) {
            try {
                String nombre = txtProducto.getText();
                String categoria = txtCategoria.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                double precio = Double.parseDouble(txtPrecio.getText());

                if (productoSeleccionadoId == -1) {
                    gestor.agregarProducto(nombre, categoria, precio, cantidad);
                    mostrarMensaje("Producto creado", "Éxito", COLOR_EXITO);
                } else {
                    gestor.actualizarProducto(productoSeleccionadoId, nombre, categoria, precio, cantidad);
                    mostrarMensaje("Producto actualizado", "Éxito", COLOR_EXITO);
                }
                
                limpiarCampos();
                cargarDatosEnTabla();
                
            } catch (NumberFormatException e) {
                mostrarMensaje("Error en formato numérico", "Error", COLOR_ERROR);
            }
        }
    }

    private void modificarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un producto", "Error", COLOR_ERROR);
            return;
        }
        productoSeleccionadoId = (int) modeloTabla.getValueAt(fila, 0);
        txtProducto.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtCategoria.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtPrecio.setText(modeloTabla.getValueAt(fila, 4).toString().replace("$", ""));
    }

    private void eliminarProducto() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            int id = (int) modeloTabla.getValueAt(fila, 0);
            int confirmacion = JOptionPane.showConfirmDialog(
                this, 
                "¿Está seguro de eliminar este producto?", 
                "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                if (gestor.eliminarProducto(id)) {
                    mostrarMensaje("Producto eliminado correctamente", "Éxito", COLOR_EXITO);
                    cargarDatosEnTabla();
                    limpiarCampos();
                } else {
                    mostrarMensaje("Error al eliminar el producto", "Error", COLOR_ERROR);
                }
            }
        } else {
            mostrarMensaje("Seleccione un producto para eliminar", "Error", COLOR_ERROR);
        }
    }

    private void limpiarCampos() {
        txtProducto.setText("");
        txtCategoria.setText("");
        txtCantidad.setText("");
        txtPrecio.setText("");
        txtBusqueda.setText("");
        productoSeleccionadoId = -1;
        tabla.clearSelection();
    }

    private boolean validarCampos() {
        if (txtProducto.getText().isEmpty() || txtCategoria.getText().isEmpty() ||
            txtCantidad.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios", "Error", COLOR_ERROR);
            return false;
        }
        
        try {
            Integer.parseInt(txtCantidad.getText());
            Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarMensaje("Cantidad y Precio deben ser números válidos", "Error", COLOR_ERROR);
            return false;
        }
        
        return true;
    }

    private void mostrarMensaje(String mensaje, String titulo, Color colorFondo) {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel label = new JLabel(mensaje);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(COLOR_TEXTO);
        panel.add(label);
        
        JOptionPane.showMessageDialog(this, panel, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}