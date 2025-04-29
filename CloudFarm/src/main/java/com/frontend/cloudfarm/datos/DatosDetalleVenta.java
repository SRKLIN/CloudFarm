/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;


//ira conjunta con las ventas todo junto en el proceso izi

public class DatosDetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DatosDetalleVenta(int idDetalle, int idVenta, int idProducto, String nombreProducto, int cantidad, double precio, double subtotal) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    // Getters
    public int getIdVenta() {
        return idVenta;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public double getSubtotal() {
        return subtotal;
    }
}
