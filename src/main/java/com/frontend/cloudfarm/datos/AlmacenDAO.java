/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.datos;

import java.util.ArrayList;
import java.util.List;

public class AlmacenDAO {
    private List<AlmacenProducto> productos = new ArrayList<>();
    private int ultimoId = 0;

    public AlmacenDAO() {
        // Datos de prueba iniciales
        agregarProducto(new AlmacenProducto(0, "Paracetamol", "Analgésicos", 5.99, 100));
        agregarProducto(new AlmacenProducto(0, "Ibuprofeno", "Antiinflamatorios", 7.50, 50));
        agregarProducto(new AlmacenProducto(0, "Ciprofloxacino", "Antibióticos", 14.90, 70));
        agregarProducto(new AlmacenProducto(0, "Metoclopramida", "Antieméticos", 5.45, 120));
        agregarProducto(new AlmacenProducto(0, "Warfarina", "Anticoagulantes", 19.30, 35));
        agregarProducto(new AlmacenProducto(0, "Fluoxetina", "Antidepresivos", 17.25, 55));
        agregarProducto(new AlmacenProducto(0, "Hidroclorotiazida", "Diuréticos", 8.75, 90));
        agregarProducto(new AlmacenProducto(0, "Mometasona", "Corticosteroides", 23.60, 40));
        agregarProducto(new AlmacenProducto(0, "Amlodipino", "Antihipertensivos", 10.20, 130));
        agregarProducto(new AlmacenProducto(0, "Ondansetrón", "Antieméticos", 25.00, 60));
        agregarProducto(new AlmacenProducto(0, "Insulina Glargina", "Antidiabéticos", 89.99, 25));
        agregarProducto(new AlmacenProducto(0, "Dexametasona", "Antiinflamatorios", 12.30, 85));
        agregarProducto(new AlmacenProducto(0, "Levofloxacino", "Antibióticos", 16.50, 65));
        agregarProducto(new AlmacenProducto(0, "Tamsulosina", "Urológicos", 21.75, 45));
        agregarProducto(new AlmacenProducto(0, "Mirtazapina", "Antidepresivos", 18.40, 50));
        agregarProducto(new AlmacenProducto(0, "Esomeprazol", "Antiulcerosos", 11.90, 100));
        agregarProducto(new AlmacenProducto(0, "Aciclovir", "Antivirales", 27.80, 30));
        agregarProducto(new AlmacenProducto(0, "Carbamazepina", "Anticonvulsivos", 15.60, 40));
        agregarProducto(new AlmacenProducto(0, "Venlafaxina", "Antidepresivos", 20.10, 60));
        agregarProducto(new AlmacenProducto(0, "Ranitidina", "Antiulcerosos", 6.80, 150));
        agregarProducto(new AlmacenProducto(0, "Cetirizina", "Antialérgicos", 5.25, 180));
        agregarProducto(new AlmacenProducto(0, "Nistatina", "Antimicóticos", 9.40, 75));
        agregarProducto(new AlmacenProducto(0, "Furosemida", "Diuréticos", 4.95, 200)); 
        agregarProducto(new AlmacenProducto(0, "Quetiapina", "Antipsicóticos", 22.90, 35));
        agregarProducto(new AlmacenProducto(0, "Vitamina B12", "Suplementos", 7.10, 300));
        agregarProducto(new AlmacenProducto(0, "Lansoprazol", "Antiulcerosos", 8.45, 110));
        agregarProducto(new AlmacenProducto(0, "Levonorgestrel", "Anticonceptivos", 29.99, 20));
    }

    public boolean agregarProducto(AlmacenProducto producto) {
        producto.setId(++ultimoId);
        return productos.add(producto);
    }

    public List<AlmacenProducto> obtenerTodos() {
        return new ArrayList<>(productos);
    }

    public boolean actualizarProducto(int id, String nombre, String categoria, double precio, int cantidad) {
        for (AlmacenProducto p : productos) {
            if (p.getId() == id) {
                p.setNombre(nombre);
                p.setCategoria(categoria);
                p.setPrecio(precio);
                p.setCantidad(cantidad);
                return true;
            }
        }
        return false;
    }
    public AlmacenProducto buscarPorNombre(String nombre) {
    for (AlmacenProducto p : productos) {
        if (p.getNombre().equalsIgnoreCase(nombre)) { // Búsqueda sin distinción de mayúsculas
            return p;
        }
    }
    return null;
}

    public boolean eliminarProducto(int id) {
    // Verificar si existe el producto antes de eliminar
    boolean existe = productos.stream().anyMatch(p -> p.getId() == id);
    
    if (existe) {
        productos.removeIf(p -> p.getId() == id);
        return true;
    }
    return false;
}
  
}
