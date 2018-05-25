/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cetak;

import com.koneksi.Koneksi;
import java.sql.Connection;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Hamz
 */
public class Cetak {
    
    
    public Cetak(String source, String Dest) throws JRException{
        Koneksi obj = new Koneksi();
        Connection con = obj.getKoneksi();
        
        String reportSource = null;
        String reportDest   = null;
        
        try{
            reportSource = source;
            reportDest   = Dest;
            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, con);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, reportDest);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
