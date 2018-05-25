/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import com.koneksi.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hamz
 */
public class DialogAddItem extends javax.swing.JDialog {

    /**
     * Creates new form DialogAddItem
     */
    Connection con;
    Statement stm;
    ResultSet rs;
    String sql;
    DefaultTableModel tabmode;
    
    public DialogAddItem(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Koneksi obj = new Koneksi();
        con = obj.getKoneksi();
        stm = obj.getStm();
        tampilData();
        reset();
    }
    
    private boolean validasi(){
        if(txtJumlah.getText().trim().equals("")){
            txtJumlah.requestFocus();
            return false;
        }
        return true;
    }
    
    private void reset(){
        jButton1.setEnabled(false);
    }
    
    public void tampilData(){
        try {
            sql = "select * from tbl_barang2";
            Object[] baris = {"Kode Barang", "Nama", "Harga", "Stok"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                String kd = rs.getString("kd_barang");
                String nama = rs.getString("nama");
                String harga = rs.getString("harga");
                String masuk = rs.getString("stok");
                String[]row = {kd, nama, harga, masuk};
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    
    
    public void tampilFind(){
        try {
            sql = "select * from tbl_barang2 where kd_barang like '%"+txtFind.getText()+"%' or nama like '%"+txtFind.getText()+"%'";
            Object[] baris = {"Kode Barang", "Nama", "Harga", "Stok"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                String kd = rs.getString("kd_barang");
                String nama = rs.getString("nama");
                String harga = rs.getString("harga");
                String masuk = rs.getString("stok");
                String[]row = {kd, nama, harga, masuk};
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    
    private void addItem(){
        if(txtJumlah.getText().trim().equals("")){
            txtJumlah.requestFocus();
        }
        if (Integer.parseInt(txtJumlah.getText()) > Integer.parseInt(txtStok.getText())){
            JOptionPane.showMessageDialog(null, "Jumlah Lebih besar dari stok");
        }
        {
            try{
                sql = "select * from tbl_detail_transaksi where kd_pelanggan='"+getKodePelanggan()+"' and kd_barang='"+txtKdBarang.getText()+"'";
                rs = stm.executeQuery(sql);
                    int jumlah = Integer.parseInt(txtJumlah.getText());
                    int harga = Integer.parseInt(txtHarga.getText());
                    int hasil = jumlah * harga;
                if (rs.next()){
                    int jumlah_akhir = rs.getInt("jumlah") + jumlah;
                    int total_akhir = rs.getInt("sub_total") + hasil;
                    sql = "update tbl_detail_transaksi set jumlah='"+jumlah_akhir+"', sub_total='"+total_akhir+"' where kd_pelanggan='"+getKodePelanggan()+"' and kd_barang='"+txtKdBarang.getText()+"'";
                    stm.executeUpdate(sql);
                }else{
                    sql = "insert into tbl_detail_transaksi VALUES(NULL,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, getKodePelanggan());
                    ps.setString(2, txtKdBarang.getText());
                    ps.setString(3, txtJumlah.getText());
                    ps.setInt(4, hasil);
                    ps.executeUpdate();
                }
                DialogPenjualan penjualan = new DialogPenjualan(null, true);
                penjualan.tampilData();
                this.hide();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    private boolean getkode(){
        int i = 0;
            try {
                sql = "select * from tbl_transaksi";
                rs = stm.executeQuery(sql);
                while(rs.next())
                    i++;
                if(i > 0){
                    rs.close();
                    return true;
                }
                else{
                    rs.close();
                    return false;
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return false;
        }
    
    private String getKodePelanggan(){
        if(getkode()){
        try{
            sql = "SELECT max(kd_pelanggan) as kode FROM tbl_transaksi";
            rs = stm.executeQuery(sql);
            if(rs.next()){
                //JOptionPane.showMessageDialog(null, rs.getString("kode"));
                String id = rs.getString("kode").substring(1);
                String kode = ""+(Integer.parseInt(id)+1);
                if(kode.length()==1){
                   return "P00"+kode; 
                }
                else if(kode.length()==3){
                  return  "P0"+kode; 
                }else{
                    return "P"+kode; 
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal sql");
        }
        }else{
            return "P001";
        }
        return "apa";
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
        txtKdBarang = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtStok = new javax.swing.JTextField();
        txtJumlah = new javax.swing.JTextField();
        txtFind = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txtHarga = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txtKdBarang.setEnabled(false);

        txtNama.setEnabled(false);

        txtStok.setEnabled(false);

        txtFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindActionPerformed(evt);
            }
        });
        txtFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFindKeyPressed(evt);
            }
        });

        jLabel1.setText("Kode Barang");

        jLabel2.setText("Nama");

        jLabel3.setText("Stok");

        jLabel4.setText("Jumlah");

        jButton1.setText("Add Item");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtHarga.setEnabled(false);

        jLabel5.setText("Harga");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtKdBarang)
                                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtJumlah)
                                    .addComponent(txtStok)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKdBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtStok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        setSize(new java.awt.Dimension(686, 440));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFindActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFindActionPerformed

    private void txtFindKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindKeyPressed
        // TODO add your handling code here:
        tampilFind();
    }//GEN-LAST:event_txtFindKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row = jTable1.getSelectedRow();
        txtKdBarang.setText(jTable1.getValueAt(row, 0).toString());
        txtNama.setText(jTable1.getValueAt(row, 1).toString());
        txtHarga.setText(jTable1.getValueAt(row, 2).toString());
        txtStok.setText(jTable1.getValueAt(row, 3).toString());
        jButton1.setEnabled(true);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (txtJumlah.getText().trim().equals(""))
            JOptionPane.showMessageDialog(null, "Silakan Pilih barang dan masukan jumlah");
        else
            addItem();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(DialogAddItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogAddItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogAddItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogAddItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogAddItem dialog = new DialogAddItem(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKdBarang;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtStok;
    // End of variables declaration//GEN-END:variables
}
