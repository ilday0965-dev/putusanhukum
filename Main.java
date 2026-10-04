package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // 1. Membuat objek dokumen PDF baru
        try (PDDocument document = new PDDocument()) {

            // 2. Membuat objek halaman kosong baru dan menambahkannya ke dokumen
            PDPage page = new PDPage();
            document.addPage(page);

            // 3. Membuka aliran untuk menulis teks atau gambar ke halaman tersebut
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();

                // Menentukan jenis huruf (font) dan ukurannya
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

                // Menentukan koordinat awal posisi teks (X, Y)
                contentStream.newLineAtOffset(100, 700);

                // Menuliskan teks ke dalam PDF
                contentStream.showText("Halo! Ini adalah tugas membuat PDF dengan Java.");

                contentStream.endText();
            }

            // 4. Menyimpan dokumen ke dalam bentuk file fisik
            document.save(new File("TugasPBO.pdf"));
            System.out.println("Berhasil! File PDF telah dibuat.");

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat membuat PDF: " + e.getMessage());
        }
    }
}