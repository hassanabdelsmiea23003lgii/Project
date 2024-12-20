/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Go
 */
public class db {
   
 public static Connection mycon(){
 
 
     Connection connection = null;
        try {
            // Register JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Open a connection
            connection = DriverManager.getConnection("jdbc:sqlite:db.db");
            //System.out.println("sucsess");
            
        } catch (ClassNotFoundException | SQLException e) {
            
            System.out.println(e);
            
        }
        return connection;

     
 
 
 }   
    
    
    
}
