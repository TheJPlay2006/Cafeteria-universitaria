package servicio;

import datos.RepositorioVenta;
import dominio.DetalleVenta;
import dominio.Usuario;
import dominio.Venta;
import infraestructura.ConexionBD;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar la lógica de negocio de las ventas.
 */
public class VentaServicio {

    private final RepositorioVenta repositorioVenta;

    public VentaServicio() {
        this.repositorioVenta = new RepositorioVenta();
    }

    /**
     * Registra una nueva venta en el sistema.
     *
     * @param usuario   El usuario que realiza la venta.
     * @param detalles  Los detalles de la venta (productos y cantidades).
     * @param descuento El descuento aplicado a la venta.
     * @return La venta registrada.
     * @throws IllegalArgumentException Si los datos proporcionados son inválidos.
     * @throws SQLException            Si ocurre un error al acceder a la base de datos.
     */
    public Venta registrarVenta(Usuario usuario, List<DetalleVenta> detalles, double descuento)
            throws IllegalArgumentException, SQLException {

        // Validaciones de entrada
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un ítem.");
        }
        if (descuento < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo.");
        }

        // Calcular subtotal y validar detalles
        double subtotal = 0.0;
        for (DetalleVenta dv : detalles) {
            if (dv.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
            }
            if (dv.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0.");
            }
            dv.setTotalLinea(dv.getCantidad() * dv.getPrecioUnitario());
            subtotal += dv.getTotalLinea();
        }

        if (subtotal <= 0) {
            throw new IllegalArgumentException("El subtotal debe ser mayor a 0.");
        }

        if (descuento > subtotal) {
            throw new IllegalArgumentException("El descuento no puede ser mayor al subtotal.");
        }

        // Calcular impuestos
        double impuestoIVA = subtotal * 0.07;  // 7%
        double impuestoIVI = subtotal * 0.13;  // 13%
        double total = subtotal + impuestoIVA + impuestoIVI - descuento;

        // Crear objeto Venta
        Venta venta = new Venta(
                0, // ID será generado por la BD
                usuario,
                LocalDateTime.now(),
                subtotal,
                impuestoIVA,
                impuestoIVI,
                descuento,
                total,
                detalles
        );

        // Registrar la venta en la base de datos
        repositorioVenta.registrarVenta(venta);

        return venta;
    }

    /**
     * Obtiene todas las ventas registradas para el día actual.
     *
     * @return Lista de ventas del día.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
   public List<Venta> obtenerVentasDelDia() throws SQLException {
    String sql = """
        SELECT v.id, v.user_id, v.fecha_hora, v.subtotal, v.impuestoIVA, 
               v.impuestoIVI, v.descuento, v.total
        FROM VENTAS v
        WHERE CAST(v.fecha_hora AS DATE) = CAST(GETDATE() AS DATE)
        ORDER BY v.fecha_hora DESC
        """;

    List<Venta> ventas = new ArrayList<>();

    try (var conn = ConexionBD.getConnection();
         var stmt = conn.prepareStatement(sql);
         var rs = stmt.executeQuery()) {

        while (rs.next()) {
            int userId = rs.getInt("user_id");

            // Cargar el objeto Usuario completo
            Usuario usuario = repositorioVenta.findUsuarioPorId(userId);
            if (usuario == null) {
                throw new SQLException("No se encontró el usuario con ID: " + userId);
            }

            // Crear el objeto Venta
            Venta venta = new Venta(
                rs.getInt("id"),
                usuario,
                rs.getTimestamp("fecha_hora").toLocalDateTime(),
                rs.getDouble("subtotal"),
                rs.getDouble("impuestoIVA"),
                rs.getDouble("impuestoIVI"),
                rs.getDouble("descuento"),
                rs.getDouble("total"),
                null 
            );

            ventas.add(venta);
        }
    }

    return ventas;
}
    public Venta obtenerVentaConDetalles(int id) throws SQLException {
        return repositorioVenta.findById(id);
    }

    public List<Object[]> getProductosVendidosDelDia() throws SQLException {
        return repositorioVenta.findProductosVendidosDelDia();
    }
}