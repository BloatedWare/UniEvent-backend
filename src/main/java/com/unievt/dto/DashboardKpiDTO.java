package com.unievt.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardKpiDTO {

    // ── Événements ──────────────────────────────────────────────────────
    private long totalEvenements;
    private Map<String, Long> evenementsParStatut;
    private Map<String, Long> evenementsParCategorie;

    // ── Inscriptions ─────────────────────────────────────────────────────
    private long totalInscriptions;
    private long inscriptionsPresentes;
    private long inscriptionsAbsentes;
    /** Pourcentage de présence (0-100) */
    private double tauxPresence;

    // ── Réservations ─────────────────────────────────────────────────────
    private long totalReservations;
    private Map<String, Long> reservationsParStatut;

    // ── Entités générales ────────────────────────────────────────────────
    private long totalClubs;
    private long totalUtilisateurs;
    private long totalPartenaires;
    private long partenairesActifs;

    // ── Évaluations ──────────────────────────────────────────────────────
    private long totalEvaluations;
    private double noteMoyenneGlobale;

    // ── Top 5 événements par inscriptions ───────────────────────────────
    private List<TopEvenementDTO> topEvenements;
}
