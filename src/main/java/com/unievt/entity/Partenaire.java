package com.unievt.entity;

import com.unievt.enums.TypePartenaireEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "partenaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "logo")
    private String logo;

    @Column(name = "site_web")
    private String siteWeb;

    @Column(name = "contact_nom")
    private String contactNom;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_telephone")
    private String contactTelephone;

    @Column(name = "type_partenariat")
    @Enumerated(EnumType.STRING)
    private TypePartenaireEnum typePartenariat;

    @Column(name = "actif")
    @Builder.Default
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;
}
