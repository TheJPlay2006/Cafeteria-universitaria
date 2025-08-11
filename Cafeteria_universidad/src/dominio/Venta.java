/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.util.List;

/**
 * Representa una venta realizada por un usuario 
 */
public class Venta {
    private int id;
    private Usuario usuario;
    private java.time.LocalDateTime fechaHora;
    private double subtotal;
    private double impuestoIVA;  // 7%
    private double impuestoIVI;  // 13%
    private double descuento;
    private double total;
    private List<DetalleVenta> detalles;

    public Venta(int id, Usuario usuario, java.time.LocalDateTime fechaHora, double subtotal,
                 double impuestoIVA, double impuestoIVI, double descuento, double total,
                 List<DetalleVenta> detalles) {
        this.id = id;
        this.usuario = usuario;
        this.fechaHora = fechaHora;
        this.subtotal = subtotal;
        this.impuestoIVA = impuestoIVA;
        this.impuestoIVI = impuestoIVI;
        this.descuento = descuento;
        this.total = total;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public java.time.LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(java.time.LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestoIVA() {
        return impuestoIVA;
    }

    public void setImpuestoIVA(double impuestoIVA) {
        this.impuestoIVA = impuestoIVA;
    }

    public double getImpuestoIVI() {
        return impuestoIVI;
    }

    public void setImpuestoIVI(double impuestoIVI) {
        this.impuestoIVI = impuestoIVI;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}