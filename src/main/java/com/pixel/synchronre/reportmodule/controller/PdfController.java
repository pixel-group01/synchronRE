package com.pixel.synchronre.reportmodule.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api")
public class PdfController {

    @PostMapping("/decode-base64")
    public ResponseEntity<byte[]> decodeBase64ToPdf(@RequestBody Base64Request request) {
        try {
            // Décoder la chaîne Base64
            byte[] pdfBytes = Base64.getDecoder().decode(request.getBase64UrlString());

            // Définir les en-têtes HTTP pour la réponse
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=document.pdf");
            headers.add("Content-Type", "application/pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
