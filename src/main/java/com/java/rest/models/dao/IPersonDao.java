package com.java.rest.models.dao;

import com.java.rest.models.entity.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPersonDao {

    List<Person> findAll();
    Person findById(UUID id);
    Person save(Person person);
    Person update(Person person);
    void deleteById(UUID id);
    Optional login(String email);
    Optional findByToken(String token);
}
