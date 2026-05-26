package com.unievt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopEvenementDTO {
    private Long evenementId;
    private String titre;
    private Long nbInscriptions;
}
