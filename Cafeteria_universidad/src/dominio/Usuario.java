/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 * Representa un usuario del sistema 
 */
public class Usuario {
    private int id;
    private String nombreUsuario;
    private String contrasenaHash; 
    private String rol;            
    private boolean activo;
    private java.time.LocalDateTime creado;

    public Usuario(int id, String nombreUsuario, String contrasenaHash, String rol, boolean activo, java.time.LocalDateTime creado) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.rol = rol;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
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