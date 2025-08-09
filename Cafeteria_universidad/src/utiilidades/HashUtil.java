/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// utilidades/HashUtil.java
package utiilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para generar hashes criptográficos.
 * 
 * @author [Tu Nombre]
 */
public class HashUtil {
    
    /**
     * Genera el hash SHA-256 de una cadena de texto.
     * 
     * @param input Texto a encriptar (ej: contraseña)
     * @return Hash en formato hexadecimal (64 caracteres)
     */
    public static String sha256(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("La entrada no puede ser nula o vacía.");
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar SHA-256", e);
        }
    }
}