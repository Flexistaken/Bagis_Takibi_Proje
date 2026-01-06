package com.example.bagis_takibi_proje;

import com.example.proje_bagis_takibi.model.Bagis;
import com.example.proje_bagis_takibi.model.Kurum;
import com.example.proje_bagis_takibi.service.BagisService;
import com.example.proje_bagis_takibi.service.KurumService;
import com.example.proje_bagis_takibi.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Side;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class RaporlarController {

    @FXML
    private Label toplamBagisLabel;
    @FXML
    private Label bugunBagisLabel;
    @FXML
    private Label kurumSayisiLabel;

    @FXML
    private PieChart kurumBagisChart;
    @FXML
    private BarChart<String, Number> liderBagisciChart;

    private final BagisService bagisService = new BagisService();
    private final KurumService kurumService = new KurumService();

    @FXML
    public void initialize() {
        ozetleriDoldur();
        kurumBagisDagilimi();
        liderBagiscilariGetir();
    }

    /* ================= ÖZET ================= */
    private void ozetleriDoldur() {

        double toplam = bagisService.tumBagislariGetir()
                .stream().mapToDouble(Bagis::getMiktar).sum();

        double bugun = bagisService.tumBagislariGetir()
                .stream()
                .filter(b -> b.getTarih().equals(LocalDate.now()))
                .mapToDouble(Bagis::getMiktar).sum();

        toplamBagisLabel.setText(String.format("%,.0f ₺", toplam));
        bugunBagisLabel.setText(String.format("%,.0f ₺", bugun));
        kurumSayisiLabel.setText(String.valueOf(kurumService.kurumlariGetir().size()));
    }

    /* ================= PIE CHART ================= */
    private void kurumBagisDagilimi() {

        Map<Integer, String> kurumMap = new HashMap<>();
        for (Kurum k : kurumService.kurumlariGetir()) {
            kurumMap.put(k.getId(), k.getAd());
        }

        Map<String, Double> toplamlar = new LinkedHashMap<>();

        for (Bagis b : bagisService.tumBagislariGetir()) {
            String kurumAdi = kurumMap.getOrDefault(b.getKurumId(), "Bilinmeyen");
            toplamlar.put(
                    kurumAdi,
                    toplamlar.getOrDefault(kurumAdi, 0.0) + b.getMiktar()
            );
        }

        //  ÇOK ÖNEMLİ: Önce temizle
        kurumBagisChart.getData().clear();

        //  PieChart verilerini burada ekliyoruz
        for (var e : toplamlar.entrySet()) {

            PieChart.Data data =
                    new PieChart.Data(e.getKey(), e.getValue());

            kurumBagisChart.getData().add(data);
        }

        //  GÖRÜNÜRLÜK AYARLARI
        kurumBagisChart.setLegendVisible(true);     // ← kurum isimleri BURADA
        kurumBagisChart.setLabelsVisible(false);    // ← dilim üstü yazıyı kapat

        // legend için yer aç
        kurumBagisChart.setMinHeight(360);
        kurumBagisChart.setPrefHeight(360);
        kurumBagisChart.setMinWidth(420);
        kurumBagisChart.setPrefWidth(420);

        //  Hover olunca bilgi göster
        for (PieChart.Data d : kurumBagisChart.getData()) {
            Tooltip.install(
                    d.getNode(),
                    new Tooltip(
                            d.getName() + "\n" +
                                    String.format("%,.0f ₺", d.getPieValue())
                    )
            );
        }
    }


    /* ================= BAR CHART ================= */
    private void liderBagiscilariGetir() {

        Map<Integer, String> bagisciMap = new HashMap<>();
        for (var k : FileUtil.kullanicilariOku("data/kullanicilar.txt")) {
            if (k instanceof com.example.proje_bagis_takibi.model.Bagisci b) {
                bagisciMap.put(b.getId(), b.getAd());
            }
        }

        Map<String, Double> toplamlar = new HashMap<>();

        for (Bagis b : bagisService.tumBagislariGetir()) {
            String ad = bagisciMap.getOrDefault(b.getBagisciId(), "Bilinmeyen");
            toplamlar.put(ad, toplamlar.getOrDefault(ad, 0.0) + b.getMiktar());
        }

        var top5 = toplamlar.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .toList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (var e : top5) {
            series.getData().add(new XYChart.Data<>(e.getKey(), e.getValue()));
        }

        liderBagisciChart.getData().clear();
        liderBagisciChart.getData().add(series);
        liderBagisciChart.setLegendVisible(false);
    }

    /* ================= BUTTONS ================= */
    @FXML
    private void kapat() {
        ((Stage) toplamBagisLabel.getScene().getWindow()).close();
    }

    @FXML
    private void pdfExport() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("PDF Kaydet");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        var file = chooser.showSaveDialog(toplamBagisLabel.getScene().getWindow());
        if (file == null) return;

        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            // Font Tanımlamaları
            BaseFont bfNormal = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            BaseFont bfBold = BaseFont.createFont("fonts/arialbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font titleFont = new Font(bfBold, 20, Font.BOLD, BaseColor.DARK_GRAY);
            Font headerFont = new Font(bfBold, 12, Font.NORMAL, BaseColor.WHITE);
            Font textFont = new Font(bfNormal, 11);
            Font dateFont = new Font(bfNormal, 10, Font.ITALIC);

            // 1. Üst Bilgi (Başlık ve Tarih)
            Paragraph title = new Paragraph("BAĞIŞ TAKİP SİSTEMİ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);

            String currentDateTime = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
            Paragraph date = new Paragraph("Rapor Tarihi: " + currentDateTime, dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            doc.add(date);

            doc.add(new Paragraph("\n")); // Boşluk

            // 2. Özet Bilgi Alanı
            PdfPTable summaryTable = new PdfPTable(1);
            summaryTable.setWidthPercentage(100);
            PdfPCell summaryCell = new PdfPCell(new Phrase("Genel İstatistikler", headerFont));
            summaryCell.setBackgroundColor(new BaseColor(51, 122, 183)); // Mavi tonu
            summaryCell.setPadding(5);
            summaryTable.addCell(summaryCell);
            doc.add(summaryTable);

            doc.add(new Paragraph("Toplam Biriken Bağış: " + toplamBagisLabel.getText(), textFont));
            doc.add(new Paragraph("Sistemdeki Toplam Kurum: " + kurumSayisiLabel.getText(), textFont));
            doc.add(new Paragraph("\n"));

            // 3. Kurum Dağılım Tablosu (2 Kolonlu)
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setWidths(new float[]{3f, 1f}); // Kolon genişlik oranları

            // Tablo Başlıkları
            PdfPCell h1 = new PdfPCell(new Phrase("Kurum Adı", headerFont));
            h1.setBackgroundColor(BaseColor.GRAY);
            h1.setPadding(8);
            table.addCell(h1);

            PdfPCell h2 = new PdfPCell(new Phrase("Miktar (TL)", headerFont));
            h2.setBackgroundColor(BaseColor.GRAY);
            h2.setPadding(8);
            table.addCell(h2);

            // Verileri Tabloya Ekleme
            for (PieChart.Data d : kurumBagisChart.getData()) {
                table.addCell(new PdfPCell(new Phrase(d.getName(), textFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.0f TL", d.getPieValue()), textFont)));
            }

            doc.add(table);
            doc.close();

            new Alert(Alert.AlertType.INFORMATION, "Rapor başarıyla oluşturuldu.").showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Hata: " + e.getMessage()).showAndWait();
        }
    }
}