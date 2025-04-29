/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

import java.sql.Timestamp;


//datos de ventas revisar luego que todo funcione  datos ultimos
public class DatosVenta {
    private int idVenta;
    private Timestamp fecha;
    private double total;

    public DatosVenta(int idVenta, Timestamp fecha, double total) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
    }

    // Getters y Setters
    public int getIdVenta() {
        return idVenta;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }
}