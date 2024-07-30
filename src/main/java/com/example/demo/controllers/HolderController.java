package com.example.demo.controllers;


import com.example.demo.core.services.HolderService;
import com.example.demo.dtos.AccountHolderPostDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/v1/holder")
@AllArgsConstructor
@Validated
public class HolderController {

    private HolderService holderService;

    @PostMapping
    public ResponseEntity<Object> createHolder(@Valid @RequestBody AccountHolderPostDTO request) {
        holderService.createHolder(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
