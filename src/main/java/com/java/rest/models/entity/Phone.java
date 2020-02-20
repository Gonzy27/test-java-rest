package com.java.rest.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "phones")
public class Phone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "The number field cannot be empty")
    private Integer number;

    @NotNull(message = "The city code field cannot be empty")
    private Integer cityCode;

    @NotNull(message = "The country code field cannot be empty")
    private Integer contryCode;

    @JsonIgnoreProperties(value = {"phones"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person person;

    public Phone() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getContryCode() {
        return contryCode;
    }

    public void setContryCode(Integer contryCode) {
        this.contryCode = contryCode;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
