package com.socs.oneCardIDManagement.temp;

import com.google.zxing.BarcodeFormat;
// import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

@Service
public class PDFService {

    public void generateStudentIDCard(Student student) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(student.getFullName() + "_IDCard.pdf"));
        document.open();

        // Add student information
        document.add(new com.itextpdf.text.Paragraph("Name: " + student.getFullName()));
        document.add(new com.itextpdf.text.Paragraph("ID: " + student.getIdNumber()));
        document.add(new com.itextpdf.text.Paragraph("Gender: " + student.getGender()));
        document.add(new com.itextpdf.text.Paragraph("Department: " + student.getDepartment()));
        document.add(new com.itextpdf.text.Paragraph("Entry Year: " + student.getEntryYear()));

        // Add QR code
        Image qrCode = generateQRCode(student.getIdNumber());
        document.add(qrCode);

        // Add student photo
        if (student.getStudentPhoto() != null) {
            Image studentPhoto = Image.getInstance(student.getStudentPhoto());
            studentPhoto.scaleAbsolute(100, 100);
            document.add(studentPhoto);
        }

        document.close();
    }

    private Image generateQRCode(String idNumber) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(idNumber, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
        return Image.getInstance(baos.toByteArray());
    }
}
