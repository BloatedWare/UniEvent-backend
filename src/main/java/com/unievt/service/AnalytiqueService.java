package com.unievt.service;

import com.unievt.dto.DashboardKpiDTO;
import com.unievt.dto.TopEvenementDTO;
import com.unievt.enums.StatutReservationEnum;
import com.unievt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalytiqueService {

    private final EvenementRepository evenementRepository;
    private final InscriptionRepository inscriptionRepository;
    private final ReservationRepository reservationRepository;
    private final EvaluationRepository evaluationRepository;
    private final ClubRepository clubRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PartenaireRepository partenaireRepository;

    public DashboardKpiDTO getDashboard() {
        // ── Événements ─────────────────────────────────────────────────
        long totalEvenements = evenementRepository.count();
        Map<String, Long> evenementsParStatut = toMap(evenementRepository.countByStatut());
        Map<String, Long> evenementsParCategorie = toMap(evenementRepository.countByCategorie());

        // ── Inscriptions ───────────────────────────────────────────────
        long totalInscriptions = inscriptionRepository.count();
        long presents = inscriptionRepository.countByPresent(true);
        long absents = inscriptionRepository.countByPresent(false);
        double tauxPresence = totalInscriptions > 0
                ? Math.round((presents * 100.0 / totalInscriptions) * 10.0) / 10.0
                : 0.0;

        // ── Réservations ───────────────────────────────────────────────
        long totalReservations = reservationRepository.count();
        Map<String, Long> reservationsParStatut = new LinkedHashMap<>();
        for (StatutReservationEnum s : StatutReservationEnum.values()) {
            reservationsParStatut.put(s.name(), (long) reservationRepository.findByStatut(s).size());
        }

        // ── Entités générales ──────────────────────────────────────────
        long totalClubs = clubRepository.count();
        long totalUtilisateurs = utilisateurRepository.count();
        long totalPartenaires = partenaireRepository.count();
        long partenairesActifs = partenaireRepository.findByActif(true).size();

        // ── Évaluations ────────────────────────────────────────────────
        long totalEvaluations = evaluationRepository.count();
        Double avgNote = evaluationRepository.averageNote();
        double noteMoyenne = avgNote != null
                ? Math.round(avgNote * 10.0) / 10.0
                : 0.0;

        // ── Top 5 événements ───────────────────────────────────────────
        List<TopEvenementDTO> top = inscriptionRepository
                .topEvenementsByInscriptions(PageRequest.of(0, 5))
                .stream()
                .map(row -> TopEvenementDTO.builder()
                        .evenementId(((Number) row[0]).longValue())
                        .titre((String) row[1])
                        .nbInscriptions(((Number) row[2]).longValue())
                        .build())
                .toList();

        return DashboardKpiDTO.builder()
                .totalEvenements(totalEvenements)
                .evenementsParStatut(evenementsParStatut)
                .evenementsParCategorie(evenementsParCategorie)
                .totalInscriptions(totalInscriptions)
                .inscriptionsPresentes(presents)
                .inscriptionsAbsentes(absents)
                .tauxPresence(tauxPresence)
                .totalReservations(totalReservations)
                .reservationsParStatut(reservationsParStatut)
                .totalClubs(totalClubs)
                .totalUtilisateurs(totalUtilisateurs)
                .totalPartenaires(totalPartenaires)
                .partenairesActifs(partenairesActifs)
                .totalEvaluations(totalEvaluations)
                .noteMoyenneGlobale(noteMoyenne)
                .topEvenements(top)
                .build();
    }

    private Map<String, Long> toMap(List<Object[]> rows) {
        Map<String, Long> map = new LinkedHashMap<>();
        for (Object[] row : rows) {
            map.put(row[0].toString(), ((Number) row[1]).longValue());
        }
        return map;
    }
}
