/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// datos/RepositorioVenta.java
package datos;

import dominio.DetalleVenta;
import dominio.Venta;
import infraestructura.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepositorioVenta {
    
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
            try (var rs = stmt.getGeneratedKeys()) {
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
}