/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infraestructura; 

/**
 * Clase para gestionar la conexión a la base de datos SQL Server.
 * 
 * @author TheJPlay2006
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private static final String URL = 
        "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
        "databaseName=cafeteria_universitaria;" +
        "integratedSecurity=true;" +
        "encrypt=false;" +
        "trustServerCertificate=true;";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver SQL Server no encontrado. Verifica que el JAR esté en lib.");
            throw new SQLException("Driver no disponible", e);
        }
        // Establecer la conexión
        return DriverManager.getConnection(URL);
    }
}