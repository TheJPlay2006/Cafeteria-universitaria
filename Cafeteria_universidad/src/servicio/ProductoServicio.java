/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// servicio/ProductoService.java
package servicio;

import datos.RepositorioProducto;
import dominio.Producto;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author TheJPlay2006
 */
public class ProductoServicio {

    private final RepositorioProducto repositorioProducto;

    public ProductoServicio() {
        this.repositorioProducto = new RepositorioProducto();
    }


    public void guardarProducto(String nombre, double precio, boolean activo, Producto productoExistente) 
            throws IllegalArgumentException, SQLException {
        
        // Validaciones de negocio
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (precio > 1000) {
            throw new IllegalArgumentException("El precio no puede exceder $1000.");
        }

        // Verificar duplicados (solo si es nuevo o cambia el nombre)
        if (productoExistente == null || 
            !productoExistente.getNombre().equals(nombre)) {
            if (repositorioProducto.existeProductoConNombre(nombre.trim())) {
                throw new IllegalArgumentException("Ya existe un producto con ese nombre.");
            }
        }

        // Crear o actualizar
        Producto producto;
        if (productoExistente == null) {
            // Nuevo producto
            producto = new Producto(0, nombre.trim(), precio, activo, LocalDateTime.now());
            repositorioProducto.guardar(producto);
        } else {
            // Producto existente
            producto = new Producto(
                productoExistente.getId(),
                nombre.trim(),
                precio,
                activo,
                productoExistente.getCreado()
            );
            repositorioProducto.actualizar(producto);
        }
    }


    public List<Producto> listarProductos() throws SQLException {
        return repositorioProducto.findAll();
    }

    public void activarDesactivarProducto(int id, boolean activo) throws SQLException {
        repositorioProducto.activarDesactivar(id, activo);
    }


    public Producto obtenerProductoPorId(int id) throws SQLException {
        return repositorioProducto.findById(id);
    }
}