/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package almacen.cloudfarm;

import java.util.Scanner;

public class Cloudfarm {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("¿Cuál es tu nombre? ");
        String nombre = scanner.nextLine();

        System.out.println("¡Hola, " + nombre + "!");
        
        scanner.close();
    }
}
