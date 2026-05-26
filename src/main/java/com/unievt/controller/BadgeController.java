package com.unievt.controller;

import com.unievt.dto.BadgeResponseDTO;
import com.unievt.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    // GET /inscriptions/{id}/qrcode — retourne l'image PNG du QR code
    @GetMapping(value = "/inscriptions/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> qrCode(@PathVariable Long id) {
        byte[] png = badgeService.genererQrCodePng(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(png);
    }

    // GET /inscriptions/{id}/badge — retourne le badge JSON avec QR code en base64
    @GetMapping("/inscriptions/{id}/badge")
    public BadgeResponseDTO badge(@PathVariable Long id) {
        return badgeService.genererBadge(id);
    }
}
