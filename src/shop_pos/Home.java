/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package shop_pos;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.print.PrintService;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public final class Home extends javax.swing.JFrame {
public String Receipt_No  ;
    /**
     * Creates new form Home
     */
    public Home() {
        
        initComponents();
        setTabNamesFromSQLData();
        loadAllProducts();        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTableColumnSize();
        loadTaxDiscount();
        loadReceiptNo();
        loadMoreProducts();
        
        }
    
//Start

public void setTableColumnSize() {
    // Assuming your JTable is named 'Product_list'
    Product_list.getColumnModel().getColumn(0).setPreferredWidth(1);
    Product_list.getColumnModel().getColumn(1).setPreferredWidth(190);
    Product_list.getColumnModel().getColumn(2).setPreferredWidth(1);
    Product_list.getColumnModel().getColumn(3).setPreferredWidth(40);
  
    // Assuming your JTable is named 'Product_list'
    moreProductTable.getColumnModel().getColumn(0).setPreferredWidth(1);
    moreProductTable.getColumnModel().getColumn(1).setPreferredWidth(190);
    moreProductTable.getColumnModel().getColumn(2).setPreferredWidth(1);
    moreProductTable.getColumnModel().getColumn(3).setPreferredWidth(40);
  
}


public void addTable(int id, String name, int qty, Double price) {
    DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();

    try {
        int rowCount = dt.getRowCount();
        boolean idExists = false;

        for (int i = 0; i < rowCount; i++) {
            Object idValue = dt.getValueAt(i, 0); // Assuming ID is at column 0
            if (idValue != null && idValue.toString().equals(String.valueOf(id))) {
                idExists = true;

                Object qtyValue = dt.getValueAt(i, 2); // Assuming Qty is at column 2
                if (qtyValue != null) {
                    int existingQty = Integer.parseInt(qtyValue.toString());
                    dt.setValueAt(existingQty + qty, i, 2); // Update Qty

                    // Update the price based on the quantity change
                    Object priceValue = dt.getValueAt(i, 3); // Assuming Price is at column 3
                    if (priceValue != null) {
                        double existingPrice = Double.parseDouble(priceValue.toString());
                        double updatedPrice = existingPrice * ((double)(existingQty + qty) / existingQty);
                        //price format
                        DecimalFormat df = new DecimalFormat("0.00");         
                        String formatPrice = df.format(updatedPrice);
                        double parsedPrice = Double.parseDouble(formatPrice);
                        dt.setValueAt(parsedPrice, i, 3); // Update Price
                    }
                }
                break;
            }
        }

        if (!idExists) {
            ArrayList<Object> rowData = new ArrayList<>();
            rowData.add(id);
            rowData.add(name);
            rowData.add(qty);
            rowData.add(price);

            dt.addRow(rowData.toArray());
        }
    } catch (NumberFormatException e) {
        System.out.println(e); // Print the exception for debugging
    }
}


public void loadMoreProducts() {

    DefaultTableModel dt = (DefaultTableModel) moreProductTable.getModel();
        dt.setRowCount(0);

        try (Statement s = db.mycon().createStatement()) {
            String query = "SELECT * FROM Products";
            try (ResultSet rs = s.executeQuery(query)) {
                while (rs.next()) {
                    Object[] rowData = {
                        rs.getString("id"),
                        rs.getString("Name"),
                        rs.getString("Price"),
                        rs.getString("Barcode")
                    };
                    dt.addRow(rowData);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

}
public void setTabNamesFromSQLData() {
    
    try {
        String sql = "SELECT Name FROM Category"; // Query to retrieve tab names from your database
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<String> tabNames = new ArrayList<>();

        while (resultSet.next()) {
            String tabName = resultSet.getString("name");
            tabNames.add(tabName);
        }

        for (int i = 0; i < tabNames.size(); i++) {
            jTabbedPane1.setTitleAt(i, tabNames.get(i));
        }
    } catch (SQLException e) {
        System.out.println(e); // Handle the exception appropriately
    }
    
    
 }
 // test 

public void loadAllProducts() {
    
    loadProductsByCategory(1, jP1);
    loadProductsByCategory(2, jP2);
    loadProductsByCategory(3, jP3);
    loadProductsByCategory(4, jP4);
    loadProductsByCategory(5, jP5);
    loadProductsByCategory(6, jP6);
    loadProductsByCategory(7, jP7);
    loadProductsByCategory(8, jP8);
    loadProductsByCategory(9, jP9);
    loadProductsByCategory(10, jP10);
    loadProductsByCategory(11, jP11);
    loadProductsByCategory(12, jP12);
    loadProductsByCategory(13, jP13);
    loadProductsByCategory(14, jP14);
    //loadProductsByCategory(15, jPanel20);
    

}

public void loadReceiptNo() {
    
     try (Statement statement = db.mycon().createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM shop_data WHERE id = 1")) {

        if (resultSet.next()) {
            Receipt_No = resultSet.getString("Receipt_No");
            //System.out.println(Receipt_No);
           
            
        } 
    } catch (SQLException e) {        
         System.out.println(e);
        
    }

}
public void loadTaxDiscount() {
    
    
    try (Statement statement = db.mycon().createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT * FROM shop_data")) {

        if (resultSet.next()) {
            t_tax.setText(resultSet.getString("Tax"));
            t_Dis.setText(resultSet.getString("Discount"));
        } else {
            
            t_tax.setText("");
            t_Dis.setText("");            
        }

    } catch (SQLException e) {
        // Log or display error message
        System.out.println("Error loading Tax and Discount: " + e.getMessage());
    }
}


public void loadProductsByCategory(int categoryId, JPanel panel) {
    ArrayList<Product> products = new ArrayList<>();
    int maxButtonWidth = 150; // Define your maximum width
    try {
        String sql = "SELECT id, name, price FROM Products WHERE Category = " + categoryId;
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");            
            //price format
            DecimalFormat df = new DecimalFormat("0.00");         
            String formatPrice = df.format(price);
            double parsedPrice = Double.parseDouble(formatPrice);
            Product product = new Product(id, name, parsedPrice);
            products.add(product);
        }
    } catch (SQLException e) {
        System.out.println(e);
    }
    
    for (Product product : products) {
        JButton button = new JButton("<html><center>" + product.getName() + "<br/>$" + product.getPrice() + "</center></html>");
        button.putClientProperty("productID", product.getId());
        button.setFont(new Font("Dialog", Font.BOLD, 16));
        
        // Calculate the button's preferred width
        int nameLength = product.getName().length() * 40; // Adjust the multiplier
        int calculatedWidth = Math.min(maxButtonWidth, nameLength);
        
        button.setPreferredSize(new Dimension(calculatedWidth, 90)); // Setting the preferred width
        
        button.addActionListener(e -> {
            JButton source = (JButton) e.getSource();
            int productId = (int) source.getClientProperty("productID");
            String buttonText = source.getText();
            String[] splitText = buttonText.split("<br/>");
            if (splitText.length == 2) {
                String name = splitText[0].replaceAll("<html><center>", "");
                String priceStr = splitText[1].replaceAll("</center></html>", "").replace("$", "");
                double price = Double.parseDouble(priceStr);
                
                // Add the selected product to a table or perform other actions
                addTable(productId, name, 1, price);
                Product_Price_calculation();
                loadReceiptNo();
            }
        });
        
        panel.add(button);
    }
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Adjust layout as needed
    panel.revalidate();
    panel.repaint();
}


  
// maste code    

public void master_loadProductsByCategory(int categoryId, JPanel panel) throws SQLException {
    ArrayList<Product> products = new ArrayList<>();

    try {
        String sql = "SELECT id, name, price FROM Products WHERE Category = " + categoryId;
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");            
            //price format
            DecimalFormat df = new DecimalFormat("0.00");         
            String formatPrice = df.format(price);
            double parsedPrice = Double.parseDouble(formatPrice);
            Product product = new Product(id, name, parsedPrice);
            products.add(product);
        }
    } catch (SQLException e) {
        System.out.println(e);
    }

    panel.setLayout(new GridLayout(6, 0)); // You can pass the panel as an argument

    for (Product product : products) {
        JButton button = new JButton(product.getName() + " - $" + product.getPrice());

        button.putClientProperty("productID", product.getId());
        button.setPreferredSize(new Dimension(100, 100));
        button.setFont(new Font("Dialog", Font.BOLD, 18));

        button.addActionListener((ActionEvent e) -> {
            JButton source = (JButton) e.getSource();
            int productId = (int) source.getClientProperty("productID");
            String buttonText = source.getText();
            String[] splitText = buttonText.split(" - ");
            if (splitText.length == 2) {
                String name = splitText[0];
                String priceStr = splitText[1].replace("$", "");
                double price = Double.parseDouble(priceStr);
                
                //price format
                DecimalFormat df = new DecimalFormat("0.00");         
                String formatPrice = df.format(price);
                double parsedPrice = Double.parseDouble(formatPrice);
                
                addTable(productId, name, 1, parsedPrice);
                Product_Price_calculation();
            }
        });

        panel.add(button);
    }

    panel.revalidate();
    panel.repaint();
    
       

}


//test end



public void Category_1_Product_loading() {
    
    ArrayList<Product> products = new ArrayList<>(); // Assuming Product is a class that holds ID, name, and price

    try {
        // Fetch 'id', 'name' and 'price' columns from the database
        String sql = "SELECT id, name, price FROM Products WHERE Category = 1 ";
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");

            Product product = new Product(id, name, price); // Create a Product object
            products.add(product);
        }
    } catch (SQLException e) {
        System.out.println(e);
    }

    jP1.setLayout(new GridLayout(6, 0)); // Setting the layout to a grid with 3 columns

    for (Product product : products) {
        JButton button = new JButton(product.getName() + " - $" + product.getPrice());

        button.putClientProperty("productID", product.getId()); // Set the product ID as client property

        button.setPreferredSize(new Dimension(150, 150)); // Set button size
        button.setFont(new Font("Dialog", Font.BOLD, 18)); // Set button font

        button.addActionListener((ActionEvent e) -> {
            JButton source = (JButton) e.getSource();
            int productId = (int) source.getClientProperty("productID"); // Retrieve the product ID
            String buttonText = source.getText();
            String[] splitText = buttonText.split(" - ");
            if (splitText.length == 2) {
                String name1 = splitText[0];
                String price = splitText[1].replace("$", ""); // Extracting the price without the dollar sign
                System.out.println("Name: " + name1);
                System.out.println("Price: " + price);
                System.out.println("Product ID: " + productId);
                addTable(productId, name1, 1, Double.valueOf(price));
                Product_Price_calculation();
            }
        });

        jP1.add(button);
    }

    
}
public void Category_12_Product_loading() {
    
    ArrayList<Product> products = new ArrayList<>(); // Assuming Product is a class that holds ID, name, and price

    try {
        // Fetch 'id', 'name' and 'price' columns from the database
        String sql = "SELECT id, name, price FROM Products WHERE Category = 12 ";
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");

            Product product = new Product(id, name, price); // Create a Product object
            products.add(product);
        }
    } catch (SQLException e) {
        System.out.println(e);
    }

    jP12.setLayout(new GridLayout(6, 0)); // Setting the layout to a grid with 3 columns

    for (Product product : products) {
        JButton button = new JButton(product.getName() + " - $" + product.getPrice());

        button.putClientProperty("productID", product.getId()); // Set the product ID as client property

        button.setPreferredSize(new Dimension(150, 150)); // Set button size
        button.setFont(new Font("Dialog", Font.BOLD, 18)); // Set button font

        button.addActionListener((ActionEvent e) -> {
            JButton source = (JButton) e.getSource();
            int productId = (int) source.getClientProperty("productID"); // Retrieve the product ID
            String buttonText = source.getText();
            String[] splitText = buttonText.split(" - ");
            if (splitText.length == 2) {
                String name1 = splitText[0];
                String price = splitText[1].replace("$", ""); // Extracting the price without the dollar sign
                System.out.println("Name: " + name1);
                System.out.println("Price: " + price);
                System.out.println("Product ID: " + productId);
                addTable(productId, name1, 1, Double.valueOf(price));
                Product_Price_calculation();
            }
        });

        jP12.add(button);
    }

    
}


