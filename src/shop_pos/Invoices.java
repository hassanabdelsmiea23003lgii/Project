/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package shop_pos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public final class Invoices extends javax.swing.JFrame {
public String LastMsg ;
    /**
     * Creates new form Invoices
     */
    public Invoices() {
        initComponents();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        tb_load();
        Product_list.getColumnModel().getColumn(0).setPreferredWidth(50);
        Product_list.getColumnModel().getColumn(1).setPreferredWidth(1);
        Product_list.getColumnModel().getColumn(2).setPreferredWidth(1);
        Product_list.getColumnModel().getColumn(3).setPreferredWidth(250);
        Product_list.getColumnModel().getColumn(4).setPreferredWidth(1);
        Product_list.getColumnModel().getColumn(4).setPreferredWidth(1);

        Invoice_total();
        simpleAccounts();

    }

    public void tb_load() {
        //table

        DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
        dt.setRowCount(0);

        try ( Statement s = (Statement) db.mycon().createStatement();  ResultSet rs = s.executeQuery("SELECT Inid,Pid,Name,Qty,Price,Date FROM Cart")) {

            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("Date"),
                    rs.getString("Inid"),
                    rs.getString("Pid"),
                    rs.getString("Name"),
                    rs.getString("Qty"),
                    rs.getString("Price"),};
                dt.addRow(rowData);

            }

        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void loadNew() {

        DateNow.setText("");
        sub_total.setText("00");
        total.setText("00");
        pay.setText("0");
        t_tax.setText("");
        Balance.setText("00");

    }

    public void searchInvoice() {
        
        String your_desired_id = c_search.getText();
        String your_date = text_Date.getText();

        DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
        dt.setRowCount(0);

        if (your_desired_id.isEmpty() || your_date.isEmpty()) {
            loadNew();
            tb_load();
        } else {
            try {
                String query = "SELECT * FROM Cart WHERE Inid = ? OR Date =?";
                PreparedStatement ps = db.mycon().prepareStatement(query);
                ps.setString(1, your_desired_id);
                ps.setString(2, your_date);
                ResultSet rs = ps.executeQuery();
                //System.out.println("Generated Query: " + ps.toString());

                while (rs.next()) {
                    Object[] rowData = {
                        rs.getString("Date"),
                        rs.getString("Inid"),
                        rs.getString("Pid"),
                        rs.getString("Name"),
                        rs.getString("Qty"),
                        rs.getString("Price"),};
                    dt.addRow(rowData);

                    sub_total.setText(rs.getString("Sub_Total"));
                    t_tax.setText(rs.getString("Tax"));
                    t_Dis.setText(rs.getString("Discount"));
                    total.setText(rs.getString("Total"));
                    pay.setText(rs.getString("Paid_Amount"));
                    Balance.setText(rs.getString("Balance"));
                    DateNow.setText(rs.getString("Date"));
                }
            } catch (SQLException e) {
                tb_load();
                loadNew();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    
    public void Invoice_total() {

        int search_total = Product_list.getRowCount();
        double total_amount = 0;

        if (search_total == 0) {
            total.setText("00");
        } else {
            for (int i = 0; i < search_total; i++) {
                double value = Double.valueOf(Product_list.getValueAt(i, 5).toString());
                total_amount += value;
            }

            DecimalFormat df = new DecimalFormat("0.00");
            LastMsg = "With Out Tax :"+df.format(total_amount);
        }
    }

  public void simpleAccounts() {
      
      
      
      LocalDate currentDate = LocalDate.now();
      LocalDate previousDate = currentDate.minusDays(1);
      //System.out.println(currentDate);
      //System.out.println(previousDate);
      
      try {
          
          String query = "SELECT SUM(Total) AS total FROM Invoices WHERE Date=?";
            PreparedStatement preparedStatement = db.mycon().prepareStatement(query);
            preparedStatement.setString(1, previousDate.toString());            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double Total = resultSet.getDouble("total");
                DecimalFormat df = new DecimalFormat("0.00");
                      
                
                    if (resultSet.wasNull()) {
                        yetady_total.setText("0.0"); // Set default value if the result is null
                    } else {
                        yetady_total.setText(df.format(Total));
                    }
            } else {
                yetady_total.setText("0.0"); // Set default value if no records found
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
      
      // today
      try {
          
          String query = "SELECT SUM(Total) AS total FROM Invoices WHERE Date=?";
            PreparedStatement preparedStatement = db.mycon().prepareStatement(query);
            preparedStatement.setString(1, currentDate.toString());            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double Total = resultSet.getDouble("total");
                DecimalFormat df = new DecimalFormat("0.00");
                      
                
                    if (resultSet.wasNull()) {
                        today_amounts.setText("0.0"); // Set default value if the result is null
                    } else {
                        today_amounts.setText(df.format(Total));
                    }
            } else {
                today_amounts.setText("0.0"); // Set default value if no records found
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
      
      
    // text search 
        String  textDate = text_Date.getText();
        
    try {
          
          String query = "SELECT SUM(Total) AS total FROM Invoices WHERE Date=?";
            PreparedStatement preparedStatement = db.mycon().prepareStatement(query);
            preparedStatement.setString(1, textDate);            
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double Total = resultSet.getDouble("total");
                DecimalFormat df = new DecimalFormat("0.00");
                      
                
                    if (resultSet.wasNull()) {
                        
                        total.setText("0.0"); // Set default value if the result is null
                    
                    } else {
                        total.setText(LastMsg +" |       With Tax : "+df.format(Total));
                    }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    
    
    // All Total Amounts
    
      try {
          String query = "SELECT SUM(Total) AS total FROM Invoices";
          Statement statement = db.mycon().createStatement();
          ResultSet resultSet = statement.executeQuery(query);

          if (resultSet.next()) {
              double Total = resultSet.getDouble("total");
              DecimalFormat df = new DecimalFormat("0.00");

              if (resultSet.wasNull()) {
                  All_total.setText("0.0"); // Set default value if the result is null
              } else {
                  All_total.setText(df.format(Total));
              }
          } else {
              All_total.setText("0.0"); // Set default value if no records found
          }
      } catch (SQLException e) {
          System.out.println(e);
      }

    
    }  
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        c_search = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        text_Date = new javax.swing.JTextField();
        DateNow = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Product_list = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        Balance = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        pay = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        t_Dis = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        t_tax = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        sub_total = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        s_date = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        e_data = new javax.swing.JTextField();
        betwinDateTotalAmounts = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        today_amounts = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        All_total = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        yetady_total = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Free POS Software.Simple POS for Small Businesses. Youtube.com/c/Dappcode and Youtube.com/@sasindu");
        setAlwaysOnTop(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("Receipt_Noe :");

        c_search.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        c_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                c_searchKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel4.setText("Date :");

        text_Date.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        text_Date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                text_DateKeyReleased(evt);
            }
        });

        DateNow.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        DateNow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(23, 23, 23)
                .addComponent(c_search, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(text_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DateNow, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(DateNow, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(text_Date, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(c_search)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        Product_list.setFont(new java.awt.Font("Consolas", 0, 21)); // NOI18N
        Product_list.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "Receipt_No", "ID", "Name", "Qty", "Price"
            }
        ));
        Product_list.setRowHeight(30);
        Product_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Product_listMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Product_list);

        jLabel7.setBackground(new java.awt.Color(0, 0, 51));
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 30)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("TOTAL : ");
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

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tax  % :");

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
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Discount % :");

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

        jLabel12.setBackground(new java.awt.Color(22, 68, 4));
        jLabel12.setFont(new java.awt.Font("Dialog", 0, 23)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("SUB TOTAL : ");
        jLabel12.setOpaque(true);

        sub_total.setBackground(new java.awt.Color(22, 68, 4));
        sub_total.setFont(new java.awt.Font("Dialog", 0, 30)); // NOI18N
        sub_total.setForeground(new java.awt.Color(255, 255, 255));
        sub_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sub_total.setText("00");
        sub_total.setOpaque(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pay)
                            .addComponent(Balance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(sub_total, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(t_tax, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(t_Dis, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sub_total, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_tax)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(t_Dis)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pay, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(Balance))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Accounts"));

        s_date.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        s_date.setText("2023-11-01");
        s_date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                s_dateKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel5.setText("Start :");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel6.setText("End  :");

        e_data.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        e_data.setText("2023-11-05");
        e_data.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                e_dataKeyReleased(evt);
            }
        });

        betwinDateTotalAmounts.setBackground(new java.awt.Color(0, 102, 102));
        betwinDateTotalAmounts.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        betwinDateTotalAmounts.setForeground(new java.awt.Color(255, 255, 255));
        betwinDateTotalAmounts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        betwinDateTotalAmounts.setText("00");
        betwinDateTotalAmounts.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        betwinDateTotalAmounts.setOpaque(true);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("To Day : ");

        today_amounts.setBackground(new java.awt.Color(0, 102, 102));
        today_amounts.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        today_amounts.setForeground(new java.awt.Color(255, 255, 255));
        today_amounts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        today_amounts.setText("00");
        today_amounts.setOpaque(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search x30.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Total Amounts :");

        All_total.setBackground(new java.awt.Color(0, 102, 102));
        All_total.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        All_total.setForeground(new java.awt.Color(255, 255, 255));
        All_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        All_total.setText("00");
        All_total.setOpaque(true);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Yesterday :");

        yetady_total.setBackground(new java.awt.Color(0, 102, 102));
        yetady_total.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        yetady_total.setForeground(new java.awt.Color(255, 255, 255));
        yetady_total.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        yetady_total.setText("00");
        yetady_total.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(s_date, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(e_data, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(betwinDateTotalAmounts, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(today_amounts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(All_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yetady_total, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(s_date)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(e_data)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(betwinDateTotalAmounts, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(All_total, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yetady_total, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(today_amounts, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void c_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_c_searchKeyReleased

       String your_desired_id = c_search.getText();
            DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
            dt.setRowCount(0);

            if (your_desired_id == null || your_desired_id.isEmpty()) {
                loadNew();
                tb_load();
            } else {
                try {
                    String query = "SELECT Cart.Date AS CartDate, Cart.Inid, Cart.Pid, Cart.Name, Cart.Qty, Cart.Price, " +
                                   "Invoices.Sub_Total, Invoices.Tax, Invoices.Discount, Invoices.Total, " +
                                   "Invoices.Paid_Amount, Invoices.Balance, Invoices.Date AS InvoiceDate " +
                                   "FROM Cart " +
                                   "JOIN Invoices ON Cart.Inid = Invoices.Inid " +
                                   "WHERE Cart.Inid = ?";
                    PreparedStatement ps = db.mycon().prepareStatement(query);
                    ps.setString(1, your_desired_id);

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Object[] rowData = {
                            rs.getString("CartDate"),
                            rs.getString("Inid"),
                            rs.getString("Pid"),
                            rs.getString("Name"),
                            rs.getString("Qty"),
                            rs.getString("Price")
                        };
                        dt.addRow(rowData);

                        sub_total.setText(rs.getString("Sub_Total"));
                        t_tax.setText(rs.getString("Tax"));
                        t_Dis.setText(rs.getString("Discount"));
                        total.setText(rs.getString("Total"));
                        pay.setText(rs.getString("Paid_Amount"));
                        Balance.setText(rs.getString("Balance"));
                        DateNow.setText(rs.getString("InvoiceDate"));
                    }
                } catch (SQLException e) {
                    tb_load();
                    loadNew();
                    System.out.println("Error: " + e.getMessage());
                }
            }

    }//GEN-LAST:event_c_searchKeyReleased

    private void text_DateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_text_DateKeyReleased

        String your_date = text_Date.getText();

        DefaultTableModel dt = (DefaultTableModel) Product_list.getModel();
        dt.setRowCount(0);

        if (your_date == null || your_date.isEmpty()) {
            loadNew();
            tb_load();
        } else {
            try {
                String query = "SELECT * FROM Cart WHERE Date LIKE ?";
                PreparedStatement ps = db.mycon().prepareStatement(query);
                ps.setString(1, "%" + your_date + "%");
                ResultSet rs = ps.executeQuery();
                //System.out.println("Generated Query: " + ps.toString());

                while (rs.next()) {
                    Object[] rowData = {
                        rs.getString("Date"),
                        rs.getString("Inid"),
                        rs.getString("Pid"),
                        rs.getString("Name"),
                        rs.getString("Qty"),
                        rs.getString("Price"),};
                    dt.addRow(rowData);

                }
            } catch (SQLException e) {
                tb_load();
                loadNew();
                System.out.println("Error: " + e.getMessage());
            }
        }
        Invoice_total();
        simpleAccounts();
    }//GEN-LAST:event_text_DateKeyReleased

    private void Product_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Product_listMouseClicked

    }//GEN-LAST:event_Product_listMouseClicked

    private void paypayKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paypayKeyReleased

    }//GEN-LAST:event_paypayKeyReleased

    private void t_Dist_DisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_Dist_DisActionPerformed

    }//GEN-LAST:event_t_Dist_DisActionPerformed

    private void t_Dist_DisKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_Dist_DisKeyReleased

    }//GEN-LAST:event_t_Dist_DisKeyReleased

    private void t_taxt_taxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_taxt_taxActionPerformed

    }//GEN-LAST:event_t_taxt_taxActionPerformed

    private void t_taxt_taxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_taxt_taxKeyReleased

    }//GEN-LAST:event_t_taxt_taxKeyReleased

    private void s_dateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_s_dateKeyReleased
       
    }//GEN-LAST:event_s_dateKeyReleased

    private void e_dataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_e_dataKeyReleased
       
    }//GEN-LAST:event_e_dataKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
          
          String startDate = s_date.getText(); // Example start date
          String endDate = e_data.getText(); // Example end date          

          String query = "SELECT SUM(Total) AS total FROM Invoices WHERE Date BETWEEN ? AND ?";
            PreparedStatement preparedStatement = db.mycon().prepareStatement(query);
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double Total = resultSet.getDouble("total");
                
                    if (resultSet.wasNull()) {
                        betwinDateTotalAmounts.setText("0.0"); // Set default value if the result is null
                    } else {
                        
                        DecimalFormat df = new DecimalFormat("0.00");
                        betwinDateTotalAmounts.setText(df.format(Total));
                    }
            } else {
                betwinDateTotalAmounts.setText("0.0"); // Set default value if no records found
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Invoices.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Invoices.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Invoices.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Invoices.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Invoices().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel All_total;
    private javax.swing.JLabel Balance;
    private javax.swing.JLabel DateNow;
    private javax.swing.JTable Product_list;
    private javax.swing.JLabel betwinDateTotalAmounts;
    private javax.swing.JTextField c_search;
    private javax.swing.JTextField e_data;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField pay;
    private javax.swing.JTextField s_date;
    private javax.swing.JLabel sub_total;
    private javax.swing.JTextField t_Dis;
    private javax.swing.JTextField t_tax;
    private javax.swing.JTextField text_Date;
    private javax.swing.JLabel today_amounts;
    private javax.swing.JLabel total;
    private javax.swing.JLabel yetady_total;
    // End of variables declaration//GEN-END:variables
}
