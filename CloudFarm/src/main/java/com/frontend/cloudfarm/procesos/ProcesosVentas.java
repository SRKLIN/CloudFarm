/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;


//espero funcione 
// se importan todos los datos pero se puede modificar especificamente a las 2 que utilizaremos revisar luego
//se unen Datos ventas y Datos Detalle ventas 
import com.frontend.cloudfarm.datos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesosVentas {
    private static final String TABLA_VENTA = "dbo.Venta";
    private static final String TABLA_DETALLE = "dbo.DetalleVenta";
    private static final String TABLA_PRODUCTO = "dbo.Producto";

    public List<DatosVenta> obtenerTodasVentas() {
        List<DatosVenta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLA_VENTA + " ORDER BY Fecha DESC";
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ventas.add(new DatosVenta(
                    rs.getInt("Id_Venta"),
                    rs.getTimestamp("Fecha"),
                    rs.getDouble("Total")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas: " + e.getMessage());
        }
        return ventas;
    }

    public List<DatosDetalleVenta> obtenerDetallesVenta(int idVenta) {
        List<DatosDetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT d.*, p.Nombre as NombreProducto FROM " + TABLA_DETALLE + " d " +
                     "JOIN " + TABLA_PRODUCTO + " p ON d.Id_Producto = p.Id_Producto " +
                     "WHERE d.Id_Venta = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idVenta);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                detalles.add(new DatosDetalleVenta(
                    rs.getInt("Id_Detalle"),
                    rs.getInt("Id_Venta"),
                    rs.getInt("Id_Producto"),
                    rs.getString("NombreProducto"),
                    rs.getInt("Cantidad"),
                    rs.getDouble("Precio"),
                    rs.getDouble("Subtotal")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles: " + e.getMessage());
        }
        return detalles;
    }

    public boolean registrarVenta(DatosVenta venta, List<DatosDetalleVenta> detalles) {
        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtDetalle = null;
        boolean resultado = false;
        
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Registrar la venta
            String sqlVenta = "INSERT INTO " + TABLA_VENTA + " (Fecha, Total) VALUES (?, ?)";
            pstmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            pstmtVenta.setTimestamp(1, venta.getFecha());
            pstmtVenta.setDouble(2, venta.getTotal());
            
            int affectedRows = pstmtVenta.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No se pudo registrar la venta");
            }
            
            // Obtener el ID generado
            int idVenta;
            try (ResultSet generatedKeys = pstmtVenta.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idVenta = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se obtuvo el ID de la venta");
                }
            }
            
            // 2. Registrar los detalles
            String sqlDetalle = "INSERT INTO " + TABLA_DETALLE + 
                               " (Id_Venta, Id_Producto, Cantidad, Precio, Subtotal) VALUES (?, ?, ?, ?, ?)";
            pstmtDetalle = conn.prepareStatement(sqlDetalle);
            
            for (DatosDetalleVenta detalle : detalles) {
                pstmtDetalle.setInt(1, idVenta);
                pstmtDetalle.setInt(2, detalle.getIdProducto());
                pstmtDetalle.setInt(3, detalle.getCantidad());
                pstmtDetalle.setDouble(4, detalle.getPrecio());
                pstmtDetalle.setDouble(5, detalle.getSubtotal());
                pstmtDetalle.addBatch();
            }
            
            pstmtDetalle.executeBatch();
            conn.commit(); // Confirmar transacción
            resultado = true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al registrar venta: " + e.getMessage());
        } finally {
            try { if (pstmtDetalle != null) pstmtDetalle.close(); } catch (SQLException e) { /* ignorar */ }
            try { if (pstmtVenta != null) pstmtVenta.close(); } catch (SQLException e) { /* ignorar */ }
            try { if (conn != null) conn.close(); } catch (SQLException e) { /* ignorar */ }
        }
        
        return resultado;
    }
}