// Product Price calculation

public void Product_Price_calculation() {
    try {
        
        int numOfRow = Product_list.getRowCount();
        double tot = 0.0;

        // Price calculation
        for (int i = 0; i < numOfRow; i++) {
            try {
                Object value = Product_list.getValueAt(i, 3);
                if (value != null) {
                    String stringValue = value.toString();
                    if (!stringValue.isEmpty()) {
                        double parsedValue = Double.parseDouble(stringValue);
                        tot += parsedValue;
                        DecimalFormat df = new DecimalFormat("0.00");
                        sub_total.setText(df.format(tot));
                    }
                }
            } catch (NumberFormatException | NullPointerException e) {
                System.err.println("Error parsing product value: " + e.getMessage());
            }
        }

        // Total quantity calculation
        double total_qty = 0;
        for (int i = 0; i < numOfRow; i++) {
            try {
                double value = Double.parseDouble(Product_list.getValueAt(i, 2).toString());
                total_qty += value;
                DecimalFormat df = new DecimalFormat("0.00");
                tot_qty.setText(df.format(total_qty));
                
            } catch (NumberFormatException | NullPointerException e) {
                System.err.println("Error parsing product quantity: " + e.getMessage());
            }
        }

        DecimalFormat df = new DecimalFormat("0.00");
        

        String discountText = t_Dis.getText();
        String taxText = t_tax.getText();
        String payedText = pay.getText();

        double discount = discountText.isEmpty() ? 0.0 : Double.parseDouble(discountText);
        double tax = taxText.isEmpty() ? 0.0 : Double.parseDouble(taxText);
        double payed = payedText.isEmpty() ? 0.0 : Double.parseDouble(payedText);

        // Adjusting the tax calculation based on whether tax is a percentage
        double grand_total;
        if (tax <= 1.0) {
            grand_total = tot - discount + (tot * tax); // If tax is a percentage
        } else {
            grand_total = tot - discount + tax; // If tax is an absolute value
        }

        // Set grand total
        total.setText(df.format(grand_total));

        double balance = grand_total - payed;

        // Set balance
        Balance.setText(df.format(balance));

        show_Bill_textarea();

    } catch (NumberFormatException e) {
        System.out.println("Error in input: " + e.getMessage());
    }
}




