package com.socs.oneCardIDManagement.temp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.nio.file.Paths;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class IDCardGenerator {

    public static void generateIDCard(String name, String idNumber, String email, String photoPath)
            throws IOException, WriterException {
        // ID card dimensions (in pixels) – similar to credit card size
        int width = 860;
        int height = 540;

        // Create a blank ID card image
        BufferedImage idCard = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = idCard.createGraphics();

        // Set background color to white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw borders (optional)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(10, 10, width - 20, height - 20);

        // Add Student Photo (position: top-left)
        BufferedImage studentPhoto = ImageIO.read(new File(photoPath));
        g2d.drawImage(studentPhoto, 40, 40, 160, 200, null); // Adjust size as needed

        // Set Font for Text
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.setColor(Color.BLACK);

        // Draw Student Name (below photo)
        g2d.drawString("Name: " + name, 220, 100);
        g2d.drawString("ID: " + idNumber, 220, 160);
        g2d.drawString("Email: " + email, 220, 220);

        // Generate and Add QR Code (position: bottom-right)
        BufferedImage qrCode = generateQRCode(idNumber);
        g2d.drawImage(qrCode, 650, 360, 150, 150, null);

        // Dispose Graphics context and save the image
        g2d.dispose();
        ImageIO.write(idCard, "png", new File("generatedIDCard.png"));

        System.out.println("ID Card generated successfully!");
    }

    // Method to generate a QR Code with the student's ID
    private static BufferedImage generateQRCode(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 150, 150);

        BufferedImage qrImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 150; x++) {
            for (int y = 0; y < 150; y++) {
                qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return qrImage;
    }

    public static void main(String[] args) {
        try {
            generateIDCard("John Doe", "123456789", "john.doe@example.com",
                    "C:\\Users\\HENOK\\Documents\\pic\\henok.jpg");
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }
}
