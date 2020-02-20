package com.java.rest.models.dao;

import com.java.rest.models.entity.Phone;

import java.util.List;

public interface IPhoneDao {

    List<Phone> findAll();

    Phone findById(Integer id);

    Phone save(Phone phone);

    Phone update(Phone phone);

    void delete(Integer id);

}
