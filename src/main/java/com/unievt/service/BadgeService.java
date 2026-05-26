package com.unievt.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.unievt.dto.BadgeResponseDTO;
import com.unievt.entity.Inscription;
import com.unievt.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BadgeService {

    private static final int QR_SIZE = 300;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final InscriptionRepository inscriptionRepository;

    public byte[] genererQrCodePng(Long inscriptionId) {
        Inscription inscription = getOrThrow(inscriptionId);
        String contenu = buildQrContent(inscription);
        return generatePng(contenu);
    }

    public BadgeResponseDTO genererBadge(Long inscriptionId) {
        Inscription inscription = getOrThrow(inscriptionId);
        String contenu = buildQrContent(inscription);
        byte[] png = generatePng(contenu);
        String base64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(png);

        return BadgeResponseDTO.builder()
                .inscriptionId(inscription.getId())
                .nomEtudiant(inscription.getEtudiant().getNom())
                .prenomEtudiant(inscription.getEtudiant().getPrenom())
                .emailEtudiant(inscription.getEtudiant().getEmail())
                .titreEvenement(inscription.getEvenement().getTitre())
                .dateEvenement(inscription.getEvenement().getDateDebut() != null
                        ? inscription.getEvenement().getDateDebut().format(FMT) : "")
                .statut(inscription.getStatut() != null ? inscription.getStatut().name() : "")
                .qrCodeBase64(base64)
                .build();
    }

    private String buildQrContent(Inscription inscription) {
        return String.format("INS-%d|%s %s|%s|%s",
                inscription.getId(),
                inscription.getEtudiant().getNom(),
                inscription.getEtudiant().getPrenom(),
                inscription.getEvenement().getTitre(),
                inscription.getDateInscription() != null
                        ? inscription.getDateInscription().format(FMT) : "");
    }

    private byte[] generatePng(String contenu) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(contenu, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE,
                    Map.of(EncodeHintType.CHARACTER_SET, "UTF-8"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        } catch (WriterException | IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur génération QR code: " + e.getMessage());
        }
    }

    private Inscription getOrThrow(Long id) {
        return inscriptionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Inscription introuvable: " + id));
    }
}
