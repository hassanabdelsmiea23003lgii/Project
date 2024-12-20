package shop_pos;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRCodePanel extends JPanel {
    private String qrCodeData;
    private String labelText;
    private BufferedImage qrCodeImage;

    public QRCodePanel(String qrCodeData, String labelText, BufferedImage qrCodeImage) {
        this.qrCodeData = qrCodeData;
        this.labelText = labelText;
        this.qrCodeImage = qrCodeImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the QR code image
        if (qrCodeImage != null) {
            g.drawImage(qrCodeImage, 50, 50, null);
        }

        // Draw the QR code data and label text in JLabels
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("QR Code Data: " + qrCodeData, 50, 400);
        g.drawString("Label Text: " + labelText, 50, 430);
    }

    public static void main(String[] args) {
        String qrCodeData = "YourQRCodeData";
        String labelText = "YourLabelText";

        // Split the combined data into qrCodeData and labelText
        String combinedData = qrCodeData + "," + labelText;
        String[] dataParts = combinedData.split(",");
        qrCodeData = dataParts[0];
        labelText = dataParts[1];

        // Load or generate the QR code image (replace this with your QR code generation logic)
        BufferedImage qrCodeImage = null;
        try {
            qrCodeImage = generateQRCodeImage(qrCodeData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a JFrame and add the QRCodePanel
        JFrame frame = new JFrame("QR Code Panel");
        QRCodePanel qrCodePanel = new QRCodePanel(qrCodeData, labelText, qrCodeImage);
        frame.add(qrCodePanel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Replace this method with your actual QR code generation logic
    private static BufferedImage generateQRCodeImage(String qrCodeData) throws IOException {
        // This is a placeholder method, replace it with your QR code generation logic
        // For example, you can use the QR code generation code from the previous examples
        return null;
    }
}
