package com.example.demo.controllers;


import com.example.demo.core.services.PixKeyService;
import com.example.demo.dtos.EditPixKeyRequestDTO;
import com.example.demo.dtos.KeySearchCriteriaRequestDTO;
import com.example.demo.dtos.PixKeyIdDTO;
import com.example.demo.dtos.PixKeyRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/v1/key")
@AllArgsConstructor
@Validated
public class PixController {

    private PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<Object> addPixKey(@Valid @RequestBody PixKeyRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(pixKeyService.includeKey(request));
    }

    @PutMapping
    public ResponseEntity<Object> editKey(@Valid @RequestBody EditPixKeyRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(pixKeyService.editKey(request));
    }

    @PatchMapping
    public ResponseEntity<Object> deactivateKey(@RequestBody PixKeyIdDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(pixKeyService.deactivateKey(request));
    }

    @GetMapping
    public ResponseEntity<Object> filterByCriteria(KeySearchCriteriaRequestDTO criteria) {
        return ResponseEntity.status(HttpStatus.OK).body(pixKeyService.filter(criteria));
    }
}
