package com.unievt.service;

import com.unievt.dto.PartenaireCreateDTO;
import com.unievt.dto.PartenaireResponseDTO;
import com.unievt.dto.PartenaireUpdateDTO;
import com.unievt.entity.Partenaire;
import com.unievt.enums.TypePartenaireEnum;
import com.unievt.mapper.PartenaireMapper;
import com.unievt.repository.PartenaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PartenaireService {

    private final PartenaireRepository partenaireRepository;
    private final PartenaireMapper partenaireMapper;

    public PartenaireResponseDTO creer(PartenaireCreateDTO dto) {
        Partenaire entity = partenaireMapper.toEntity(dto);
        entity.setActif(true);
        return partenaireMapper.toResponseDTO(partenaireRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<PartenaireResponseDTO> lister() {
        return partenaireRepository.findAll().stream()
                .map(partenaireMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartenaireResponseDTO> listerActifs() {
        return partenaireRepository.findByActif(true).stream()
                .map(partenaireMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PartenaireResponseDTO> listerParType(TypePartenaireEnum type) {
        return partenaireRepository.findByTypePartenariat(type).stream()
                .map(partenaireMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PartenaireResponseDTO lire(Long id) {
        return partenaireMapper.toResponseDTO(getOrThrow(id));
    }

    public PartenaireResponseDTO modifier(Long id, PartenaireUpdateDTO dto) {
        Partenaire entity = getOrThrow(id);
        partenaireMapper.updateEntityFromDTO(dto, entity);
        return partenaireMapper.toResponseDTO(partenaireRepository.save(entity));
    }

    public PartenaireResponseDTO activer(Long id) {
        Partenaire entity = getOrThrow(id);
        entity.setActif(true);
        return partenaireMapper.toResponseDTO(partenaireRepository.save(entity));
    }

    public PartenaireResponseDTO desactiver(Long id) {
        Partenaire entity = getOrThrow(id);
        entity.setActif(false);
        return partenaireMapper.toResponseDTO(partenaireRepository.save(entity));
    }

    public void supprimer(Long id) {
        if (!partenaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partenaire introuvable: " + id);
        }
        partenaireRepository.deleteById(id);
    }

    private Partenaire getOrThrow(Long id) {
        return partenaireRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Partenaire introuvable: " + id));
    }
}
