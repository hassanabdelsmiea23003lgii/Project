/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author Hassan
 */

    
public class SalesTransactionManager {
    private static SalesTransactionManager instance;
  

    private SalesTransactionManager() {
        ArrayList<Object> sales = new ArrayList<>();
    }

    public static synchronized SalesTransactionManager getInstance() {
        if (instance == null) {
            instance = new SalesTransactionManager();
        }
        return instance;
    }

    public void processSale(Sale sale) {
        
        System.out.println("Sale processed: " + sale);
    }
}
