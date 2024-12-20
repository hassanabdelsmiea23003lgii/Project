/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shop_pos;

/**
 *
 * @author Hassan
 */

   public class ProductFactory {
    public static Electronics createProduct(String category) {
        switch (category.toLowerCase()) {
            case "electronics": 
                return new Electronics();
            case "apparel": 
                return new Apparel();
            case "groceries": 
                return new Groceries();
            default: 
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }

    private static class Apparel extends Electronics {

        public Apparel() {
        }
    }
}
