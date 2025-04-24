/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;

import com.frontend.cloudfarm.datos.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcesoLogin {
    public static String validarUsuario(String usuario, String contraseña) {
        String cargo = null;
        String query = "SELECT Cargo FROM Usuarios WHERE Usuario = ? AND Contraseña = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                cargo = rs.getString("Cargo");
            }
        } catch (SQLException e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return cargo; // Retorna "gerente", "cajero", o null si falla para proceder en el ejecutable
    }
}
