/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

/**
 *
 * @author anton
 */
public class DatosProducto {
    private int Id_Producto;
    private String Nombre;
    private String Categoria;
    private int Cantidad;
    private double Precio;

    public DatosProducto(int Id_Producto, String Nombre, String Categoria, int Cantidad, double Precio) {
        this.Id_Producto = Id_Producto;
        this.Nombre = Nombre;
        this.Categoria = Categoria;
        this.Cantidad = Cantidad;
        this.Precio = Precio;
    }

    public int getId_Producto() {
        return Id_Producto;
    }

    public void setId_Producto(int Id_Producto) {
        this.Id_Producto = Id_Producto;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double Precio) {
        this.Precio = Precio;
    }

    public Object getIdProducto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}