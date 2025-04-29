/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class CajaDatos {
    private static HashMap<String, String[]> cacheProductos = new HashMap<>();

    // Método para verificar si un producto existe en la base de datos
    public static boolean existeProducto(String codigo) {
        // Primero verificar en caché
        if (cacheProductos.containsKey(codigo)) {
            return true;
        }
        
        // Si no está en caché, consultar la base de datos
        String sql = "SELECT Id_Producto, Nombre, Precio FROM Producto WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Agregar a caché para futuras consultas
                String nombre = rs.getString("Nombre");
                String precio = String.valueOf(rs.getDouble("Precio"));
                cacheProductos.put(codigo, new String[]{nombre, precio});
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar producto: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener datos básicos del producto
    public static String[] obtenerProducto(String codigo) {
        if (cacheProductos.containsKey(codigo)) {
            return cacheProductos.get(codigo);
        }
        
        String sql = "SELECT Nombre, Precio FROM Producto WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("Nombre");
                String precio = String.valueOf(rs.getDouble("Precio"));
                String[] datos = {nombre, precio};
                cacheProductos.put(codigo, datos);
                return datos;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto: " + e.getMessage());
        }
        return null;
    }

    // Método para obtener un producto con stock
    public static Producto buscarProducto(String codigo) {
        String sql = "SELECT Id_Producto, Nombre, Precio, Cantidad FROM Producto WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String id = rs.getString("Id_Producto");
                String nombre = rs.getString("Nombre");
                double precio = rs.getDouble("Precio");
                int stock = rs.getInt("Cantidad");
                return new Producto(id, nombre, precio, stock);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
        }
        return null;
    }

    // Método para actualizar un producto
    public static boolean modificarProducto(String codigo, String nombre, double precio) {
        String sql = "UPDATE Producto SET Nombre = ?, Precio = ? WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setDouble(2, precio);
            stmt.setString(3, codigo);
            
            int affectedRows = stmt.executeUpdate();
            
            // Actualizar caché si la modificación fue exitosa
            if (affectedRows > 0) {
                cacheProductos.put(codigo, new String[]{nombre, String.valueOf(precio)});
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar producto: " + e.getMessage());
        }
        return false;
    }

    // Método para eliminar un producto
    public static boolean eliminarProducto(String codigo) {
        String sql = "DELETE FROM Producto WHERE Id_Producto = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            int affectedRows = stmt.executeUpdate();
            
            // Eliminar de caché si la eliminación fue exitosa
            if (affectedRows > 0) {
                cacheProductos.remove(codigo);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return false;
    }

    // Método para descontar stock al realizar una venta
    public static boolean descontarStock(String codigo, int cantidad) {
        String sql = "UPDATE Producto SET Cantidad = Cantidad - ? WHERE Id_Producto = ? AND Cantidad >= ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cantidad);
            stmt.setString(2, codigo);
            stmt.setInt(3, cantidad);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al descontar stock: " + e.getMessage());
        }
        return false;
    }

    // Clase interna Producto
    public static class Producto {
        private String codigo;
        private String nombre;
        private double precio;
        private int stock;

        public Producto(String codigo, String nombre, double precio, int stock) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }

        // Getters
        public String getCodigo() { return codigo; }
        public String getNombre() { return nombre; }
        public double getPrecio() { return precio; }
        public int getStock() { return stock; }
    }
}