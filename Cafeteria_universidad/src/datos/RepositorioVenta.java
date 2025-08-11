/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// datos/RepositorioVenta.java
package datos;

import dominio.DetalleVenta;
import dominio.Producto;
import dominio.Usuario;
import dominio.Venta;
import infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de ventas en la base de datos.
 * 
 * Esta clase se encarga de todas las operaciones relacionadas con VENTAS y DETALLES_VENTA.
 * 
 * @author [Tu Nombre]
 */
public class RepositorioVenta {

public Usuario findUsuarioPorId(int userId) throws SQLException {
    String sql = """
        SELECT id, username, password_hash, rol, activo, creado
        FROM USUARIOS
        WHERE id = ?
        """;

    try (var conn = ConexionBD.getConnection();
         var stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, userId);
        var rs = stmt.executeQuery();

        if (rs.next()) {
            return new Usuario(
                rs.getInt(1),             
                rs.getString(2),           
                rs.getString(3),        
                rs.getString(4),            
                rs.getBoolean(5),         
                rs.getTimestamp(6).toLocalDateTime() 
            );
        }
    }

    return null; // Retorna null si no se encuentra el usuario
}
    public void registrarVenta(Venta venta) throws SQLException {
        String sqlVenta = """
            INSERT INTO VENTAS (user_id, fecha_hora, subtotal, impuestoIVA, 
                               impuestoIVI, descuento, total) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlVenta, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, venta.getUsuario().getId());
            stmt.setObject(2, venta.getFechaHora());
            stmt.setDouble(3, venta.getSubtotal());
            stmt.setDouble(4, venta.getImpuestoIVA());
            stmt.setDouble(5, venta.getImpuestoIVI());
            stmt.setDouble(6, venta.getDescuento());
            stmt.setDouble(7, venta.getTotal());
            
            stmt.executeUpdate();
            
            // Obtener ID generado
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int ventaId = rs.getInt(1);
                    
                    // Guardar detalles
                    String sqlDetalle = """
                        INSERT INTO DETALLES_VENTA (venta_id, product_id, cantidad, 
                                                   precio_unit, total_linea) 
                        VALUES (?, ?, ?, ?, ?)
                        """;
                    
                    try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                        for (DetalleVenta detalle : venta.getDetalles()) {
                            stmtDetalle.setInt(1, ventaId);
                            stmtDetalle.setInt(2, detalle.getProducto().getId());
                            stmtDetalle.setInt(3, detalle.getCantidad());
                            stmtDetalle.setDouble(4, detalle.getPrecioUnitario());
                            stmtDetalle.setDouble(5, detalle.getTotalLinea());
                            stmtDetalle.addBatch();
                        }
                        stmtDetalle.executeBatch();
                    }
                }
            }
        }
    }
    
    /**
     * Obtiene todas las ventas del día actual.
     * 
     * @return Lista de ventas del día
     * @throws SQLException
     */
    public List<Venta> findVentasDelDia() throws SQLException {
        String sql = """
            SELECT v.id, v.user_id, v.fecha_hora, v.subtotal, v.impuestoIVA, 
                   v.impuestoIVI, v.descuento, v.total, u.username
            FROM VENTAS v
            JOIN USUARIOS u ON v.user_id = u.id
            WHERE CAST(v.fecha_hora AS DATE) = CAST(GETDATE() AS DATE)
            ORDER BY v.fecha_hora DESC
            """;
        
        List<Venta> ventas = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Usuario usuario = new Usuario(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    null, // password_hash (no necesario aquí)
                    null, // rol (puedes cargarlo si lo necesitas)
                    true, // activo
                    rs.getTimestamp("fecha_hora").toLocalDateTime()
                );
                
                Venta venta = new Venta(
                    rs.getInt("id"),
                    usuario,
                    rs.getTimestamp("fecha_hora").toLocalDateTime(),
                    rs.getDouble("subtotal"),
                    rs.getDouble("impuestoIVA"),
                    rs.getDouble("impuestoIVI"),
                    rs.getDouble("descuento"),
                    rs.getDouble("total"),
                    new ArrayList<>() // Detalles se cargarán después
                );
                
                // Cargar detalles
                venta.setDetalles(obtenerDetallesPorVenta(venta.getId()));
                ventas.add(venta);
            }
        }
        return ventas;
    }
    
    /**
     * Obtiene los detalles de una venta específica.
     * 
     * @param ventaId ID de la venta
     * @return Lista de detalles
     * @throws SQLException
     */
    
       public List<DetalleVenta> obtenerDetallesPorVenta(int ventaId) throws SQLException {
        String sql = """
            SELECT dv.id, dv.product_id, dv.cantidad, dv.precio_unit, dv.total_linea,
                   p.nombre as producto_nombre
            FROM DETALLES_VENTA dv
            JOIN PRODUCTOS p ON dv.product_id = p.id
            WHERE dv.venta_id = ?
            """;
        
        List<DetalleVenta> detalles = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ventaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                        rs.getInt("product_id"),
                        rs.getString("producto_nombre"),
                        rs.getDouble("precio_unit"),
                        true, // activo
                        LocalDateTime.now() // creado (no necesario para esta consulta)
                    );
                    
                    DetalleVenta detalle = new DetalleVenta(
                        rs.getInt("id"),
                        null, // venta (se asigna después)
                        producto,
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unit"),
                        rs.getDouble("total_linea")
                    );
                    
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }
    
    /**
     * Busca una venta por su ID con todos sus detalles.
     * 
     * @param id ID de la venta
     * @return Venta encontrada o null si no existe
     * @throws SQLException
     */
    public Venta findById(int id) throws SQLException {
        String sql = """
            SELECT v.id, v.user_id, v.fecha_hora, v.subtotal, v.impuestoIVA, 
                   v.impuestoIVI, v.descuento, v.total, u.username
            FROM VENTAS v
            JOIN USUARIOS u ON v.user_id = u.id
            WHERE v.id = ?
            """;
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        null, null, true,
                        rs.getTimestamp("fecha_hora").toLocalDateTime()
                    );
                    
                    Venta venta = new Venta(
                        rs.getInt("id"),
                        usuario,
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getDouble("subtotal"),
                        rs.getDouble("impuestoIVA"),
                        rs.getDouble("impuestoIVI"),
                        rs.getDouble("descuento"),
                        rs.getDouble("total"),
                        new ArrayList<>()
                    );
                    
                    venta.setDetalles(obtenerDetallesPorVenta(venta.getId()));
                    return venta;
                }
            }
        }
        return null;
    }
    
    /**
     * Obtiene los productos más vendidos del día.
     * 
     * @return Lista de objetos {nombre, cantidad_total}
     * @throws SQLException
     */
    public List<Object[]> findProductosVendidosDelDia() throws SQLException {
        String sql = """
            SELECT p.nombre, SUM(dv.cantidad) as total_cantidad
            FROM DETALLES_VENTA dv
            JOIN PRODUCTOS p ON dv.product_id = p.id
            JOIN VENTAS v ON dv.venta_id = v.id
            WHERE CAST(v.fecha_hora AS DATE) = CAST(GETDATE() AS DATE)
            GROUP BY p.nombre
            ORDER BY total_cantidad DESC
            """;
        
        List<Object[]> productos = new ArrayList<>();
        
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getString("nombre"),
                    rs.getInt("total_cantidad")
                };
                productos.add(fila);
            }
        }
        return productos;
    }
}