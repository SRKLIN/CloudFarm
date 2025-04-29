/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;
import com.frontend.cloudfarm.datos.DatosProducto;
import com.frontend.cloudfarm.datos.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesosProducto {
    private static final String TABLA = "dbo.Producto";

    public boolean agregarProducto(DatosProducto producto) {
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn = ConexionDB.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Habilitar IDENTITY_INSERT
            stmt = conn.createStatement();
            stmt.execute("SET IDENTITY_INSERT " + TABLA + " ON");
            
            // 2. Verificar si el ID ya existe
            if (existeProducto(conn, producto.getId_Producto())) {
                throw new SQLException("El ID de producto ya existe");
            }
            
            // 3. Preparar e ejecutar inserción
            String sql = "INSERT INTO " + TABLA + " (Id_Producto, Nombre, Categoria, Cantidad, Precio) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, producto.getId_Producto());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getCategoria());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setDouble(5, producto.getPrecio());
            
            int affectedRows = pstmt.executeUpdate();
            result = affectedRows > 0;
            
            // 4. Confirmar transacción si todo va bien
            conn.commit();
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Revertir en caso de error
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al agregar producto: " + e.getMessage());
            return false;
        } finally {
            // 5. Deshabilitar IDENTITY_INSERT (esto debe hacerse SIEMPRE)
            try {
                if (stmt != null) {
                    stmt.execute("SET IDENTITY_INSERT " + TABLA + " OFF");
                }
            } catch (SQLException e) {
                System.err.println("Error al deshabilitar IDENTITY_INSERT: " + e.getMessage());
            }
            
            // Cerrar recursos
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignorar */ }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { /* ignorar */ }
            try { if (conn != null) conn.close(); } catch (SQLException e) { /* ignorar */ }
        }
        
        return result;
    }

    private boolean existeProducto(Connection conn, int idProducto) throws SQLException {
        String sql = "SELECT 1 FROM " + TABLA + " WHERE Id_Producto = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Retorna true si ya existe
            }
        }
    }

    public DatosProducto buscarProducto(int idProducto) {
        String sql = "SELECT * FROM " + TABLA + " WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new DatosProducto(
                    rs.getInt("Id_Producto"),
                    rs.getString("Nombre"),
                    rs.getString("Categoria"),
                    rs.getInt("Cantidad"),
                    rs.getDouble("Precio")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    public boolean modificarProducto(DatosProducto producto) {
        String sql = "UPDATE " + TABLA + " SET Nombre = ?, Categoria = ?, Cantidad = ?, Precio = ? WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getCategoria());
            pstmt.setInt(3, producto.getCantidad());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setInt(5, producto.getId_Producto());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "DELETE FROM " + TABLA + " WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idProducto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    public List<DatosProducto> obtenerTodos() {
        List<DatosProducto> productos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLA;
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                productos.add(new DatosProducto(
                    rs.getInt("Id_Producto"),
                    rs.getString("Nombre"),
                    rs.getString("Categoria"),
                    rs.getInt("Cantidad"),
                    rs.getDouble("Precio")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }
}
    

