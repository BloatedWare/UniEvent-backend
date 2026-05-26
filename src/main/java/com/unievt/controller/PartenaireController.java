package com.unievt.controller;

import com.unievt.dto.PartenaireCreateDTO;
import com.unievt.dto.PartenaireResponseDTO;
import com.unievt.dto.PartenaireUpdateDTO;
import com.unievt.enums.TypePartenaireEnum;
import com.unievt.service.PartenaireService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartenaireController {

    private final PartenaireService partenaireService;

    // POST /partenaires — créer un partenaire
    @PostMapping("/partenaires")
    public ResponseEntity<PartenaireResponseDTO> creer(@Valid @RequestBody PartenaireCreateDTO dto) {
        PartenaireResponseDTO created = partenaireService.creer(dto);
        return ResponseEntity
                .created(URI.create("/partenaires/" + created.getId()))
                .body(created);
    }

    // GET /partenaires — lister tous les partenaires
    @GetMapping("/partenaires")
    public List<PartenaireResponseDTO> lister() {
        return partenaireService.lister();
    }

    // GET /partenaires/actifs — lister les partenaires actifs
    @GetMapping("/partenaires/actifs")
    public List<PartenaireResponseDTO> listerActifs() {
        return partenaireService.listerActifs();
    }

    // GET /partenaires?type=SPONSOR_OR — filtrer par type
    @GetMapping(value = "/partenaires", params = "type")
    public List<PartenaireResponseDTO> listerParType(@RequestParam TypePartenaireEnum type) {
        return partenaireService.listerParType(type);
    }

    // GET /partenaires/{id} — lire un partenaire
    @GetMapping("/partenaires/{id}")
    public PartenaireResponseDTO lire(@PathVariable Long id) {
        return partenaireService.lire(id);
    }

    // PUT /partenaires/{id} — modifier un partenaire
    @PutMapping("/partenaires/{id}")
    public PartenaireResponseDTO modifier(@PathVariable Long id,
                                          @Valid @RequestBody PartenaireUpdateDTO dto) {
        return partenaireService.modifier(id, dto);
    }

    // PATCH /partenaires/{id}/activer
    @PatchMapping("/partenaires/{id}/activer")
    public PartenaireResponseDTO activer(@PathVariable Long id) {
        return partenaireService.activer(id);
    }

    // PATCH /partenaires/{id}/desactiver
    @PatchMapping("/partenaires/{id}/desactiver")
    public PartenaireResponseDTO desactiver(@PathVariable Long id) {
        return partenaireService.desactiver(id);
    }

    // DELETE /partenaires/{id}
    @DeleteMapping("/partenaires/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        partenaireService.supprimer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
