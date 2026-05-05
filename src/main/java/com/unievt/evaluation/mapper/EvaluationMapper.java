package com.unievt.evaluation.mapper;

import com.unievt.evaluation.dto.EvaluationDTO;
import com.unievt.evaluation.dto.EvaluationResponseDTO;
import com.unievt.evaluation.entity.Evaluation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EvaluationMapper {

    @Mapping(target = "etudiant", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateEvaluation", ignore = true)
    Evaluation toEntity(EvaluationDTO dto);

    @Mapping(source = "etudiant.id", target = "etudiantId")
    @Mapping(source = "etudiant.nom", target = "etudiantNom")
    @Mapping(source = "etudiant.prenom", target = "etudiantPrenom")
    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "reservation.evenement.id", target = "evenementId")
    @Mapping(source = "reservation.evenement.titre", target = "evenementTitre")
    EvaluationResponseDTO toResponseDTO(Evaluation entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "etudiant", ignore = true)
    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateEvaluation", ignore = true)
    void updateEntityFromDTO(EvaluationDTO dto, @MappingTarget Evaluation entity);
}
