package com.reviewblog.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthReponse {
    private String token;
    private String username;
    private String role;
}
