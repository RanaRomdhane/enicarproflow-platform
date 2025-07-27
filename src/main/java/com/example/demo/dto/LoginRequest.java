package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("email")
    private String email;
    @JsonProperty("motDePasse")

    private String motDePasse;
}
