package com.unievt.notification.mapper;

import com.unievt.notification.dto.NotificationDTO;
import com.unievt.notification.dto.NotificationResponseDTO;
import com.unievt.notification.entity.Notification;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "evenement", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lu", ignore = true)
    @Mapping(target = "dateEnvoi", ignore = true)
    Notification toEntity(NotificationDTO dto);

    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "destinataire.prenom", target = "destinatairePrenom")
    @Mapping(source = "evenement.id", target = "evenementId")
    @Mapping(source = "evenement.titre", target = "evenementTitre")
    NotificationResponseDTO toResponseDTO(Notification entity);
}
