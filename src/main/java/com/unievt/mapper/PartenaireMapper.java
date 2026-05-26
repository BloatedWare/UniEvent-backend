package com.unievt.mapper;

import com.unievt.dto.PartenaireCreateDTO;
import com.unievt.dto.PartenaireResponseDTO;
import com.unievt.dto.PartenaireUpdateDTO;
import com.unievt.entity.Partenaire;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PartenaireMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    Partenaire toEntity(PartenaireCreateDTO dto);

    PartenaireResponseDTO toResponseDTO(Partenaire entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actif", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    void updateEntityFromDTO(PartenaireUpdateDTO dto, @MappingTarget Partenaire entity);
}
