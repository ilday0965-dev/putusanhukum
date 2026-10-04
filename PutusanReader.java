package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PutusanReader {
    public static void main(String[] args) {
        // Nama file putusan Anda yang sudah ada di folder proyek
        String namaFile = "putusan_1012_pid.sus_2026_pn_sby_20260928093625.pdf";

        try (PDDocument document = Loader.loadPDF(new File(namaFile))) {

            // 1. TAHAP KONVERSI: PDF -> TEXT MENTAH (Sesuai bagan papan tulis)
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String teksPdf = pdfStripper.getText(document);

            System.out.println("--- HASIL EKSTRAKSI DATA PUTUSAN ---");

            // 2. TAHAP FILTERING: Menggunakan Regex untuk mencari data spesifik
            ekstrakNamaTerdakwa(teksPdf);
            ekstrakPasal(teksPdf);
            ekstrakIsiPutusan(teksPdf);

        } catch (IOException e) {
            System.err.println("Gagal memproses PDF: " + e.getMessage());
        }
    }

    // Metode untuk mencari Nama Terdakwa
    private static void ekstrakNamaTerdakwa(String teks) {
        // Logika Regex: Cari kata "Nama lengkap" sampai sebelum kata "Tempat lahir"
        String regex = "Nama lengkap[\\s\\|:]+(.*?)(?=Tempat lahir)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(teks);

        if (matcher.find()) {
            // Membersihkan karakter enter, spasi berlebih, dan tanda | atau ; yang terbawa dari PDF
            String nama = matcher.group(1).replaceAll("[\\r\\n|;]", " ").trim();
            nama = nama.replaceAll("\\s+", " ");
            System.out.println("Nama Terdakwa : " + nama);
        } else {
            System.out.println("Nama Terdakwa : Tidak ditemukan");
        }
    }

    // Metode untuk mencari Pasal yang didakwakan
    private static void ekstrakPasal(String teks) {
        // Logika Regex: Cari kalimat mulai dari "Mengingat Pasal" sampai kata "MENGADILI"
        String regex = "Mengingat\\s+(Pasal.*?)MENGADILI";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(teks);

        if (matcher.find()) {
            String pasal = matcher.group(1).replaceAll("[\\r\\n]", " ").trim();
            pasal = pasal.replaceAll("\\s+", " ");
            System.out.println("Pasal         : " + pasal);
        } else {
            System.out.println("Pasal         : Tidak ditemukan");
        }
    }

    // Metode untuk mencari Isi Hukuman / Pidana
    private static void ekstrakIsiPutusan(String teks) {
        // Logika Regex: Cari poin "2. Menjatuhkan pidana..." sampai poin "3. Menetapkan..." di bagian akhir
        String regex = "2\\.\\s*(Menjatuhkan pidana terhadap Terdakwa.*?)(?=3\\.\\s*Menetapkan)";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(teks);

        if (matcher.find()) {
            String putusan = matcher.group(1).replaceAll("[\\r\\n]", " ").trim();
            putusan = putusan.replaceAll("\\s+", " ");
            System.out.println("Isi Putusan   : " + putusan);
        } else {
            System.out.println("Isi Putusan   : Tidak ditemukan");
        }
    }
}