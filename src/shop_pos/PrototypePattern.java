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
public abstract class PrototypePattern implements Cloneable {
    private String name;

    public PrototypePattern(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Product clone();
}

public class Electronics extends Product {
    public Electronics() {
        super("Electronics");
    }

    public Product clone() {
        return new Electronics();
    }
}
