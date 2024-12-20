package shop_pos;


import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class SupermarketBillPrinter implements Printable {

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }

        // Set up the font and text size
        Font font = new Font("Arial", Font.PLAIN, 12);
        Font fonts = new Font("Iskoola Pota", Font.PLAIN, 12);
        g.setFont(font);

        // Define coordinates and margins
        int x = 50; // X coordinate for text
        int y = 50; // Y coordinate for text
        int lineHeight = g.getFontMetrics().getHeight() + 5; // Line height
        int lineSpacing = 5; // Vertical spacing between lines
        int leftMargin = 50; // Left margin
        int rightMargin = (int) (pf.getImageableWidth() - 50); // Right margin

        // Sample data for the bill
        String shopName = "Your Supermarket";
        String address1 = "123 Main St";
        String address2 = "City, Country";
        String contact = "Contact: 012-345-6789";
        g.setFont(fonts);
        String itemName = "සිංහල";
        g.setFont(font);
        String itemQuantity = "2";
        String itemPrice = "10.00"; // Price per item
        String totalPrice = "20.00"; // Total price for the item

        // Draw the bill content using the Graphics object
        g.drawString(shopName, x, y);
        y += lineHeight + lineSpacing;
        g.drawString(address1, x, y);
        y += lineHeight + lineSpacing;
        g.drawString(address2, x, y);
        y += lineHeight + lineSpacing;
        g.drawString(contact, x, y);
        y += lineHeight + lineSpacing;

        g.drawLine(leftMargin, y, rightMargin, y); // Draw a line
        y += lineHeight + lineSpacing;

        g.drawString("Item Name", x, y);
        g.drawString("Quantity", x + 150, y);
        g.drawString("Price", x + 250, y);
        g.drawString("Total", x + 350, y);
        y += lineHeight + lineSpacing;

        g.drawLine(leftMargin, y, rightMargin, y); // Draw a line
        y += lineHeight + lineSpacing;

        g.drawString(itemName, x, y);
        g.drawString(itemQuantity, x + 150, y);
        g.drawString(itemPrice, x + 250, y);
        g.drawString(totalPrice, x + 350, y);
        y += lineHeight + lineSpacing;

        // Draw more items if needed...

        return Printable.PAGE_EXISTS;
    }

    public static void main(String[] args) {
        // Create a PrinterJob to print the bill
        SupermarketBillPrinter billPrinter = new SupermarketBillPrinter();
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(billPrinter);

        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }
}
