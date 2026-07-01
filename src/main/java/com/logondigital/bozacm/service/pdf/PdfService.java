package com.logondigital.bozacm.service.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.logondigital.bozacm.DTO.billet.BilletResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

/**
 * ============================================
 * SERVICE DE GÉNÉRATION DE PDF
 * ============================================

 * Génère un PDF professionnel avec toutes les
 * informations du billet, du client et de la réservation.

 * NOUVEAU : Logo en filigrane pour l'authenticité
 */
@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Couleurs BozaCM
    private static final DeviceRgb BLEU_FONCE = new DeviceRgb(27, 73, 101);      // #1B4965
    private static final DeviceRgb BLEU_CLAIR = new DeviceRgb(95, 168, 184);     // #5FA8B8
    private static final DeviceRgb ROUGE_ORANGE = new DeviceRgb(231, 76, 60);    // #E74C3C

    @Value("${app.qrcode.upload-dir:uploads/qrcodes}")
    private String qrcodeUploadDir;

    /**
     * Génère un PDF pour un billet avec logo en filigrane ET QR CODE.
     *
     * @param billet Les infos complètes du billet
     * @return Le PDF en bytes (prêt à télécharger)
     */
    public byte[] generateBilletPdf(BilletResponseDTO billet) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // ========== AJOUTER LE LOGO EN FILIGRANE ==========
        addWatermark(pdf);

        // ========== LOGO EN HAUT ==========
        try {
            ClassPathResource logoResource = new ClassPathResource("static/images/logo-bozacm.png");
            InputStream logoStream = logoResource.getInputStream();
            byte[] logoBytes = logoStream.readAllBytes();

            Image logo = new Image(ImageDataFactory.create(logoBytes));
            logo.setWidth(200);
            logo.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
            logo.setMarginBottom(15);
            document.add(logo);

            System.out.println("✅ Logo ajouté en haut du PDF");
        } catch (Exception e) {
            System.err.println("⚠️ Logo introuvable : " + e.getMessage());
            e.printStackTrace();
        }

        // ========== TITRE ==========
        Paragraph titre = new Paragraph("BILLET DE VOYAGE")
                .setFontSize(26)
                .setBold()
                .setFontColor(BLEU_FONCE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(titre);

        Paragraph sousTitre = new Paragraph("Découvrez le Cameroun en 2 clics")
                .setFontSize(12)
                .setFontColor(BLEU_CLAIR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(sousTitre);

        // ========== INFOS BILLET ==========
        document.add(createSectionTitle("INFORMATIONS DU BILLET"));

        Table billetTable = new Table(2);
        billetTable.setWidth(UnitValue.createPercentValue(100));

        addRow(billetTable, "Numéro de billet", billet.getNumeroBillet());
        addRow(billetTable, "Statut", billet.getStatutBillet().toString());
        addRow(billetTable, "Date d'émission",
                billet.getDateEmission() != null ?
                        billet.getDateEmission().format(DATE_FORMATTER) : "N/A");
        addRow(billetTable, "Date d'expiration",
                billet.getDateExpiration() != null ?
                        billet.getDateExpiration().format(DATE_FORMATTER) : "N/A");

        document.add(billetTable);
        document.add(new Paragraph("\n"));

        // ========== INFOS CLIENT ==========
        document.add(createSectionTitle("INFORMATIONS DU PASSAGER"));

        Table clientTable = new Table(2);
        clientTable.setWidth(UnitValue.createPercentValue(100));

        addRow(clientTable, "Nom complet", billet.getClientNomComplet());
        addRow(clientTable, "Email", billet.getClientEmail());
        addRow(clientTable, "Téléphone", billet.getClientTelephone());

        document.add(clientTable);
        document.add(new Paragraph("\n"));

        // ========== INFOS RÉSERVATION ==========
        document.add(createSectionTitle("INFORMATIONS DU VOYAGE"));

        Table voyageTable = new Table(2);
        voyageTable.setWidth(UnitValue.createPercentValue(100));

        addRow(voyageTable, "Ville de départ", billet.getVilleDeDepart());
        addRow(voyageTable, "Ville d'arrivée", billet.getVilleArrivee());
        addRow(voyageTable, "Date de départ",
                billet.getDateDepart() != null ?
                        billet.getDateDepart().format(DATE_FORMATTER) : "N/A");
        addRow(voyageTable, "Prix", billet.getPrixReservation() + " FCFA");

        // Infos spécifiques selon le type
        if (billet.getCompagnieBus() != null) {
            addRow(voyageTable, "Type de transport", "BUS");
            addRow(voyageTable, "Compagnie", billet.getCompagnieBus());
            addRow(voyageTable, "Type de bus",
                    billet.getTypeBus() != null ? billet.getTypeBus().name() : "N/A");  // ← .name() au lieu de .toString()
            addRow(voyageTable, "Climatisation",
                    Boolean.TRUE.equals(billet.getClimatisation()) ? "Oui" : "Non");  // ← Boolean.TRUE.equals()
        } else if (billet.getCompagnieAerienne() != null) {
            addRow(voyageTable, "Type de transport", "AVION");
            addRow(voyageTable, "Compagnie", billet.getCompagnieAerienne());
            addRow(voyageTable, "Numéro de vol", billet.getNumeroVol());
            addRow(voyageTable, "Classe",
                    billet.getClasseAvion() != null ? billet.getClasseAvion().name() : "N/A");
            addRow(voyageTable, "Bagages", billet.getPoidsMaxBagages() + " kg");
        } else if (billet.getCompagnieTrain() != null) {
            addRow(voyageTable, "Type de transport", "TRAIN");
            addRow(voyageTable, "Compagnie", billet.getCompagnieTrain());
            addRow(voyageTable, "Wagon", billet.getNumeroWagon());
            addRow(voyageTable, "Classe",
                    billet.getClasseTrain() != null ? billet.getClasseTrain().name() : "N/A");
        }

        document.add(voyageTable);
        document.add(new Paragraph("\n"));

        // ========== QR CODE ==========
        if (billet.getQrcodeUrl() != null && !billet.getQrcodeUrl().isEmpty()) {
            try {
                System.out.println("🔲 Ajout du QR Code dans le PDF...");

                // Extraire uniquement le nom du fichier depuis l'URL (peu importe le domaine/IP utilisé)
                // Ex: "http://172.20.10.7:8080/qrcodes/BZC-xxx.png" ou "http://localhost:8080/qrcodes/BZC-xxx.png"
                //     -> "BZC-xxx.png"
                String qrcodeUrl = billet.getQrcodeUrl();
                String fileName = qrcodeUrl.substring(qrcodeUrl.lastIndexOf('/') + 1);
                String qrcodeFilePath = qrcodeUploadDir + "/" + fileName;

                java.io.File qrcodeFile = new java.io.File(qrcodeFilePath);

                if (qrcodeFile.exists()) {
                    // Titre de la section QR Code
                    Paragraph qrcodeTitle = new Paragraph("🔲 VOTRE QR CODE")
                            .setFontSize(14)
                            .setBold()
                            .setBackgroundColor(BLEU_CLAIR)
                            .setFontColor(ColorConstants.WHITE)
                            .setPadding(8)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setMarginTop(10)
                            .setMarginBottom(10);
                    document.add(qrcodeTitle);

                    // Créer l'image du QR Code
                    Image qrcodeImage = new Image(ImageDataFactory.create(qrcodeFile.getAbsolutePath()));

                    // Redimensionner et centrer
                    qrcodeImage.scaleToFit(150, 150);
                    qrcodeImage.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
                    qrcodeImage.setMarginTop(10);
                    qrcodeImage.setMarginBottom(10);

                    document.add(qrcodeImage);

                    // Instruction
                    Paragraph qrcodeInstruction = new Paragraph("Présentez ce code à l'embarquement")
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontColor(BLEU_FONCE)
                            .setItalic()
                            .setMarginBottom(20);
                    document.add(qrcodeInstruction);

                    System.out.println("✅ QR Code ajouté au PDF avec succès");
                } else {
                    System.err.println("⚠️ Fichier QR Code introuvable : " + qrcodeFilePath);
                }
            } catch (Exception e) {
                System.err.println("❌ Erreur lors de l'ajout du QR Code : " + e.getMessage());
                e.printStackTrace();
            }
        }

        // ========== FOOTER ==========
        document.add(new Paragraph("\n"));

        Paragraph avertissement = new Paragraph("⚠️ INSTRUCTIONS IMPORTANTES")
                .setFontSize(12)
                .setBold()
                .setFontColor(ROUGE_ORANGE)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(avertissement);

        Paragraph footer = new Paragraph(
                "Ce billet est valide uniquement avec une pièce d'identité.\n" +
                        "Veuillez vous présenter 30 minutes avant le départ.\n" +
                        "Ce billet est personnel et non transférable.\n\n" +
                        "BozaCM - Découvrez le Cameroun en 2 clics\n" +
                        "📧 contact@bozacm.com | 📱 +237 6XX XX XX XX"
        )
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic()
                .setFontColor(BLEU_FONCE);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    /**
     * Ajoute un logo en filigrane (watermark) sur toutes les pages.

     * POUR L'AUTHENTICITÉ !
     */
    private void addWatermark(PdfDocument pdfDocument) {
        try {
            // ✅ CORRECTION: Utiliser ClassPathResource
            ClassPathResource logoResource = new ClassPathResource("static/images/logo-bozacm.png");
            InputStream logoStream = logoResource.getInputStream();
            byte[] logoBytes = logoStream.readAllBytes();

            int numberOfPages = pdfDocument.getNumberOfPages();

            for (int i = 1; i <= numberOfPages; i++) {
                PdfCanvas canvas = new PdfCanvas(
                        pdfDocument.getPage(i).newContentStreamBefore(),
                        pdfDocument.getPage(i).getResources(),
                        pdfDocument
                );

                // Créer l'image
                Image logo = new Image(ImageDataFactory.create(logoBytes));

                // Position au centre de la page
                float pageWidth = pdfDocument.getPage(i).getPageSize().getWidth();
                float pageHeight = pdfDocument.getPage(i).getPageSize().getHeight();

                // Taille du logo (plus grand pour le filigrane)
                float logoWidth = 400;
                float logoHeight = 200;

                logo.setFixedPosition(
                        (pageWidth - logoWidth) / 2,  // Centré horizontalement
                        (pageHeight - logoHeight) / 2  // Centré verticalement
                );
                logo.setWidth(logoWidth);
                logo.setHeight(logoHeight);

                // Rendre transparent (opacité à 8%)
                PdfExtGState gs1 = new PdfExtGState();
                gs1.setFillOpacity(0.08f);  // 8% d'opacité
                canvas.setExtGState(gs1);

                // Ajouter le logo en rotation (optionnel)
                logo.setRotationAngle(Math.toRadians(45));  // 45 degrés

                // Dessiner le logo
                com.itextpdf.layout.Canvas watermarkCanvas =
                        new com.itextpdf.layout.Canvas(canvas, pdfDocument.getDefaultPageSize());
                watermarkCanvas.add(logo);
                watermarkCanvas.close();
            }

            System.out.println("✅ Logo en filigrane ajouté pour l'authenticité");

        } catch (Exception e) {
            System.err.println("⚠️ Impossible d'ajouter le filigrane : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Crée un titre de section stylisé avec les couleurs BozaCM.
     */
    private Paragraph createSectionTitle(String text) {
        return new Paragraph(text)
                .setFontSize(14)
                .setBold()
                .setBackgroundColor(BLEU_CLAIR)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setMarginTop(10)
                .setMarginBottom(10);
    }

    /**
     * Ajoute une ligne dans un tableau (label + valeur).
     */
    private void addRow(Table table, String label, String value) {
        Cell labelCell = new Cell().add(new Paragraph(label).setBold().setFontColor(BLEU_FONCE));
        labelCell.setBackgroundColor(new DeviceRgb(248, 249, 250));

        Cell valueCell = new Cell().add(new Paragraph(value != null ? value : "N/A"));

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}