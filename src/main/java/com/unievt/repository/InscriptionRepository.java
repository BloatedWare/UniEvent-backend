package com.unievt.repository;

import com.unievt.entity.Inscription;
import com.unievt.enums.StatutInscriptionEnum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByEvenementId(Long evenementId);
    List<Inscription> findByEtudiantId(Long etudiantId);
    List<Inscription> findByEvenementIdAndStatut(Long evenementId, StatutInscriptionEnum statut);

    long countByPresent(Boolean present);

    @Query("SELECT i.evenement.id, i.evenement.titre, COUNT(i) " +
           "FROM Inscription i GROUP BY i.evenement.id, i.evenement.titre ORDER BY COUNT(i) DESC")
    List<Object[]> topEvenementsByInscriptions(Pageable pageable);
}
