package com.unievt.dto;

import com.unievt.enums.TypePartenaireEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartenaireUpdateDTO {

    private String nom;
    private String description;
    private String logo;
    private String siteWeb;
    private String contactNom;
    private String contactEmail;
    private String contactTelephone;
    private TypePartenaireEnum typePartenariat;
}