//Show  bill textarea

public void show_Bill_textarea() {  
    // date and time
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    String formattedDateTime = dtf.format(now);
    //System.out.println("Current Date and Time: " + formattedDateTime);
    
    
    //Get data from DB
    
ArrayList<ShopData> shopDataList = new ArrayList<>();  // Create an ArrayList

    try {
        String sql = "SELECT * FROM shop_data";
        Statement statement = db.mycon().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            ShopData shop = new ShopData(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("Address1"),
                resultSet.getString("Address2"),
                resultSet.getString("Telephone"),
                resultSet.getString("Email"),
                resultSet.getString("Cashier"),
                resultSet.getString("msg1"),
                resultSet.getString("msg2"),
                resultSet.getString("Tax"),
                resultSet.getString("Discount")
                
            );

            shopDataList.add(shop);
        }
    } catch (SQLException e) {
        System.out.println(e);
    }
    
    // Retrieves the first ShopData object from the list
    
        ShopData myshop = shopDataList.get(0); 
        //int id = myshop.getId();
        String name = myshop.getName();
        String Address1 = myshop.getAddress1();
        String Address2 = myshop.getAddress2();
        String Telephone = myshop.getTelephone();
        String Email = myshop.getEmail();
        String Cashier = myshop.getCashier();
        String msg1 = myshop.getMsg1();
        String msg2 = myshop.getMsg2();
        
        
       // System.out.println(myshop.getTax());
    
    
    // text bill 
    
    try {
            
           billContent.setText("                                "+name+" \n");
            billContent.setText(billContent.getText() + "                                "+Address1+" \n");
            billContent.setText(billContent.getText() + "                                "+Address2+" \n");
            billContent.setText(billContent.getText() + "                                "+Telephone+" \n");
            billContent.setText(billContent.getText() + "                                "+Email+" \n");
            billContent.setText(billContent.getText() + "                                                           Receipt #: "+Receipt_No+" \n");
            billContent.setText(billContent.getText() + ""+formattedDateTime+"                               Cashier : "+Cashier+"\n");
            billContent.setText(billContent.getText() + "-----------------------------------------------------------------------------------\n");
            billContent.setText(billContent.getText() + "  Item \t\tQty \tPrice" +"\n");
            billContent.setText(billContent.getText() + "-----------------------------------------------------------------------------------\n");
            
            DefaultTableModel df = (DefaultTableModel) Product_list.getModel();
            
            // get table Product details
            
            for (int i = 0; i < Product_list.getRowCount(); i++) {
                
                String Name = df.getValueAt(i, 1).toString();
                String Qty = df.getValueAt(i, 2).toString();
                String Price = df.getValueAt(i, 3).toString();
                
                billContent.setText(billContent.getText() +"  "+ Name+"\t\t"+Qty +"\t"+Price + "\n");
            }
            
            billContent.setText(billContent.getText() + "-----------------------------------------------------------------------------------\n");
            billContent.setText(billContent.getText() + "Sub Total  :\t" + total.getText() +"\n");
            billContent.setText(billContent.getText() + "Tax %      :\t" + t_tax.getText() +"\n");
            billContent.setText(billContent.getText() + "Discount % :\t" + t_Dis.getText() +"\n");
            billContent.setText(billContent.getText() + "TOTAL      :\t" + total.getText() +"\n");
            billContent.setText(billContent.getText() + "Paid Amount:\t" + pay.getText() +"\n");
            billContent.setText(billContent.getText() + "Balance    :\t" + Balance.getText() +"\n");      
            billContent.setText(billContent.getText() + "-----------------------------------------------------------------------------------\n");
            billContent.setText(billContent.getText() + "                     "+msg1+"\n");
            billContent.setText(billContent.getText() + "                     "+msg2+"\n");
            billContent.setText(billContent.getText() + "-----------------------------------------------------------------------------------\n");
            billContent.setText(billContent.getText() + "         SoftwareFree by Youtube.com/DappCode"+"\n"); //Dont remove this Line
            
           
        
            
            
        } catch (Exception e) {
            
            System.out.println(e);
            
            
        }
        
}  
 
