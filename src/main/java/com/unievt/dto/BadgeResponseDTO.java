package com.unievt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponseDTO {

    private Long inscriptionId;
    private String nomEtudiant;
    private String prenomEtudiant;
    private String emailEtudiant;
    private String titreEvenement;
    private String dateEvenement;
    private String statut;
    /** PNG du QR code encodé en Base64 (data:image/png;base64,...) */
    private String qrCodeBase64;
}
