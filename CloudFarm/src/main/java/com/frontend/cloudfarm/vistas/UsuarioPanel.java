/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import com.frontend.cloudfarm.datos.DatosUsuario;
import com.frontend.cloudfarm.procesos.ProcesosUsuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UsuarioPanel extends JPanel {
    private final Color COLOR_FONDO = new Color(240, 248, 255);
    private final Color COLOR_BOTON_PRINCIPAL = new Color(0, 112, 192);
    private final Color COLOR_BOTON_SECUNDARIO = new Color(70, 130, 180);
    private final Color COLOR_TEXTO_BOTON = Color.BLACK;
    private final Color COLOR_BORDES = new Color(150, 180, 210);
    private final Color COLOR_ERROR = new Color(255, 220, 220);
    private final Color COLOR_EXITO = new Color(220, 255, 220);
    
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar, txtUsuario, txtCargo, txtContraseña;
    private JButton btnAgregar, btnEditar, btnEliminar, btnLimpiar, btnBuscar;
    private ProcesosUsuario procesos;
    
    public UsuarioPanel() {
        procesos = new ProcesosUsuario();
        setBackground(COLOR_FONDO);
        initComponents();
        setupActions();
        cargarUsuarios();
        applyStyles();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior - Busqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setBackground(COLOR_FONDO);
        
        txtBuscar = createStyledTextField(20);
        btnBuscar = createFarmaButton("Buscar", COLOR_BOTON_SECUNDARIO);
        
        panelBusqueda.add(new JLabel("Buscar usuario:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        
        // Panel central - Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Usuario", "Cargo", "Contraseña"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaUsuarios.getTableHeader().setReorderingAllowed(false);
        tablaUsuarios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tablaUsuarios.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDES));
        
        // Panel inferior - Formulario y botones
        JPanel panelFormulario = new JPanel(new GridLayout(1, 2, 15, 0));
        panelFormulario.setBackground(COLOR_FONDO);
        
        // Panel de campos
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.setBackground(COLOR_FONDO);
        panelCampos.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        
        txtUsuario = createStyledTextField(15);
        txtCargo = createStyledTextField(15);
        txtContraseña = createStyledTextField(15);
        
        panelCampos.add(new JLabel("Usuario:"));
        panelCampos.add(txtUsuario);
        panelCampos.add(new JLabel("Cargo:"));
        panelCampos.add(txtCargo);
        panelCampos.add(new JLabel("Contraseña:"));
        panelCampos.add(txtContraseña);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 10, 10));
        panelBotones.setBackground(COLOR_FONDO);
        
        btnAgregar = createFarmaButton("Agregar Usuario", COLOR_BOTON_PRINCIPAL);
        btnEditar = createFarmaButton("Editar Usuario", COLOR_BOTON_SECUNDARIO);
        btnEliminar = createFarmaButton("Eliminar Usuario", COLOR_BOTON_SECUNDARIO);
        btnLimpiar = createFarmaButton("Limpiar Campos", COLOR_BOTON_SECUNDARIO);
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        
        panelFormulario.add(panelCampos);
        panelFormulario.add(panelBotones);
        
        // Agregar componentes al panel principal
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.SOUTH);
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
        btnAgregar.addActionListener(this::agregarUsuario);
        btnEditar.addActionListener(this::editarUsuario);
        btnEliminar.addActionListener(this::eliminarUsuario);
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(this::buscarUsuario);
        
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarUsuarioSeleccionado();
            }
        });
    }
    
    private void cargarUsuarios() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<DatosUsuario> usuarios = procesos.obtenerTodosUsuarios();
        
        for (DatosUsuario usuario : usuarios) {
            modeloTabla.addRow(new Object[]{
                usuario.getId_usuario(),
                usuario.getUsuario(),
                usuario.getCargo(),
                "••••••••" // No mostrar contraseña real
            });
        }
    }
    
    private void cargarUsuarioSeleccionado() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            DatosUsuario usuario = procesos.buscarUsuarioPorId(idUsuario);
            
            if (usuario != null) {
                txtUsuario.setText(usuario.getUsuario());
                txtCargo.setText(usuario.getCargo());
                txtContraseña.setText(usuario.getContraseña());
            }
        }
    }
    
    private void agregarUsuario(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String cargo = txtCargo.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        
        if (usuario.isEmpty() || cargo.isEmpty() || contraseña.isEmpty()) {
            showMessage("Complete todos los campos", "Validación", COLOR_ERROR);
            return;
        }
        
        DatosUsuario nuevoUsuario = new DatosUsuario(0, usuario, cargo, contraseña);
        
        if (procesos.agregarUsuario(nuevoUsuario)) {
            showMessage("Usuario agregado correctamente", "Éxito", COLOR_EXITO);
            cargarUsuarios();
            limpiarCampos();
        } else {
            showMessage("Error al agregar usuario", "Error", COLOR_ERROR);
        }
    }
    
    private void editarUsuario(ActionEvent e) {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            showMessage("Seleccione un usuario para editar", "Validación", COLOR_ERROR);
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String usuario = txtUsuario.getText().trim();
        String cargo = txtCargo.getText().trim();
        String contraseña = txtContraseña.getText().trim();
        
        if (usuario.isEmpty() || cargo.isEmpty() || contraseña.isEmpty()) {
            showMessage("Complete todos los campos", "Validación", COLOR_ERROR);
            return;
        }
        
        DatosUsuario usuarioEditado = new DatosUsuario(idUsuario, usuario, cargo, contraseña);
        
        if (procesos.modificarUsuario(usuarioEditado)) {
            showMessage("Usuario modificado correctamente", "Éxito", COLOR_EXITO);
            cargarUsuarios();
        } else {
            showMessage("Error al modificar usuario", "Error", COLOR_ERROR);
        }
    }
    
    private void eliminarUsuario(ActionEvent e) {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada < 0) {
            showMessage("Seleccione un usuario para eliminar", "Validación", COLOR_ERROR);
            return;
        }
        
        int idUsuario = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar este usuario?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (procesos.eliminarUsuario(idUsuario)) {
                showMessage("Usuario eliminado correctamente", "Éxito", COLOR_EXITO);
                cargarUsuarios();
                limpiarCampos();
            } else {
                showMessage("Error al eliminar usuario", "Error", COLOR_ERROR);
            }
        }
    }
    
    private void buscarUsuario(ActionEvent e) {
        String textoBusqueda = txtBuscar.getText().trim().toLowerCase();
        
        if (textoBusqueda.isEmpty()) {
            cargarUsuarios();
            return;
        }
        
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<DatosUsuario> usuarios = procesos.obtenerTodosUsuarios();
        
        for (DatosUsuario usuario : usuarios) {
            if (usuario.getUsuario().toLowerCase().contains(textoBusqueda) ||
                usuario.getCargo().toLowerCase().contains(textoBusqueda)) {
                
                modeloTabla.addRow(new Object[]{
                    usuario.getId_usuario(),
                    usuario.getUsuario(),
                    usuario.getCargo(),
                    "••••••••"
                });
            }
        }
    }
    
    private void limpiarCampos() {
        txtUsuario.setText("");
        txtCargo.setText("");
        txtContraseña.setText("");
        tablaUsuarios.clearSelection();
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