public void billPrint() {
    
        try {

         PrinterJob job = PrinterJob.getPrinterJob();

         job.setPrintable((Graphics graphics, PageFormat pageFormat, int pageIndex) -> {
             int linesPerPage = (int) (pageFormat.getImageableHeight() / 15); // 15 pixels for each line

             int startLine = linesPerPage * pageIndex;

             String billText = billContent.getText();
             String[] lines = billText.split("\n");

             if (startLine >= lines.length) {
                 return NO_SUCH_PAGE;
             }

             Graphics2D g2 = (Graphics2D) graphics;
             g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

             int x1 = 10; // X-coordinate
             int y1 = 10; // Initial Y-coordinate

             Font boldFont = new Font("Serif", Font.BOLD, 12); // Adjust the font style and size as needed

             for (int lineIndex = startLine; lineIndex < Math.min(startLine + linesPerPage, lines.length); lineIndex++) {
                 String line = lines[lineIndex];
                 String[] items = line.split("\t");

                 for (int i = 0; i < items.length; i++) {

                     if (line.contains("TOTAL") && i == 0) { // Identify the "Total" line

                         g2.setFont(boldFont); // Set bold font style for the total amount
                         g2.drawString(items[i], x1 + i * 100, y1); // Draw the bold text
                         g2.setFont(g2.getFont().deriveFont(Font.PLAIN)); // Revert to regular font style

                     } else {
                         g2.drawString(items[i], x1 + i * 100, y1); // Regular text
                     }
                 }
                 y1 += 15; // Move to the next line
             }

             return PAGE_EXISTS;
         });

         PageFormat format = job.defaultPage(); // Get default page format
         Paper paper = format.getPaper();
         paper.setSize(80.0, paper.getHeight()); // Set the paper width to 80.0 units
         format.setPaper(paper);

         PrintService[] printServices = PrinterJob.lookupPrintServices(); // Get available print services

        if (printServices.length > 0) {
            // Set the print service directly (choosing the first available one)
            job.setPrintService(printServices[0]);

            job.print(); // Print all pages
        } else {
            System.out.println("No printer services available.");
        }

     } catch (PrinterException e) {
            System.out.println(e);
     }


}


