/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hassan
 */
public class InventoryManager {
    private static InventoryManager instance;
    private Map<String, Integer> stock;

    private InventoryManager() {
        stock = new HashMap<>();
    }

    public static synchronized InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }

    public void updateStock(String product, int quantity) {
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
        System.out.println("Stock updated: " + product + " - " + stock.get(product));
    }
}
