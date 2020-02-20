package com.java.rest.models.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class Person implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotEmpty(message = "The name field cannot be empty")
    @Size(min = 4, max = 12, message = "The name size must be between 4 and 12")
    @Column(nullable = false)
    private String name;

    @Valid
    @NotEmpty(message = "The email field cannot be empty")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "(\\S+)\\@(\\S+)\\.(\\S+)", message = "The email has not the right format")
    private String email;

    @NotEmpty(message = "The password field cannot be empty")
    @Pattern(regexp = "^(?=.*[0-9]{2})(?=.*[a-z])(?=.*[A-Z]{1,}).{1,}$",
            message = "The password doesnt have the right forma (at least one uppercase letter, at least two numbers and one lower case number)")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    private Boolean isActive;

    private String token;

    @JsonIgnoreProperties(value = {"person"}, allowSetters = true)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Phone> phones;

    @PrePersist
    public void prePersist() throws UnsupportedEncodingException {
        created = new Date();
        modified = new Date();
        lastLogin = new Date();
        isActive = true;
        token = Jwts.builder()
                .setSubject(this.email)
                .setIssuer(this.name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "123456".getBytes("UTF-8"))
                .compact();
    }

    public Person() {
        this.phones = new LinkedList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    private static final long serialVersionUID = 1L;
}
