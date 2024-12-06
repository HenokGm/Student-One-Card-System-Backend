package com.socs.oneCardIDManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.zxing.WriterException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import com.socs.studentManagement.Student;

@Service
public class IDCardService {

    @Autowired
    private CodeGeneratorService codeGeneratorService;

    @Autowired
    private IDCardRepository idCardRepository;

    // Generate an incremental 6-digit card ID starting with '7'
    private String generateCardId() {
        Random random = new Random();
        int randomNumber = 700000 + random.nextInt(100000); // Ensures the number starts with 7
        return String.valueOf(randomNumber);
    }

    // Generate an ID card for the student and store it locally
    public IDCard generateAndStoreIDCard(Optional<Student> dbStudent) throws IOException, WriterException {
        String cardId = generateCardId();

        if (dbStudent.isEmpty()) {
            throw new RuntimeException("Student not found");
        }

        Student student = dbStudent.get();

        // Check if an ID card already exists for the student
        // Optional<IDCard> existingCard =
        // idCardRepository.findByStudentId(student.getStudentId());
        // if (existingCard.isPresent()) {
        // throw new RuntimeException("This student already has a One Card ID.");
        // }
        Optional<IDCard> existingCard = idCardRepository.findByStudentId(student.getStudentId());
        if (existingCard.isPresent()) {
            throw new StudentIDAlreadyExistsException("This student already has a One Card ID.");
        }

        BufferedImage template = ImageIO.read(new File("C:\\Users\\HENOK\\Pictures\\Background2.png"));
        Graphics2D graphics = template.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set font and draw student details
        graphics.setFont(new Font("Arial", Font.PLAIN, 30));
        graphics.setColor(Color.BLACK);
        graphics.drawString(student.getFullname(), 480, 255);
        graphics.drawString(student.getStudentId(), 480, 350);
        graphics.drawString(student.getDepartement(), 480, 435);
        graphics.drawString(student.getGender(), 480, 520);
        graphics.drawString(student.getEntry_year(), 480, 610);

        // Load student photo
        BufferedImage studentPhoto = ImageIO.read(new File(student.getPicture()));
        graphics.drawImage(studentPhoto, 35, 230, 280, 300, null);

        // Generate QR code
        byte[] qrCodeBytes = codeGeneratorService.generateQRCode(student.getStudentId(), 200, 200);
        BufferedImage qrCodeImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        graphics.drawImage(qrCodeImage, 800, 450, null);

        // Generate barcode
        byte[] barcodeBytes = codeGeneratorService.generateBarcode(cardId, 250, 40);
        BufferedImage barcodeImage = ImageIO.read(new ByteArrayInputStream(barcodeBytes));
        graphics.drawImage(barcodeImage, 25, 550, null);

        graphics.setFont(new Font("Arial", Font.PLAIN, 18));
        graphics.setColor(Color.BLACK);
        graphics.drawString(cardId, 120, 610);

        graphics.dispose();

        // Save the generated card to disk

        String path = "C:\\Users\\HENOK\\Documents\\Project\\Spring boot projects\\Student_One_Card_System_Backend\\Generated ID Cards\\IDCard_"
                + cardId + ".png";
        ImageIO.write(template, "PNG", new File(path));

        // Save ID card metadata in the database
        IDCard idCard = new IDCard(cardId, student.getStudentId(), path, LocalDate.now());
        idCardRepository.save(idCard);

        return idCard;
    }
}
