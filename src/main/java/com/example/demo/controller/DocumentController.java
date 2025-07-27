package com.example.demo.controller;

import java.util.List; 
import java.io.IOException;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.entity.TypeDocument;
import com.example.demo.services.DocumentService;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }
    
    @GetMapping("/with-stage")
    public ResponseEntity<List<DocumentDTO>> getDocumentsWithStage() {
        return ResponseEntity.ok(documentService.getDocumentsWithStage());
    }

    @GetMapping("/without-stage")
    public ResponseEntity<List<DocumentDTO>> getDocumentsWithoutStage() {
        return ResponseEntity.ok(documentService.getDocumentsWithoutStage());
    }
    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") TypeDocument type,
            @RequestParam(name = "stageId", required = false) Integer stageId,
            @RequestParam("etudiantId") Integer etudiantId
    ) throws IOException {
        // Le service gère stageId == null sans exception
        DocumentDTO dto = documentService.uploadDocument(stageId, file, type, etudiantId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<DocumentDTO> validateDocument(@PathVariable Integer id) {
        return ResponseEntity.ok(documentService.validateDocument(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<DocumentDTO> rejectDocument(
            @PathVariable Integer id,
            @RequestParam String reason
    ) {
        return ResponseEntity.ok(documentService.rejectDocument(id, reason));
    }
    
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Integer documentId) {
        DocumentDTO document = documentService.getDocumentById(documentId);
        Resource resource    = documentService.downloadDocument(documentId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getNom() + "\"")
                .body(resource);
    }

    @GetMapping("/stage/{stageId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByStage(@PathVariable Integer stageId) {
        return ResponseEntity.ok(documentService.getDocumentsByStage(stageId));
    }

    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByEtudiant(@PathVariable Integer etudiantId) {
        return ResponseEntity.ok(documentService.getDocumentsByEtudiant(etudiantId));
    }
}
