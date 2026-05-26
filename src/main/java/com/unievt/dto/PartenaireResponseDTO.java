package com.unievt.dto;

import com.unievt.enums.TypePartenaireEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartenaireResponseDTO {

    private Long id;
    private String nom;
    private String description;
    private String logo;
    private String siteWeb;
    private String contactNom;
    private String contactEmail;
    private String contactTelephone;
    private TypePartenaireEnum typePartenariat;
    private Boolean actif;
    private LocalDateTime dateCreation;
}
