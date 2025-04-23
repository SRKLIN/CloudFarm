/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;

import com.frontend.cloudfarm.datos.AlmacenDAO;
import com.frontend.cloudfarm.datos.AlmacenProducto;
import java.util.List;

public class AlmacenGestor {
    private AlmacenDAO dao = new AlmacenDAO();

    public boolean agregarProducto(String nombre, String categoria, double precio, int cantidad) {
        return dao.agregarProducto(new AlmacenProducto(0, nombre, categoria, precio, cantidad));
    }

    public List<AlmacenProducto> obtenerProductos() {
        return dao.obtenerTodos();
    }

    public boolean actualizarProducto(int id, String nombre, String categoria, double precio, int cantidad) {
        return dao.actualizarProducto(id, nombre, categoria, precio, cantidad);
    }

    public AlmacenProducto buscarProductoPorNombre(String nombre) {
    return dao.buscarPorNombre(nombre);
}
    
   public boolean eliminarProducto(int id) {
    return dao.eliminarProducto(id); // Debe retornar true si se eliminó
}
}