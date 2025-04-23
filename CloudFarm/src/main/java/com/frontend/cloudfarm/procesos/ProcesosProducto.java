/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.procesos;
import com.frontend.cloudfarm.datos.DatosProducto;
import java.util.ArrayList;
import java.util.List;

public class ProcesosProducto {

    // Lista que simula una base de datos de productos
    private List<DatosProducto> productos;

    public ProcesosProducto() {
        // Inicializamos la lista de productos
        productos = new ArrayList<>();
    }

    // Método para agregar un producto
    public void agregarProducto(DatosProducto producto) {
        productos.add(producto);
    }

    // Método para modificar un producto
    public boolean modificarProducto(DatosProducto producto) {
        // Verificamos si el producto existe en la lista
        for (int i = 0; i < productos.size(); i++) {
            DatosProducto productoExistente = productos.get(i);
            if (productoExistente.getCodigo().equals(producto.getCodigo())) {
                // Si el producto existe, lo modificamos
                productos.set(i, producto);
                return true; // Se modificó correctamente
            }
        }
        return false; // No se encontró el producto para modificar
    }

    // Método para eliminar un producto
    public boolean eliminarProducto(DatosProducto producto) {
        return productos.remove(producto);
    }

    // Otros métodos si es necesario...

}
    

