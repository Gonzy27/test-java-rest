package com.java.rest.controllers;

import com.java.rest.models.entity.Person;
import com.java.rest.models.entity.Phone;
import com.java.rest.models.services.IPersonService;
import com.java.rest.models.services.IPhoneService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
public class PhoneRestController {

    @Autowired
    private IPhoneService phoneService;
    @Autowired
    private IPersonService personService;


    @GetMapping(value = "/phones", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Phone> index() {
        return phoneService.findAll();
    }

    @GetMapping(value = "/phones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(@PathVariable Integer id) {

        Phone phone;
        Map<String, Object> response = new HashMap<>();

        try {
            phone = phoneService.findById(id);
        } catch (DataAccessException e) {
            response.put("message", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (phone == null) {
            response.put("message", "El phone ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(phone, HttpStatus.OK);
    }

    @PostMapping(value = "/{personId}/phones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Valid @RequestBody Phone phone, @PathVariable UUID personId, BindingResult result) {

        Person personFinded;
        Phone phoneNew;
        Map<String, Object> response = new HashMap<>();

        try {
            personFinded = personService.findById(personId);

            if (personFinded == null) {
                response.put("message", "El person ID: ".concat(personId.toString().concat(" no existe en la base de datos!")));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            personFinded.setModified(new Date());
            phone.setPerson(personFinded);

            phoneNew = phoneService.save(phone);
        } catch (Exception e) {
            response.put("message", "Error al realizar el insert en la base de datos: " + e.getLocalizedMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "El teléfono ha sido creado con éxito!");
        response.put("cliente", phoneNew);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{personId}/phones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody Phone phone, BindingResult result, @PathVariable UUID personId, @PathVariable Integer id) {

        Person personFinded;
        Phone phoneToUpdate;
        Phone phoneUpdated;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            personFinded = personService.findById(personId);

            if (personFinded == null) {
                response.put("message", "The phone ID: ".concat(personId.toString().concat(" doesnt exists")));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            personFinded.setModified(new Date());

            phoneToUpdate = phoneService.findById(id);

            if (phoneToUpdate == null) {
                response.put("message", "Error: when updating the phone , phone ID: "
                        .concat(id.toString().concat(" doesnt exists")));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }


            phoneToUpdate.setNumber(phone.getNumber());
            phoneToUpdate.setCityCode(phone.getCityCode());
            phoneToUpdate.setContryCode(phone.getContryCode());
            phoneToUpdate.setPerson(personFinded);
            phoneUpdated = phoneService.update(phoneToUpdate);

        } catch (Exception e) {
            response.put("message", "Error when updating the phone");
            response.put("error", e.getMessage().concat(": ").concat(e.getLocalizedMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The phone has been updated!");
        response.put("cliente", phoneUpdated);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/phones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        Map<String, Object> response = new HashMap<>();

        try {
            phoneService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error when deleting the phone");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "The hone has been deleted");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
