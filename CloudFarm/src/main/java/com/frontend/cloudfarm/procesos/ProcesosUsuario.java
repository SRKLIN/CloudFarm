/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;

import com.frontend.cloudfarm.datos.DatosUsuario;
import com.frontend.cloudfarm.datos.ConexionDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesosUsuario {
    private static final String TABLA = "dbo.Usuarios";

    public boolean agregarUsuario(DatosUsuario usuario) {
        String sql = "INSERT INTO " + TABLA + " (Usuario, Cargo, Contraseña) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsuario());
            pstmt.setString(2, usuario.getCargo());
            pstmt.setString(3, usuario.getContraseña());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarUsuario(DatosUsuario usuario) {
        String sql = "UPDATE " + TABLA + " SET Usuario = ?, Cargo = ?, Contraseña = ? WHERE id_usuario = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getUsuario());
            pstmt.setString(2, usuario.getCargo());
            pstmt.setString(3, usuario.getContraseña());
            pstmt.setInt(4, usuario.getId_usuario());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al modificar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM " + TABLA + " WHERE id_usuario = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<DatosUsuario> obtenerTodosUsuarios() {
        List<DatosUsuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLA;
        
        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(new DatosUsuario(
                    rs.getInt("id_usuario"),
                    rs.getString("Usuario"),
                    rs.getString("Cargo"),
                    rs.getString("Contraseña")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public DatosUsuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM " + TABLA + " WHERE id_usuario = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new DatosUsuario(
                    rs.getInt("id_usuario"),
                    rs.getString("Usuario"),
                    rs.getString("Cargo"),
                    rs.getString("Contraseña")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }
}