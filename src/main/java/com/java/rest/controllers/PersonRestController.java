package com.java.rest.controllers;

import com.java.rest.models.entity.Person;
import com.java.rest.models.services.IPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/api")
@RestController
@Validated
public class PersonRestController {

    @Autowired
    private IPersonService personService;

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> index() {
        return personService.findAll();
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(@PathVariable UUID id) {

        Person person = null;
        Map<String, Object> response = new HashMap<>();

        try {
            person = personService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error when reading the database");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (person == null) {
            response.put("message", "The user ID: ".concat(id.toString().concat(" doesnt exists")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Person person, BindingResult result) {

        Person personNew = null;
        Map<String, Object> response = new HashMap<>();

        try {

            personNew = personService.save(person);

        } catch (Exception e) {
            response.put("message", "Error when inserting the new user in the database: " + e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The user has been created");
        response.put("cliente", personNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody Person person, BindingResult result, @PathVariable UUID id) {

        Person personActual = personService.findById(id);
        Person personUpdated;

        Map<String, Object> response = new HashMap<>();

        if (personActual == null) {
            response.put("message", "Error: cant update, the user ID: "
                    .concat(id.toString().concat(" doesnt exists")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {

            personActual.setPassword(person.getPassword());
            personActual.setName(person.getName());
            personActual.setEmail(person.getEmail());
            personActual.setModified(new Date());
            personUpdated = personService.update(personActual);

        } catch (DataAccessException e) {
            response.put("message", "Error when updating the user in the database: " + e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The user has been updated");
        response.put("cliente", personUpdated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        Map<String, Object> response = new HashMap<>();

        try {
            personService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error when deleting the user in the database: " + e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The user has been eliminate");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
