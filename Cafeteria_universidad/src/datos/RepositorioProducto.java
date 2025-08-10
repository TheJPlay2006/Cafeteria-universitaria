
// datos/RepositorioProducto.java
package datos;

import dominio.Producto;
import infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de productos en la base de datos.
 * 
 * @author TheJPlay2006
 */
public class RepositorioProducto {

    public void guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO PRODUCTOS (nombre, precio_unitario, activo, creado) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecioUnitario());
            stmt.setBoolean(3, producto.isActivo());
            stmt.setObject(4, producto.getCreado());
            
            stmt.executeUpdate();
        }
    }   

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET nombre = ?, precio_unitario = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecioUnitario());
            stmt.setBoolean(3, producto.isActivo());
            stmt.setInt(4, producto.getId());
            
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se encontró el producto con ID: " + producto.getId());
            }
        }
    }

    public List<Producto> findAll() throws SQLException {
   String sql = "SELECT id, nombre, precio_unitario, activo, creado FROM PRODUCTOS ORDER BY id ASC";
    List<Producto> productos = new ArrayList<>();

    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Producto producto = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getDouble("precio_unitario"),
                rs.getBoolean("activo"),
                rs.getTimestamp("creado").toLocalDateTime()
            );
            productos.add(producto);
        }
    }
    return productos;
}

    public boolean existeProductoConNombre(String nombre) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRODUCTOS WHERE LOWER(nombre) = LOWER(?)";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre.trim());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
public Producto findByNombre(String nombre) throws SQLException {
    String sql = "SELECT id, nombre, precio_unitario, activo, creado FROM PRODUCTOS WHERE nombre = ? AND activo = 1";
    
    try (Connection conn = ConexionBD.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, nombre);
        
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio_unitario"),
                    rs.getBoolean("activo"),
                    rs.getTimestamp("creado").toLocalDateTime()
                );
            }
        }
    }
    return null;
}
    public void activarDesactivar(int id, boolean activo) throws SQLException {
        String sql = "UPDATE PRODUCTOS SET activo = ? WHERE id = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, activo);
            stmt.setInt(2, id);
            
            int filas = stmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se encontró el producto con ID: " + id);
            }
        }
    }


    public Producto findById(int id) throws SQLException {
        String sql = "SELECT id, nombre, precio_unitario, activo, creado FROM PRODUCTOS WHERE id = ?";
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_unitario"),
                        rs.getBoolean("activo"),
                        rs.getTimestamp("creado").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }
}