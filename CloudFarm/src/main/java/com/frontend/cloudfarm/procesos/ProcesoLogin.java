/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;

import com.frontend.cloudfarm.datos.ConexionDB;
import java.sql.*;

public class ProcesoLogin {
    public static String validarUsuario(String usuario, String contraseña) throws SQLException {
        String sql = "SELECT Cargo FROM Usuarios WHERE Usuario = ? AND Contraseña = ?";
        
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario);
            stmt.setString(2, contraseña);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Cargo");
            }
        }
        return null;
    }
}
