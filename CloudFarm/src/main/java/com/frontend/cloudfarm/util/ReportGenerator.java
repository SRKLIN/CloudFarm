/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.frontend.cloudfarm.util;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import javax.swing.JOptionPane;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import net.sf.jasperreports.view.JasperViewer;

public class ReportGenerator {

    public static void generarFactura(List<Map<String, Object>> datosFactura, String total) {
        InputStream reportStream = null;
        try {
            // 1. Cargar reporte compilado
            reportStream = ReportGenerator.class.getResourceAsStream("/reportes/factura.jasper");
            
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, 
                    "No se encontró el reporte compilado (.jasper)", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Configurar parámetros
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("total", total);
            parametros.put("fecha", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            // 3. Crear data source
            JRDataSource dataSource = new JRBeanCollectionDataSource(datosFactura);

            // 4. Generar reporte
            JasperReport reporte = (JasperReport) JRLoader.loadObject(reportStream);
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, dataSource);

            // 5. Mostrar en visor
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle("Factura CloudFarm");
            viewer.setVisible(true);

        } catch (JRException e) {
            JOptionPane.showMessageDialog(null,
                "Error al generar factura: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reportStream != null) reportStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}