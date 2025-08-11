package Main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author jh599
 */

import gui.gui_login;  // ajusta el nombre y paquete según tengas tu clase

public class Main {

    public static void main(String[] args) {
        // Ejecutar la GUI en el hilo de eventos Swing
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gui_login login = new gui_login();
                login.setVisible(true);
                login.setLocationRelativeTo(null);  // centra la ventana
            }
        });
    }
}
