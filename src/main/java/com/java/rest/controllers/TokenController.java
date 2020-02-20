package com.java.rest.controllers;

import com.java.rest.models.services.IPersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenController {

    @Autowired
    private IPersonService userService;

    @PostMapping(value = "/users/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken(@RequestParam("email") final String email, @RequestParam("password") final String password) {
        Map<String, Object> response = new HashMap<>();
        String token = userService.login(email, password);
        if (StringUtils.isEmpty(token)) {
            response.put("message", "Login failed");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        response.put("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/users/token/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteToken(@RequestParam("email") final String email) {
        Map<String, Object> response = new HashMap<>();
        Boolean token = userService.logout(email);
        if (!token) {
            response.put("mensaje", "The user is not logged in");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("message", "User logged out successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}