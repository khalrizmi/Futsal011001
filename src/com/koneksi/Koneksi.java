/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koneksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Hamz
 */
public class Koneksi {
    
    private Connection con;

    private Statement stm;
    
    public Connection getKoneksi(){
        if(con == null){
            try{
                DriverManager.registerDriver(new com.mysql.jdbc.Driver());
                con = DriverManager.getConnection("jdbc:mysql://localhost/db_pbo", "root", "");
                stm = con.createStatement();
            }catch(SQLException e){
                System.out.println("Koneksi gagal : "+e.getMessage());
            }
        }
        return con;
    }
    
    public Statement getStm(){
        return stm;
    }
    
}
