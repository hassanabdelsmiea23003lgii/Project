/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

import java.io.PrintStream;

/**
 *
 * @author Hassan
 */

    
public class InventoryProxy implements Inventory {
    private final InventoryManager manager;

    public InventoryProxy() {
        this.manager = InventoryManager.getInstance();
    }

    public void checkStock(String product) {
        if (manager != null) {
            PrintStream println = System.out.println("Stock for " + product + ": " + manager.getStock(product));
        } else {
            System.out.println("No stock available for " + product);
        }
    }
}
