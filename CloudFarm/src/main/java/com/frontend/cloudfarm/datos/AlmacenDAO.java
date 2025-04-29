/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlmacenDAO {
    private static final String SQL_SELECT = "SELECT Id_Producto, Nombre, Categoria, Cantidad, Precio FROM Producto";
    private static final String SQL_INSERT = "INSERT INTO Producto(Nombre, Categoria, Cantidad, Precio) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE Producto SET Nombre=?, Categoria=?, Cantidad=?, Precio=? WHERE Id_Producto=?";
    private static final String SQL_DELETE = "DELETE FROM Producto WHERE Id_Producto=?";
    private static final String SQL_SELECT_BY_ID = "SELECT Id_Producto, Nombre, Categoria, Cantidad, Precio FROM Producto WHERE Id_Producto=?";
    private static final String SQL_SELECT_BY_NAME = "SELECT Id_Producto, Nombre, Categoria, Cantidad, Precio FROM Producto WHERE Nombre LIKE ?";

    public List<AlmacenProducto> obtenerTodos() {
        List<AlmacenProducto> productos = new ArrayList<>();
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                int id = rs.getInt("Id_Producto");
                String nombre = rs.getString("Nombre");
                String categoria = rs.getString("Categoria");
                int cantidad = rs.getInt("Cantidad");
                double precio = rs.getDouble("Precio");
                
                productos.add(new AlmacenProducto(id, nombre, categoria, precio, cantidad));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return productos;
    }

    public boolean agregarProducto(AlmacenProducto producto) {
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setInt(3, producto.getCantidad());
            stmt.setDouble(4, producto.getPrecio());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean actualizarProducto(int id, String nombre, String categoria, double precio, int cantidad) {
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, categoria);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, precio);
            stmt.setInt(5, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int id) {
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public AlmacenProducto buscarPorId(int id) {
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nombre = rs.getString("Nombre");
                    String categoria = rs.getString("Categoria");
                    int cantidad = rs.getInt("Cantidad");
                    double precio = rs.getDouble("Precio");
                    
                    return new AlmacenProducto(id, nombre, categoria, precio, cantidad);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public AlmacenProducto buscarPorNombre(String nombre) {
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_NAME)) {
            
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("Id_Producto");
                    String nombreProd = rs.getString("Nombre");
                    String categoria = rs.getString("Categoria");
                    int cantidad = rs.getInt("Cantidad");
                    double precio = rs.getDouble("Precio");
                    
                    return new AlmacenProducto(id, nombreProd, categoria, precio, cantidad);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}