/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// servicio/VentaService.java
package servicio;

import datos.RepositorioVenta;
import dominio.DetalleVenta;
import dominio.Usuario;
import dominio.Venta;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VentaServicio {

    private final RepositorioVenta repositorioVenta;

    public VentaServicio() {
        this.repositorioVenta = new RepositorioVenta();
    }

    public Venta registrarVenta(Usuario usuario, List<DetalleVenta> detalles, double descuento) 
            throws IllegalArgumentException, SQLException {
        
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener al menos un ítem.");
        }
        if (descuento < 0) {
            throw new IllegalArgumentException("El descuento no puede ser negativo.");
        }

        // Calcular subtotal
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

        repositorioVenta.registrarVenta(venta);

        return venta;
    }
}