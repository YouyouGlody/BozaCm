package com.logondigital.bozacm.service.email;

import com.logondigital.bozacm.dto.billet.BilletResponseDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.format.DateTimeFormatter;

/**
 * ============================================
 * SERVICE D'ENVOI D'EMAILS AVEC PDF + QR CODE
 * ============================================
 * Auteur: BozaCM Team
 * Date: Décembre 2025
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:BozaCM}")
    private String appName;

    @Value("${app.support.email:support@bozacm.cm}")
    private String supportEmail;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    /**
     * Envoie le billet par email avec PDF et QR code
     */
    public void envoyerBilletParEmail(
            BilletResponseDTO billet,
            String emailClient,
            byte[] pdfBytes,
            String qrcodeUrl
    ) throws MessagingException {

        logger.info("📧 Envoi email à {} pour billet {}", emailClient, billet.getNumeroBillet());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(emailClient);
        helper.setSubject("🎫 Votre billet " + appName + " - " + billet.getNumeroBillet());
        helper.setText(construireEmail(billet, qrcodeUrl), true);

        // Attacher le PDF
        helper.addAttachment("Billet_" + billet.getNumeroBillet() + ".pdf",
                new ByteArrayResource(pdfBytes));

        // Envoyer
        mailSender.send(message);
        logger.info("✅ Email envoyé à {}", emailClient);
    }

    /**
     * Construction du HTML de l'email
     */
    private String construireEmail(BilletResponseDTO billet, String qrcodeUrl) {
        String dateDepart = billet.getDateDepart() != null ?
                billet.getDateDepart().format(DATE_FORMATTER) : "N/A";

        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'>" +
                "<style>" +
                "body{font-family:Arial;max-width:600px;margin:0 auto;background:#f4f4f4;padding:20px}" +
                ".container{background:white;border-radius:10px;overflow:hidden;box-shadow:0 4px 6px rgba(0,0,0,0.1)}" +
                ".header{background:linear-gradient(135deg,#228B22 0%,#1a7016 100%);color:white;padding:30px;text-align:center}" +
                ".content{padding:30px}" +
                ".section{margin:20px 0}" +
                ".info{background:#f8f9fa;padding:15px;border-radius:5px;margin:10px 0}" +
                ".qrcode{text-align:center;padding:20px;background:#f8f9fa;border-radius:8px;margin:20px 0}" +
                ".warning{background:#fff3cd;border-left:4px solid #ffc107;padding:15px;margin:20px 0}" +
                ".footer{background:#1B4965;color:white;padding:20px;text-align:center}" +
                "</style></head><body>" +
                "<div class='container'>" +
                "<div class='header'><h1>🚌 " + appName + "</h1><p>Découvrez le Cameroun en 2 clics</p></div>" +
                "<div class='content'>" +
                "<p>Bonjour <strong>" + billet.getClientNomComplet() + "</strong>,</p>" +
                "<p>Merci d'avoir choisi " + appName + " ! Votre réservation est confirmée. 🎉</p>" +
                "<div class='section'>" +
                "<h3>📄 Informations du billet</h3>" +
                "<div class='info'>" +
                "<strong>Numéro:</strong> " + billet.getNumeroBillet() + "<br>" +
                "<strong>Statut:</strong> " + billet.getStatutBillet() + "<br>" +
                "<strong>Trajet:</strong> " + billet.getVilleDeDepart() + " → " + billet.getVilleArrivee() + "<br>" +
                "<strong>Date:</strong> " + dateDepart + "<br>" +
                "<strong>Prix:</strong> " + billet.getPrixReservation() + " FCFA" +
                "</div></div>" +
                (qrcodeUrl != null && !qrcodeUrl.isEmpty() ?
                        "<div class='qrcode'>" +
                                "<h3>🔲 Votre QR Code</h3>" +
                                "<p>Présentez ce code à l'embarquement</p>" +
                                "<img src='" + qrcodeUrl + "' alt='QR Code' style='max-width:200px'/>" +
                                "</div>" : "") +
                "<div class='warning'>" +
                "<h4>⚠️ Instructions importantes</h4>" +
                "<ul><li>Présentez-vous 30 minutes avant le départ</li>" +
                "<li>Munissez-vous d'une pièce d'identité valide</li>" +
                "<li>Le QR code sera scanné à l'embarquement</li></ul>" +
                "</div>" +
                "<p style='text-align:center'>Bon voyage avec " + appName + " ! 🚌✨</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p><strong>" + appName + "</strong></p>" +
                "<p>📧 " + supportEmail + " | 📱 +237 6XX XX XX XX</p>" +
                "</div></div></body></html>";
    }

    /**
     * Méthode future pour SMS (à implémenter)
     */
    public void envoyerBilletParSMS(BilletResponseDTO billet, String numero, String lien) {
        logger.warn("📱 SMS non implémenté - Prévu pour: {}", numero);
        // TODO: Implémenter avec Twilio ou API SMS locale
    }
}