public void saveInvoice() {
            

               String getsubTotal = sub_total.getText();
               String getTax = t_tax.getText();
               String getDiscount = t_Dis.getText();
               String getTotal = total.getText();
               String getPaid_Amount = pay.getText();
               String getBalance = Balance.getText();
               String getQty = tot_qty.getText();
               LocalDate Date_Now = LocalDate.now();
               
          // Save invoice      
        
        try {
            Statement statement = db.mycon().createStatement();
            statement.executeUpdate("INSERT INTO Invoices (INID,Sub_Total,Tax,Discount,Total,Paid_Amount,Balance,Date,Total_Qty) "
                        + "VALUES ('" + Receipt_No + "','" + getsubTotal + "','" + getTax + "','" + getDiscount + "','" + getTotal + "','" + getPaid_Amount + "','" + getBalance + "','" + Date_Now + "' ,'" +getQty+ "' )");
           

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
     // Save cart products    
      
        try {
            DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
            int rowCount = dt.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                String pId = dt.getValueAt(i, 0).toString();
                String pName = dt.getValueAt(i, 1).toString();
                String pQty = dt.getValueAt(i, 2).toString();
                String pPrice = dt.getValueAt(i, 3).toString();
                
                Statement statement = db.mycon().createStatement();
                statement.executeUpdate("INSERT INTO Cart (INID,Pid,Name,qty,Price,Date) "
                        + "VALUES ('" + Receipt_No + "','" + pId + "','" + pName + "','" + pQty + "','" + pPrice + "','" + Date_Now + "' )");
            }

            JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }
      
        
    // save las inid number    
        int Receipt = Integer.valueOf(Receipt_No);
        Receipt++ ;
    
        try {
                       
            Statement s = db.mycon().createStatement();
            
            s.executeUpdate("UPDATE shop_data SET Receipt_No='"+Receipt+"' WHERE id = 1");
                        
            } catch (SQLException e) {
            System.out.println(e);
            }
       
   
}

public void loadNew() {

        DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
        dt.setRowCount(0); 
        Product_Price_calculation();
        
        billContent.setText("");
        sub_total.setText("00");
        total.setText("00");
        pay.setText("0");
        Balance.setText("00");        
        loadReceiptNo();
}

