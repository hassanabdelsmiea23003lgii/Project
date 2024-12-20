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
public class ProductBuilder {
    private String name;
    private double price;

    public ProductBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new CustomProduct(name, price);
    }
}

public class CustomProduct extends Product {
    private double price;

    public CustomProduct(String name, double price) {
        
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
