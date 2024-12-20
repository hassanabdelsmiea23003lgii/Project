package shop_pos;


public class ShopData {
    private final int id;
    private final String name;
    private final String address1;
    private final String address2;
    private final String telephone;
    private final String email;
    private final String cashier;
    private final String msg1;
    private final String msg2;
    private final String tax;
    private final String discount;
    

    // Constructor
    public ShopData(int id, String name, String address1, String address2, String telephone,
                    String email, String cashier, String msg1, String msg2, String tax, String discount) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
        this.address2 = address2;
        this.telephone = telephone;
        this.email = email;
        this.cashier = cashier;
        this.msg1 = msg1;
        this.msg2 = msg2;
        this.tax = tax;
        this.discount = discount;
        
    }

    ShopData(int aInt, String string, String string0, String string1, String string2, String string3, String string4, String string5, String string6, String string7) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getCashier() {
        return cashier;
    }

    public String getMsg1() {
        return msg1;
    }

    public String getMsg2() {
        return msg2;
    }

    public String getTax() {
        return tax;
    }

    public String getDiscount() {
        return discount;
    }
    

   
}
