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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hamz
 */
public class DialogBooking extends javax.swing.JDialog {

    /**
     * Creates new form DialogBooking
     */
    Connection con;
    Statement stm;
    ResultSet rs;
    String sql;
    DefaultTableModel tabmode;
    String[]kd_lapangan = new String[20];
    
    public DialogBooking(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Koneksi obj = new Koneksi();
        con = obj.getKoneksi();
        stm = obj.getStm();
        tampilData();
        reset();
    }
    
    private String getTanggal(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public void tampilData(){
        try {
            sql = "select * from tbl_booking INNER JOIN tbl_lapangan ON tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan  ORDER BY tgl DESC";
            Object[] baris = {"Kode Booking", "Nama Team", "Tanggal", "Jam Mulai", "Lama Sewa", "Lapangan"};
            tabmode = new DefaultTableModel(null, baris);
            rs = stm.executeQuery(sql);
            while (rs.next()){
                String kd = rs.getString("kd_booking");
                String nama = rs.getString("nama_team");
                String harga = rs.getString("tgl");
                String masuk = rs.getString("jam_masuk");
                String lama = rs.getString("lama_sewa");
                String lama1 = rs.getString("nama");
                String[]row = {kd, nama, harga, masuk, lama, lama1};
                tabmode.addRow(row);
            }
            tblLap.setModel(tabmode);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal meload data "+e.getMessage());
        }
    }
    
    private void ketikcari(){
        Object[] baris = {"Kode Booking", "Nama Team", "Tanggal", "Jam Mulai", "Lama Sewa", "Lapangan"};
        tabmode = new DefaultTableModel(null, baris);
        tblLap.setModel(tabmode);
        sql = "select * from tbl_booking INNER JOIN tbl_lapangan ON tbl_booking.kd_lapangan = tbl_lapangan.kd_lapangan where kd_booking like '%"+txtCari.getText()+"%' or tbl_booking.kd_lapangan like '%"+txtCari.getText()+"%' or nama_team like '%"+txtCari.getText()+"%' or tgl like '"+txtCari.getText()+"' or jam_masuk like '"+txtCari.getText()+"' or lama_sewa like '"+txtCari.getText()+"'  ORDER BY jam_masuk ASC";
        try{
            rs = stm.executeQuery(sql);
            while(rs.next()){
                String kd = rs.getString("kd_booking");
                String nama = rs.getString("nama_team");
                String harga = rs.getString("tgl");
                String masuk = rs.getString("jam_masuk");
                String lama = rs.getString("lama_sewa");
                String lapang = rs.getString("nama");
                String[]row = {kd, nama, harga, masuk, lama, lapang};
                tabmode.addRow(row);
            }
            tblLap.setModel(tabmode);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void reset(){
        txtKode.setText(null);
        txtNama.setText(null);
        txtJamMasuk.setText(null);
        txtTelepon.setText(null);
        jXDatePicker1.setDate(null);
        txtLamaSewa.setValue(0);
        
        btnSimpan.setEnabled(true);
        
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
        autokode();
        cmbLap();
    }
    
    private void simpan(){
          try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            sql = "insert into tbl_booking values(?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, kd_lapangan[cmbLap.getSelectedIndex()]);
            ps.setString(3, txtNama.getText());
            ps.setString(4, txtTelepon.getText());
            ps.setString(5, dateFormat.format(jXDatePicker1.getDate()));
            ps.setString(6, txtJamMasuk.getText());
            ps.setInt(7, txtLamaSewa.getValue());
            ps.executeUpdate();
            tampilData();
            reset();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Booking gagal "+e.getMessage());
        }
    }
    
//    INI BELUM DISELESAIKAN
    
    
    private void hapus(){
        try{
            sql = "delete from tbl_booking where kd_booking = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.executeUpdate();
            tampilData();
            reset();
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Hapus gagal "+e.getMessage());
        }
    }
    
    private void klikTable(){
        int row = tblLap.getSelectedRow();
        txtKode.setText(tblLap.getValueAt(row, 0).toString());
        txtNama.setText(tblLap.getValueAt(row, 1).toString());
        txtJamMasuk.setText(tblLap.getValueAt(row, 2).toString());
        //txtTanggal.setText(tblLap.getValueAt(row, 3).toString());
        txtLamaSewa.setValue(Integer.parseInt(tblLap.getValueAt(row, 4).toString()));
        btnSimpan.setEnabled(false);
    }
    
    private boolean getkode(){
        int i = 0;
            try {
                sql = "select * from tbl_booking";
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
    
    private void autokode(){
        if(getkode()){
        try{
            sql = "SELECT max(kd_booking) as kode FROM tbl_booking";
            rs = stm.executeQuery(sql);
            if(rs.next()){
                //JOptionPane.showMessageDialog(null, rs.getString("kode"));
                String id = rs.getString("kode").substring(1);
                String kode = ""+(Integer.parseInt(id)+1);
                if(kode.length()==1){
                    txtKode.setText("H00" + kode); 
                }else if(kode.length()==2){
                    txtKode.setText("H0" + kode); 
                }else{
                    txtKode.setText("H" + kode); 
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal sql");
        }
        }else{
            txtKode.setText("H001");
        }
    }
    
    private boolean validasiText(){
        if (txtNama.getText().trim().equals(""))
            txtNama.requestFocus();
        else if (txtJamMasuk.getText().trim().equals(""))
            txtJamMasuk.requestFocus();
        else {
            return true;
        }
        return false;
    }
    
    private void cmbLap(){
        try{
            cmbLap.removeAllItems();
            cmbLap.addItem("");
            sql = "select * from tbl_lapangan where kondisi='baik'";
            rs = stm.executeQuery(sql);
            int i=0;
            while (rs.next()){
                i++;
                cmbLap.addItem(rs.getString("nama"));
                kd_lapangan[i] = rs.getString("kd_lapangan");
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private void selectlap(){
        if(cmbLap.getSelectedIndex() < 1)
            txtharga.setText(null);
        else{
            try{
                sql = "select * from tbl_lapangan where kd_lapangan = '"+kd_lapangan[cmbLap.getSelectedIndex()]+"'";
                rs = stm.executeQuery(sql);
                if(rs.next()){
                    txtharga.setText(rs.getString("harga"));
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    private void cek(){
        try{
            Integer[] mulai = new Integer[24];
            Integer[] sewa = new Integer[24];
            Integer[] jam_sudah = new Integer[24];
            boolean succes = false;
            int j=0;
            int k=0;
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String alert = "Jam Yang sudah ada\n";
            sql = "select * from tbl_booking where tgl='"+dateFormat.format(jXDatePicker1.getDate())+"' and kd_lapangan='"+kd_lapangan[cmbLap.getSelectedIndex()]+"'";
            rs = stm.executeQuery(sql);
            while (rs.next()){
                mulai[j] = rs.getInt("jam_masuk");
                sewa[j] = rs.getInt("lama_sewa");
                
                    for(int u=0;u<=rs.getInt("lama_sewa")-1;u++){
                        jam_sudah[k] = mulai[j] + u;
                        k++;
                    }
                j++;
            }
              if(cekadajam(jam_sudah, k)){
                  for (int p=0;p<k;p++){
                       alert = alert + ","+ jam_sudah[p];
                    }
                    JOptionPane.showMessageDialog(null, alert);
              }else{
                  Integer txt3 =  Integer.parseInt(txtJamMasuk.getText().toString());
                  Integer txt5 =  txtLamaSewa.getValue();
                  Integer[]cekjambo = new Integer[24];
                  for (int y=0;y<=txt5;y++){
                      cekjambo[y] = txt3 + y;
                  }
                  Integer hasil = cekadajambo(jam_sudah, k, cekjambo);
                    if(hasil == 0){
                        //JOptionPane.showMessageDialog(null, "Hore gaada ");
                        diizinkan();
                    }else{
//                        misal jam 7 selesai jam 8
                        if(hasil == (txt3 + txt5)){
                            //JOptionPane.showMessageDialog(null, "Bisa  "+hasil);
                            diizinkan();
                        }else{
                            JOptionPane.showMessageDialog(null, "Jam "+hasil+" adaan\nJadi hanya bisa main "+(hasil - txt3)+" jam");
                        }
                    }
              }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Gagal cek : "+e.getMessage());
        }
    }
    
    private boolean cekadajam(Integer[]jam_sudah, Integer k){
        for (int p=0;p<k;p++){
                Integer txt3 =  Integer.parseInt(txtJamMasuk.getText().toString());
                if(txt3 == jam_sudah[p]){
                   // alert = alert + ","+ jam_sudah[p];
                    //succes = false;
                    return true;
                }
            }
        return false;
    }
    private Integer cekadajambo(Integer[]jam_sudah, Integer k, Integer[]jambo){
        for (int p=0;p<k;p++){
                Integer txt3 =  Integer.parseInt(txtJamMasuk.getText().toString());
                for (int i=0;i<jambo.length;i++){
                        if(jambo[i] == jam_sudah[p]){
                        return jambo[i];
                    }
                }
                
            }
        return 0;
    }
    
    private void diizinkan(){
        txtNama.setEnabled(true);
        txtTelepon.setEnabled(true);
        btnSimpan.setEnabled(true);
    }
    
    private boolean validasicek(){
        if(cmbLap.getSelectedIndex() < 1){
            cmbLap.requestFocus();
            return false;
        }
        //else if(jXDatePicker1.getDate().equals(null) || jXDatePicker1.getDate().equals("")){
        //    jXDatePicker1.requestFocus();
        //    return false;
        //}
        else if(txtJamMasuk.getText().trim().equals("")){
            txtJamMasuk.requestFocus();
            return false;
        }
        else if(txtLamaSewa.getValue() == 0){
            txtLamaSewa.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLap = new javax.swing.JTable();
        txtKode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        txtJamMasuk = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnSimpan = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtTelepon = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txtLamaSewa = new com.toedter.components.JSpinField();
        jLabel7 = new javax.swing.JLabel();
        cmbLap = new javax.swing.JComboBox<>();
        txtharga = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCariKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCariKeyTyped(evt);
            }
        });

        tblLap.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLapMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLap);

        txtKode.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("Kode");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText("Nama");

        txtNama.setEnabled(false);

        txtJamMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJamMasukActionPerformed(evt);
            }
        });
        txtJamMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtJamMasukKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setText("jam_masuk");

        btnSimpan.setText("Simpan");
        btnSimpan.setEnabled(false);
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel4.setText("Tgl");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel5.setText("Lama_sewa");

        txtTelepon.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel6.setText("Telepon");

        jButton1.setText("Cek");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtLamaSewa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtLamaSewaMouseClicked(evt);
            }
        });
        txtLamaSewa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLamaSewaKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel7.setText("Lap");

        cmbLap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbLap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbLapActionPerformed(evt);
            }
        });

        txtharga.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel8.setText("Harga");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(59, 59, 59)
                                        .addComponent(jLabel5))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel3)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtJamMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtLamaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmbLap, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jXDatePicker1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(btnSimpan)
                                .addGap(31, 31, 31)
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(137, 137, 137))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 813, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelepon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(cmbLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtJamMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtharga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtLamaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jButton1)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSimpan)
                    .addComponent(btnReset))
                .addGap(3, 3, 3)
                .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(850, 493));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyPressed
        // TODO add your handling code here:
        ketikcari();
    }//GEN-LAST:event_txtCariKeyPressed

    private void txtCariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCariKeyTyped

    private void tblLapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLapMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblLapMouseClicked

    private void txtJamMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJamMasukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJamMasukActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if(validasiText())
        simpan();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
        if (validasicek())
            cek();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cmbLapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbLapActionPerformed
        // TODO add your handling code here:
        selectlap();
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_cmbLapActionPerformed

    private void txtJamMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJamMasukKeyPressed
        // TODO add your handling code here:
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_txtJamMasukKeyPressed

    private void txtLamaSewaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLamaSewaKeyPressed
        // TODO add your handling code here:
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_txtLamaSewaKeyPressed

    private void txtLamaSewaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLamaSewaMouseClicked
        // TODO add your handling code here:
        txtNama.setEnabled(false);
        txtTelepon.setEnabled(false);
        btnSimpan.setEnabled(false);
    }//GEN-LAST:event_txtLamaSewaMouseClicked

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
            java.util.logging.Logger.getLogger(DialogBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogBooking dialog = new DialogBooking(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JComboBox<String> cmbLap;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JTable tblLap;
    private javax.swing.JTextField txtCari;
    private javax.swing.JTextField txtJamMasuk;
    private javax.swing.JTextField txtKode;
    private com.toedter.components.JSpinField txtLamaSewa;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtTelepon;
    private javax.swing.JTextField txtharga;
    // End of variables declaration//GEN-END:variables
}
