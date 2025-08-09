/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 * Representa un producto disponible en la cafetería.
 */
public class Producto {
    private int id;
    private String nombre;
    private double precioUnitario;
    private boolean activo;
    private java.time.LocalDateTime creado;

    public Producto(int id, String nombre, double precioUnitario, boolean activo, java.time.LocalDateTime creado) {
        this.id = id;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.activo = activo;
        this.creado = creado;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public java.time.LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(java.time.LocalDateTime creado) {
        this.creado = creado;
    }
}