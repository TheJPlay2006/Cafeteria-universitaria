/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// datos/RepositorioUsuario.java
package datos;

import dominio.Usuario;
import infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repositorio para gestionar operaciones de usuarios en la base de datos.
 * 
 * @author [Tu Nombre]
 */
public class RepositorioUsuario {
    
    /**
     * Valida las credenciales de un usuario.
     * 
     * @param username Nombre de usuario
     * @param passwordHash Hash SHA-256 de la contraseña
     * @return Usuario si las credenciales son correctas, null si no
     * @throws SQLException si ocurre un error en la base de datos
     */
    public Usuario findByUsernameAndPassword(String username, String passwordHash) throws SQLException {
        String sql = """
            SELECT id, username, rol, activo, creado 
            FROM USUARIOS 
            WHERE username = ? AND password_hash = ? AND activo = 1
            """;
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("username"),
                        passwordHash, // ya lo tenemos
                        rs.getString("rol"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creado").toLocalDateTime()
                    );
                }
            }
        }
        return null; // No encontrado o inactivo
    }

    /**
     * Verifica si un usuario existe y está activo.
     * 
     * @param username Nombre de usuario
     * @return true si existe y está activo
     * @throws SQLException
     */
    public boolean existeUsuarioActivo(String username) throws SQLException {
        String sql = "SELECT 1 FROM USUARIOS WHERE username = ? AND activo = 1";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}