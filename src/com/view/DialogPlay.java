/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import com.koneksi.Koneksi;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hamz
 */
public class DialogPlay extends javax.swing.JDialog {

    /**
     * Creates new form DialogPlay
     */
    Connection con;
    Statement stm;
    ResultSet rs;
    String sql;
    String kode_lapangan;
    Integer harga_lapangan = 0;
    DefaultTableModel tabmode;
    private JPanel[] panel;
    Integer harga_lapang_awal = 0;
    Integer aksesoris = 0;
    
    public DialogPlay(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        Koneksi obj = new Koneksi();
        con = obj.getKoneksi();
        stm = obj.getStm();
        //tampil();
        tab3();
        reset();
    }
    
    private void tab3(){
        Integer i=0, p=0, page=0;
        Button[] btn;
        String[] nama;
        String[] getnamabtn = new String[100];
        String[] getkondisi = new String[100];
        String[] getkode = new String[100];
        
        jTabbedPane1.removeAll();
        
        try{
            sql = "select * from tbl_lapangan";
            rs = stm.executeQuery(sql);
            while(rs.next()){
                getnamabtn[i] = rs.getString("nama");
                getkondisi[i] = rs.getString("kondisi");
                getkode[i] = rs.getString("kd_lapangan");
                if(i%4==0)
                    page += 1;
                i++;
            }
            
            String[] status = new String[i];
        String[] statuskode = new String[i];
            
            sql = "select * from tbl_play inner join tbl_lapangan on tbl_play.kd_lapangan = tbl_lapangan.kd_lapangan where tbl_play.tgl = '"+getTanggal()+"' and '"+getJam()+"' BETWEEN tbl_play.jam AND (tbl_play.lama_sewa + tbl_play.jam)";
            rs = stm.executeQuery(sql);
            while (rs.next()){
                status[p] = rs.getString("nama");
                statuskode[p] = rs.getString("kd_lapangan");
                p++;
            }
            
            JPanel[] panel = new JPanel[page];
            Button[] button = new Button[i];
            int next=0, stop=0;
            for (int x=0;x<panel.length;x++){
                panel[x] = new JPanel();
                panel[x].setLayout(new GridLayout(2,2));
                    for (int y=0;y<i; y++){
                        if(y<4){
                            if(getnamabtn[next] != null){
                                button[next] = new Button();
                                button[next].setLabel(getnamabtn[next]);
                                button[next].setName(getkode[next]);
                                button[next].addActionListener(new klikbtn(button[next]));
                                if(getkondisi[next].equals("baik"))
                                    button[next].setBackground(Color.green);
                                else
                                    button[next].setBackground(Color.red);
                                
                                    for(int u=0; u<statuskode.length; u++){
                                        if(button[next].getName().equals(statuskode[u])){
                                            button[next].setBackground(Color.yellow);
                                        }
                                    }
                                panel[x].add(button[next]);   
                                next++;
                            }
                        }
                    }
                jTabbedPane1.add(panel[x], "Page "+(x+1));
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public class klikbtn implements ActionListener{

        Button btn;
        
        public klikbtn(Button btn){
            this.btn = btn;
        }
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            try{
                sql = "select * from tbl_play inner join tbl_lapangan on tbl_play.kd_lapangan = tbl_lapangan.kd_lapangan where tbl_play.kd_lapangan = '"+btn.getName()+"' and tbl_play.tgl = '"+getTanggal()+"' and '"+getJam()+"' BETWEEN tbl_play.jam AND (tbl_play.lama_sewa + tbl_play.jam)";
                rs = stm.executeQuery(sql);
                if (rs.next()){
                    txtKode.setText(rs.getString("kd_main"));
                    jTextField1.setText(rs.getString("nama"));
                    jTextField2.setText(rs.getString("jam"));
                    jTextField3.setText(rs.getString("lama_sewa"));
                    jTextField4.setText(rs.getString("total"));
                    jTextField1.setEnabled(false);
                    jTextField3.setEnabled(false);
                    jTextField4.setEnabled(false);
                    jCheckBox1.setEnabled(false);
                    jCheckBox2.setEnabled(false);
                    btnPlay.setEnabled(false);
                    harga_lapangan = rs.getInt("total");
                    harga_lapang_awal = 0;
                }else{
                    reset();
                    sql = "select * from tbl_lapangan where kd_lapangan = '"+btn.getName()+"'";
                    rs = stm.executeQuery(sql);
                    rs.next();
                    jTextField4.setText(rs.getString("harga"));
                    harga_lapang_awal = rs.getInt("harga");
                    harga_lapangan = 0;
                }
                kode_lapangan = btn.getName();
                tampil_table(btn.getName());
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    
    }
    
    private void reset(){
        jTextField1.setText(null);
        jTextField2.setText(String.valueOf(getJam()));
        jTextField3.setText(null);
        jTextField4.setText(null);
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        
        jTextField1.setEnabled(true);
        jTextField3.setEnabled(true);
        jCheckBox1.setEnabled(true);
        jCheckBox2.setEnabled(true);
        btnPlay.setEnabled(false);
         autokode();
    }
    
    private void tampil_table(String kode){
        try{
            sql = "select * from tbl_booking where tgl = '"+getTanggal()+"' and kd_lapangan = '"+kode+"' ORDER BY jam_masuk ASC";
            rs = stm.executeQuery(sql);
            Object[] baris = {"Nama", "Jam", "Sewa", "Lapangan"};
            tabmode = new DefaultTableModel(null, baris);
            while (rs.next()){
                String kd = rs.getString("nama_team");
                String nama = rs.getString("jam_masuk");
                String telepon = rs.getString("lama_sewa");
                String alamat = rs.getString("kd_lapangan");
                String[]row = {kd, nama, telepon, alamat};
                tabmode.addRow(row);
            }
            jTable1.setModel(tabmode);
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    private void play(){
        try{
            int akseso = Integer.parseInt(jTextField4.getText()) - harga_lapangan;
            sql = "insert into tbl_play values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txtKode.getText());
            ps.setString(2, kode_lapangan);
            ps.setString(3, jTextField1.getText());
            ps.setString(4, getTanggal());
            ps.setString(5, String.valueOf(getJam()));
            ps.setString(6, String.valueOf(aksesoris));
            ps.setString(7, jTextField3.getText());
            ps.setString(8, jTextField4.getText());
            ps.executeUpdate();
            tab3();
            reset();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    private boolean getkode(){
        int i = 0;
            try {
                sql = "select * from tbl_play";
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
            sql = "SELECT max(kd_main) as kode FROM tbl_play";
            rs = stm.executeQuery(sql);
            if(rs.next()){
                //JOptionPane.showMessageDialog(null, rs.getString("kode"));
                String id = rs.getString("kode").substring(1);
                String kode = ""+(Integer.parseInt(id)+1);
                if(kode.length()==1){
                    txtKode.setText("M00" + kode); 
                }
                else if(kode.length()==2){
                    txtKode.setText("M0" + kode); 
                }else{
                    txtKode.setText("M" + kode); 
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Gagal sql");
        }
        }else{
            txtKode.setText("M001");
        }
    }
    
    private void hitung_total(){
        int lama = Integer.parseInt(jTextField3.getText());
        
        int hasil = harga_lapang_awal * lama;
        jTextField4.setText(String.valueOf(hasil+aksesoris));
    }
    
    private String getTanggal(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    private Integer getJam(){
        DateFormat dateFormat = new SimpleDateFormat("H");
        Date date = new Date();
        return Integer.parseInt(dateFormat.format(date));
    }
    
    private boolean validasiText(){
        if (jTextField1.getText().trim().equals(""))
            jTextField2.requestFocus();
        else if (jTextField3.getText().trim().equals(""))
            jTextField3.requestFocus();
        else {
            return true;
        }
        return false;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jTextField4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnPlay = new javax.swing.JButton();
        txtKode = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnPlay1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 43, 5));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Main");

        jPanel4.setBackground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Kosong");

        jPanel5.setBackground(new java.awt.Color(0, 255, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 49, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 32, Short.MAX_VALUE)
        );

        jTextField2.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Nama");

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

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setText("Refresh");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Perbaikan");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Booking");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Mulai");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Sewa");

        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCheckBox1.setText("Rompi");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jCheckBox2.setText("Bola");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jTextField4.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Total");

        btnPlay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/right_arrow_30.png"))); // NOI18N
        btnPlay.setText("Play");
        btnPlay.setEnabled(false);
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        txtKode.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Kode");

        btnPlay1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPlay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/image/right_arrow_30.png"))); // NOI18N
        btnPlay1.setText("Hitung");
        btnPlay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlay1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addGap(33, 33, 33)
                        .addComponent(jButton1))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jCheckBox1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBox2))
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnPlay1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(7, 7, 7)
                                    .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPlay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPlay1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel5))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(972, 516));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        // TODO add your handling code here:
        if(validasiText())
            play();
    }//GEN-LAST:event_btnPlayActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        if(jCheckBox1.isSelected()){
            aksesoris = aksesoris + 10000;
        }else{
            aksesoris = aksesoris - 10000;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
        if(jCheckBox2.isSelected()){
            aksesoris = aksesoris + 5000;
        }else{
            aksesoris = aksesoris - 5000;
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        tab3();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int row = jTable1.getSelectedRow();
        jTextField1.setText(jTable1.getValueAt(row, 0).toString());
        jTextField3.setText(jTable1.getValueAt(row, 2).toString());
        jTextField1.setEnabled(false);
        jTextField2.setEnabled(false);
        jTextField3.setEnabled(false);
        hitung_total();
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnPlay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlay1ActionPerformed
        // TODO add your handling code here:
        if(jTextField3.getText().trim().equals("") || jTextField1.getText().trim().equals(""))
            JOptionPane.showMessageDialog(null, "Isi data");
        else{
            hitung_total();
            btnPlay.setEnabled(true);
        }
    }//GEN-LAST:event_btnPlay1ActionPerformed

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
            java.util.logging.Logger.getLogger(DialogPlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPlay.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogPlay dialog = new DialogPlay(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnPlay;
    private javax.swing.JButton btnPlay1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField txtKode;
    // End of variables declaration//GEN-END:variables
}
