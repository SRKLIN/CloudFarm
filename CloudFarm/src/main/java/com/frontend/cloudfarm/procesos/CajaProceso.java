/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;

import com.frontend.cloudfarm.datos.CajaDatos;
import com.frontend.cloudfarm.datos.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CajaProceso {
    
    // Método para calcular el subtotal
    public static double calcularSubtotal(int cantidad, double precio) {
        return cantidad * precio;
    }

    // Método para calcular descuento (5% si subtotal >= 20)
    public static double calcularDescuento(double subtotal) {
        return subtotal >= 20 ? subtotal * 0.05 : 0.0;
    }

    // Método para calcular el total
    public static double calcularTotal(double subtotal, double descuento) {
        return subtotal - descuento;
    }

    // Método para registrar una venta en la base de datos
    public static boolean registrarVenta(List<DetalleVenta> detalles) {
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar cabecera de venta
            String sqlVenta = "INSERT INTO Venta (Fecha, Total) VALUES (GETDATE(), ?)";
            int idVenta;
            
            try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                double total = detalles.stream().mapToDouble(d -> d.getSubtotal()).sum();
                stmtVenta.setDouble(1, total);
                stmtVenta.executeUpdate();
                
                // Obtener ID de la venta recién insertada
                ResultSet rs = stmtVenta.getGeneratedKeys();
                if (rs.next()) {
                    idVenta = rs.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID de la venta");
                }
            }

            // 2. Insertar detalles de venta y actualizar stock
            String sqlDetalle = "INSERT INTO DetalleVenta (Id_Venta, Id_Producto, Cantidad, Precio, Subtotal) VALUES (?, ?, ?, ?, ?)";
            String sqlActualizarStock = "UPDATE Producto SET Cantidad = Cantidad - ? WHERE Id_Producto = ?";
            
            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle);
                 PreparedStatement stmtStock = conn.prepareStatement(sqlActualizarStock)) {
                
                for (DetalleVenta detalle : detalles) {
                    // Insertar detalle
                    stmtDetalle.setInt(1, idVenta);
                    stmtDetalle.setString(2, detalle.getCodigoProducto());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                    stmtDetalle.setDouble(5, detalle.getSubtotal());
                    stmtDetalle.addBatch();
                    
                    // Actualizar stock
                    stmtStock.setInt(1, detalle.getCantidad());
                    stmtStock.setString(2, detalle.getCodigoProducto());
                    stmtStock.addBatch();
                }
                
                stmtDetalle.executeBatch();
                stmtStock.executeBatch();
            }
            
            conn.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar venta: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Revertir en caso de error
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    // Clase para representar los detalles de una venta
    public static class DetalleVenta {
        private String codigoProducto;
        private int cantidad;
        private double precioUnitario;
        private double subtotal;

        public DetalleVenta(String codigoProducto, int cantidad, double precioUnitario) {
            this.codigoProducto = codigoProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = cantidad * precioUnitario;
        }

        // Getters
        public String getCodigoProducto() { return codigoProducto; }
        public int getCantidad() { return cantidad; }
        public double getPrecioUnitario() { return precioUnitario; }
        public double getSubtotal() { return subtotal; }
    }
}
