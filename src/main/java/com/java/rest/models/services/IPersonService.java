package com.java.rest.models.services;

import com.java.rest.models.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPersonService {

    List<Person> findAll();

    Person findById(UUID id);

    Person save(Person person);

    Person update(Person person);

    void delete(UUID id);

    String login(String email, String password);

    Boolean logout(String email);

    Optional<org.springframework.security.core.userdetails.User> findByToken(String token);
}
