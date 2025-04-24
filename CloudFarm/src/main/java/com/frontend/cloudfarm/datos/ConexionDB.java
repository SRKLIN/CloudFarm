/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    // Configuración para autenticación de Windows (como en tu SSMS)
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=CloudFarm;integratedSecurity=true;encrypt=true;trustServerCertificate=true";
    
    public static Connection getConnection() throws SQLException {
        try {
            // Carga el driver (opcional en JDBC 4.0+)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver: " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
    }

    // Método para probar la conexión
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("✅ ¡Conectado a la base de datos!");
            System.out.println("Usuario: " + conn.getMetaData().getUserName()); // Debe mostrar tu usuario Windows
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}  