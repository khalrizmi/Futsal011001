/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import com.cetak.Cetak;
import com.koneksi.Koneksi;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
public class DialogDataBooking extends javax.swing.JDialog {

    /**
     * Creates new form DialogDataBooking
     */
    Connection con;
    Statement stm;
    ResultSet rs;
    String sql;
    DefaultTableModel tabmode;
    
    public DialogDataBooking(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Koneksi obj = new Koneksi();
        con = obj.getKoneksi();
        stm = obj.getStm();
        tampil_all();
    }
    
    private void tampil_all(){
        try {
            sql = "select * from tbl_booking inner join tbl_lapangan on tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan ORDER BY kd_booking DESC";
            Object[] baris = {"Kode Booking", "Lapangan", "Nama Team", "Telepon", "Tanggal", "Jam Masuk", "Lama Sewa"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                Object[] row ={
                    rs.getString("kd_booking"),
                    rs.getString("nama"),
                    rs.getString("nama_team"),
                    rs.getString("telepon"),
                    rs.getString("tgl"),
                    rs.getString("jam_masuk"),
                    rs.getString("lama_sewa")
                };
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    
    private void tampil_tgl(){
        try {
            sql = "select * from tbl_booking inner join tbl_lapangan on tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan where tgl = '"+txtTanggal.getText()+"'";
            Object[] baris = {"Kode Booking", "Lapangan", "Nama Team", "Telepon", "Tanggal", "Jam Masuk", "Lama Sewa"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                Object[] row ={
                    rs.getString("kd_booking"),
                    rs.getString("nama"),
                    rs.getString("nama_team"),
                    rs.getString("telepon"),
                    rs.getString("tgl"),
                    rs.getString("jam_masuk"),
                    rs.getString("lama_sewa")
                };
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    
    private void tampil_ketik(){
        if(txtTanggal.getText().trim().equals(""))
            sql = "select * from tbl_booking inner join tbl_lapangan on tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan where nama_team like '%"+txtFind.getText()+"%'";
        else
            sql = "select * from tbl_booking inner join tbl_lapangan on tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan where nama_team like '%"+txtFind.getText()+"%' and tgl = '"+txtTanggal.getText()+"'";
        try {
            Object[] baris = {"Kode Booking", "Lapangan", "Nama Team", "Telepon", "Tanggal", "Jam Masuk", "Lama Sewa"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                Object[] row ={
                    rs.getString("kd_booking"),
                    rs.getString("nama"),
                    rs.getString("nama_team"),
                    rs.getString("telepon"),
                    rs.getString("tgl"),
                    rs.getString("jam_masuk"),
                    rs.getString("lama_sewa")
                };
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtTanggal = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Tanggal");

        jLabel2.setText("Cari Team");

        txtFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFindKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFindKeyTyped(evt);
            }
        });

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Print All");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1)))
                        .addGap(298, 298, 298)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(779, 397));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tampil_tgl();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtFindKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFindKeyPressed

    private void txtFindKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindKeyTyped
        // TODO add your handling code here:
        tampil_ketik();
    }//GEN-LAST:event_txtFindKeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String reportSource = "D:\\School\\XI\\PRODUKTIF\\PBO\\TokoBukuProject\\src\\com\\laporan\\ReportBooking2.jrxml";
        String reportDest   = "D:\\School\\XI\\PRODUKTIF\\PBO\\TokoBukuProject\\src\\com\\laporan\\ReportBooking2.jasper";
        
        try {
            new Cetak(reportSource, reportDest);
        } catch (JRException ex) {
            Logger.getLogger(DialogDataBooking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogDataBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogDataBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogDataBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogDataBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogDataBooking dialog = new DialogDataBooking(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextField txtTanggal;
    // End of variables declaration//GEN-END:variables
}