//end
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jP1 = new javax.swing.JPanel();
        jP2 = new javax.swing.JPanel();
        jP3 = new javax.swing.JPanel();
        jP4 = new javax.swing.JPanel();
        jP5 = new javax.swing.JPanel();
        jP6 = new javax.swing.JPanel();
        jP7 = new javax.swing.JPanel();
        jP8 = new javax.swing.JPanel();
        jP9 = new javax.swing.JPanel();
        jP10 = new javax.swing.JPanel();
        jP11 = new javax.swing.JPanel();
        jP12 = new javax.swing.JPanel();
        jP13 = new javax.swing.JPanel();
        jP14 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        moreProductTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        c_search = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Balance = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        pay = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        t_Dis = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        t_tax = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        sub_total = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        Product_list = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        billContent = new javax.swing.JTextArea();
        tot_qty = new javax.swing.JLabel();
        T_barcode = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("jMenu3");

        jMenu9.setText("jMenu9");

        jMenu6.setText("jMenu6");

        jMenuItem8.setText("jMenuItem8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Free POS Software.Simple POS for Small Businesses. Youtube.com/c/Dappcode and Youtube.com/@sasindu");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 31)); // NOI18N

        javax.swing.GroupLayout jP1Layout = new javax.swing.GroupLayout(jP1);
        jP1.setLayout(jP1Layout);
        jP1Layout.setHorizontalGroup(
            jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP1Layout.setVerticalGroup(
            jP1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP1);

        javax.swing.GroupLayout jP2Layout = new javax.swing.GroupLayout(jP2);
        jP2.setLayout(jP2Layout);
        jP2Layout.setHorizontalGroup(
            jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP2Layout.setVerticalGroup(
            jP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP2);

        javax.swing.GroupLayout jP3Layout = new javax.swing.GroupLayout(jP3);
        jP3.setLayout(jP3Layout);
        jP3Layout.setHorizontalGroup(
            jP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP3Layout.setVerticalGroup(
            jP3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP3);

        javax.swing.GroupLayout jP4Layout = new javax.swing.GroupLayout(jP4);
        jP4.setLayout(jP4Layout);
        jP4Layout.setHorizontalGroup(
            jP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP4Layout.setVerticalGroup(
            jP4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP4);

        javax.swing.GroupLayout jP5Layout = new javax.swing.GroupLayout(jP5);
        jP5.setLayout(jP5Layout);
        jP5Layout.setHorizontalGroup(
            jP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP5Layout.setVerticalGroup(
            jP5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP5);

        javax.swing.GroupLayout jP6Layout = new javax.swing.GroupLayout(jP6);
        jP6.setLayout(jP6Layout);
        jP6Layout.setHorizontalGroup(
            jP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP6Layout.setVerticalGroup(
            jP6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP6);

        javax.swing.GroupLayout jP7Layout = new javax.swing.GroupLayout(jP7);
        jP7.setLayout(jP7Layout);
        jP7Layout.setHorizontalGroup(
            jP7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP7Layout.setVerticalGroup(
            jP7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP7);

        javax.swing.GroupLayout jP8Layout = new javax.swing.GroupLayout(jP8);
        jP8.setLayout(jP8Layout);
        jP8Layout.setHorizontalGroup(
            jP8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP8Layout.setVerticalGroup(
            jP8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP8);

        javax.swing.GroupLayout jP9Layout = new javax.swing.GroupLayout(jP9);
        jP9.setLayout(jP9Layout);
        jP9Layout.setHorizontalGroup(
            jP9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP9Layout.setVerticalGroup(
            jP9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP9);

        javax.swing.GroupLayout jP10Layout = new javax.swing.GroupLayout(jP10);
        jP10.setLayout(jP10Layout);
        jP10Layout.setHorizontalGroup(
            jP10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP10Layout.setVerticalGroup(
            jP10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP10);

        javax.swing.GroupLayout jP11Layout = new javax.swing.GroupLayout(jP11);
        jP11.setLayout(jP11Layout);
        jP11Layout.setHorizontalGroup(
            jP11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP11Layout.setVerticalGroup(
            jP11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP11);

        javax.swing.GroupLayout jP12Layout = new javax.swing.GroupLayout(jP12);
        jP12.setLayout(jP12Layout);
        jP12Layout.setHorizontalGroup(
            jP12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP12Layout.setVerticalGroup(
            jP12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP12);

        javax.swing.GroupLayout jP13Layout = new javax.swing.GroupLayout(jP13);
        jP13.setLayout(jP13Layout);
        jP13Layout.setHorizontalGroup(
            jP13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP13Layout.setVerticalGroup(
            jP13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("CAT", jP13);

        javax.swing.GroupLayout jP14Layout = new javax.swing.GroupLayout(jP14);
        jP14.setLayout(jP14Layout);
        jP14Layout.setHorizontalGroup(
            jP14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 681, Short.MAX_VALUE)
        );
        jP14Layout.setVerticalGroup(
            jP14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 818, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Catxyz", jP14);

        moreProductTable.setFont(new java.awt.Font("Segoe UI", 0, 30)); // NOI18N
        moreProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Price", "Barcode"
            }
        ));
        moreProductTable.setAlignmentX(9.0F);
        moreProductTable.setEditingColumn(6);
        moreProductTable.setEditingRow(6);
        moreProductTable.setInheritsPopupMenu(true);
        moreProductTable.setName(""); // NOI18N
        moreProductTable.setRowHeight(60);
        moreProductTable.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        moreProductTable.setShowGrid(true);
        moreProductTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                moreProductTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(moreProductTable);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Search ID/Name :");

        c_search.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        c_search.setText("0");
        c_search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                c_searchMouseClicked(evt);
            }
        });
        c_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_searchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(c_search)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(c_search, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 761, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("CAT", jPanel19);

        jPanel6.setBackground(new java.awt.Color(30, 91, 6));

        jLabel7.setBackground(new java.awt.Color(0, 0, 51));
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("TOTAL :");
        jLabel7.setOpaque(true);

        jLabel8.setBackground(new java.awt.Color(0, 102, 102));
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("CASH :");
        jLabel8.setOpaque(true);

        jLabel9.setBackground(new java.awt.Color(0, 153, 51));
        jLabel9.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Balance :");
        jLabel9.setOpaque(true);

        Balance.setBackground(new java.awt.Color(0, 153, 51));
        Balance.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        Balance.setForeground(new java.awt.Color(255, 255, 255));
        Balance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Balance.setText("00");
        Balance.setOpaque(true);

        total.setBackground(new java.awt.Color(0, 0, 51));
        total.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        total.setForeground(new java.awt.Color(255, 255, 255));
        total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        total.setText("00");
        total.setOpaque(true);

        pay.setBackground(new java.awt.Color(0, 102, 102));
        pay.setFont(new java.awt.Font("Dialog", 0, 30)); // NOI18N
        pay.setForeground(new java.awt.Color(255, 255, 255));
        pay.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pay.setText("0");
        pay.setOpaque(true);
        pay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paypayKeyReleased(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton12.setText("Pay");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12jButton11ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tax  %:");

        t_Dis.setFont(new java.awt.Font("Dialog", 1, 25)); // NOI18N
        t_Dis.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_Dis.setText("0");
        t_Dis.setOpaque(true);
        t_Dis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_Dist_DisActionPerformed(evt);
            }
        });
        t_Dis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_Dist_DisKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Discount %:");

        t_tax.setFont(new java.awt.Font("Dialog", 1, 25)); // NOI18N
        t_tax.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_tax.setText("0");
        t_tax.setOpaque(true);
        t_tax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_taxt_taxActionPerformed(evt);
            }
        });
        t_tax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_taxt_taxKeyReleased(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jButton15.setText("Print");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15jButton14ActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(22, 68, 4));
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 23)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("SUB TOTAL :");
        jLabel12.setOpaque(true);

        sub_total.setBackground(new java.awt.Color(22, 68, 4));
        sub_total.setFont(new java.awt.Font("Dialog", 0, 30)); // NOI18N
        sub_total.setForeground(new java.awt.Color(255, 255, 255));
        sub_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sub_total.setText("00");
        sub_total.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sub_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pay)
                            .addComponent(total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tax, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(t_Dis, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sub_total, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_Dis, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(t_tax, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pay, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Balance)
                            .addComponent(jLabel9)))
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton13.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jButton13.setText("Delete");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButton2.setText("-");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        Product_list.setFont(new java.awt.Font("Consolas", 0, 20)); // NOI18N
        Product_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Iteam", "Qty", "Price"
            }
        ));
        Product_list.setRowHeight(30);
        Product_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Product_listMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(Product_list);

        jTabbedPane2.addTab("Shopping Cart ", jScrollPane2);

        jScrollPane3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        billContent.setEditable(false);
        billContent.setColumns(20);
        billContent.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        billContent.setRows(5);
        billContent.setFocusable(false);
        billContent.setOpaque(false);
        jScrollPane3.setViewportView(billContent);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(126, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Print View", jPanel2);

        tot_qty.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        tot_qty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tot_qty.setText("00");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tot_qty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tot_qty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        T_barcode.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        T_barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                T_barcodeKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("BarCode  :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 795, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(T_barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 592, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(T_barcode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        getContentPane().add(jPanel1);

        jMenu4.setText("File");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem6.setText("New");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem6);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItem3.setText("About");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        jMenuItem4.setText("Exit");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem4);

        jMenuBar2.add(jMenu4);

        jMenu5.setText("Edit");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/invo.png"))); // NOI18N
        jMenuItem1.setText("Edit Products");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/product.png"))); // NOI18N
        jMenuItem2.setText("Edit Category");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem2);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/customer.png"))); // NOI18N
        jMenuItem5.setText("Shop Data");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuBar2.add(jMenu5);

        setJMenuBar(jMenuBar2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paypayKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paypayKeyReleased
        Product_Price_calculation();
    }//GEN-LAST:event_paypayKeyReleased

    private void jButton12jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12jButton11ActionPerformed
        // pat btn
        saveInvoice();
        loadReceiptNo();
        loadNew();
        
    }//GEN-LAST:event_jButton12jButton11ActionPerformed

    private void t_Dist_DisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_Dist_DisActionPerformed

    }//GEN-LAST:event_t_Dist_DisActionPerformed

    private void t_Dist_DisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_Dist_DisKeyReleased
        Product_Price_calculation();
    }//GEN-LAST:event_t_Dist_DisKeyReleased

    private void t_taxt_taxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_taxt_taxActionPerformed

    }//GEN-LAST:event_t_taxt_taxActionPerformed

    private void t_taxt_taxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_taxt_taxKeyReleased
        Product_Price_calculation();
    }//GEN-LAST:event_t_taxt_taxKeyReleased

    private void jButton15jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15jButton14ActionPerformed
        
        billPrint();
        saveInvoice();
        loadReceiptNo();
        loadNew();
    

    }//GEN-LAST:event_jButton15jButton14ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        int selectedRow = Product_list.getSelectedRow();

        if (selectedRow != -1) { // Ensure a row is selected
            DefaultTableModel model = (DefaultTableModel) Product_list.getModel();
            model.removeRow(selectedRow); // Remove the selected row from the table

            Product_Price_calculation();
        } else {
            // Inform the user to select a row
            JOptionPane.showMessageDialog(null, "Please select a Product to delete.");
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int selectedRow = Product_list.getSelectedRow();

        if (selectedRow != -1) { // Ensure a row is selected
            int currentQty = (int) Product_list.getValueAt(selectedRow, 2); // Assuming Qty is at column 2
            double currentPrice = (double) Product_list.getValueAt(selectedRow, 3); // Assuming Price is at column 3

            int incrementedQty = currentQty + 1; // Increment the quantity by 1
            double updatedPrice = currentPrice * (1 + (1.0 / currentQty)); // Update price based on quantity change

            Product_list.setValueAt(incrementedQty, selectedRow, 2); // Update the table with the new quantity
            Product_list.setValueAt(updatedPrice, selectedRow, 3); // Update the table with the updated price

            Product_Price_calculation();

        } else {
            // Inform the user to select a row
            JOptionPane.showMessageDialog(null, "Please select a row to increment its quantity and price.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        int rowCount = Product_list.getRowCount();

        if (rowCount > 0) { // Check if the table has rows
            int selectedRow = Product_list.getSelectedRow();

            if (selectedRow != -1 && selectedRow < rowCount) { // Ensure a valid row is selected
                int currentQty = (int) Product_list.getValueAt(selectedRow, 2); // Assuming Qty is at column 2
                double currentPrice = (double) Product_list.getValueAt(selectedRow, 3); // Assuming Price is at column 3

                if (currentQty > 0) { // Ensure quantity is more than 0 before decrement
                    int decrementedQty = currentQty - 1; // Decrement the quantity by 1

                    Product_list.setValueAt(decrementedQty, selectedRow, 2); // Update the table with the new quantity

                    double updatedPrice;

                    if (decrementedQty != 0) {
                        updatedPrice = currentPrice / currentQty * decrementedQty; // Adjust price according to the quantity change
                    } else {
                        updatedPrice = 0; // If quantity becomes zero, set the price to zero

                        DefaultTableModel model = (DefaultTableModel) Product_list.getModel();
                        model.removeRow(selectedRow); // Remove the row if quantity becomes zero
                    }

                    Product_list.setValueAt(updatedPrice, selectedRow, 3); // Update the table with the updated price
                    Product_Price_calculation(); // Perform necessary price calculation

                } else {
                    JOptionPane.showMessageDialog(null, "The quantity for this item is already zero.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a valid row to decrement its quantity and price.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "The table is empty. Add items before decrementing quantity.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void Product_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Product_listMouseClicked
        
             
    }//GEN-LAST:event_Product_listMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Category cp = new Category();
        cp.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Products pc = new Products();
        pc.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        About ab = new About();
        ab.setVisible(true);
       
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Shop_Name shopname = new Shop_Name();
        shopname.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        
        loadNew();    
 
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void T_barcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_T_barcodeKeyReleased

           // Barcode scanning and product retrieval
            String barcodeId = T_barcode.getText(); // Assuming T_barcode is a JTextField for the barcode

            if (!barcodeId.isEmpty()) {
                try {
                    String query = "SELECT * FROM Products WHERE Barcode = '" + barcodeId + "'";
                    try (Statement statement = db.mycon().createStatement();
                         ResultSet resultSet = statement.executeQuery(query)) {

                        if (resultSet.next()) {
                            int productId = resultSet.getInt("id");
                            String name = resultSet.getString("Name");
                            double price = resultSet.getDouble("Price");

                            // Assuming addTable() method is used to add retrieved product to a table
                            addTable(productId, name, 1, price);
                            Product_Price_calculation();

                            T_barcode.setText(""); // Clear the barcode field after successful retrieval
                        } else {
                            // If the query did not return any results, display a message
                            //System.out.println("Barcode " + barcodeId + " does not show any data in the database.");
                            JOptionPane.showMessageDialog(null, "Barcode " + barcodeId + " does not show any data in the database.");
                            T_barcode.setText("");
                            // You can also prompt a message to the user using a dialog box or another UI component
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error retrieving product: " + e.getMessage());
                }
            } else {
                // Handle the case when the barcode field is empty
                //System.out.println("Barcode field is empty. Please enter a valid barcode.");
                // You can also prompt a message to the user using a dialog box or another UI component
            }
            

    }//GEN-LAST:event_T_barcodeKeyReleased

    private void c_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_searchKeyReleased

        
        String your_desired_id = c_search.getText();

        DefaultTableModel dt = (DefaultTableModel) moreProductTable.getModel();
        dt.setRowCount(0);

        if (your_desired_id == null || your_desired_id.isEmpty()) {
            loadMoreProducts();
            
        } else {
            try (Statement s = db.mycon().createStatement()) {
                String query = "SELECT * FROM Products WHERE ID LIKE '%" + your_desired_id + "%' OR name LIKE '%"+your_desired_id+"%' OR Barcode ='"+your_desired_id+"' ";
                try (ResultSet rs = s.executeQuery(query)) {
                    while (rs.next()) {
                        Object[] rowData = {
                            rs.getString("id"),
                            rs.getString("Name"),
                            rs.getString("price"),
                            rs.getString("Barcode"),
                            rs.getString(4)}; // 
                        dt.addRow(rowData);
                    }
                }
            } catch (SQLException e) {
                loadMoreProducts();
                System.out.println(e);
            }
        }
    }//GEN-LAST:event_c_searchKeyReleased

    private void moreProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moreProductTableMouseClicked
                
       int r = moreProductTable.getSelectedRow();
       
       String productId = moreProductTable.getValueAt(r, 0).toString();
       String name   = moreProductTable.getValueAt(r, 1).toString();
       String price = moreProductTable.getValueAt(r, 2).toString();
       
        
        addTable(Integer.valueOf(productId), name, 1, Double.valueOf(price));
        Product_Price_calculation();
        
    }//GEN-LAST:event_moreProductTableMouseClicked

    private void c_searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_c_searchMouseClicked
        c_search.setText("");
        loadMoreProducts();
    }//GEN-LAST:event_c_searchMouseClicked

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Balance;
    private javax.swing.JTable Product_list;
    private javax.swing.JTextField T_barcode;
    private javax.swing.JTextArea billContent;
    private javax.swing.JTextField c_search;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jP1;
    private javax.swing.JPanel jP10;
    private javax.swing.JPanel jP11;
    private javax.swing.JPanel jP12;
    private javax.swing.JPanel jP13;
    private javax.swing.JPanel jP14;
    private javax.swing.JPanel jP2;
    private javax.swing.JPanel jP3;
    private javax.swing.JPanel jP4;
    private javax.swing.JPanel jP5;
    private javax.swing.JPanel jP6;
    private javax.swing.JPanel jP7;
    private javax.swing.JPanel jP8;
    private javax.swing.JPanel jP9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable moreProductTable;
    private javax.swing.JTextField pay;
    private javax.swing.JLabel sub_total;
    private javax.swing.JTextField t_Dis;
    private javax.swing.JTextField t_tax;
    private javax.swing.JLabel tot_qty;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
