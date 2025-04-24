/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.vistas;

import java.awt.Font;
import javax.swing.*;

public class CajaPanel extends JFrame {
    public CajaPanel() {
        setTitle("CloudFarm - Modo Cajero");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel lblBienvenida = new JLabel("Bienvenido, Cajero", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblBienvenida);
    }
}