package com.java.rest.models.services.implement;

import com.java.rest.models.dao.IPersonDao;
import com.java.rest.models.entity.Person;
import com.java.rest.models.services.IPersonService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersonServiceImpl implements IPersonService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private IPersonDao personDao;

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Person findById(UUID id) {
        return personDao.findById(id);
    }

    @Override
    @Transactional
    public Person save(Person person) {
        String passwordEncode = bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(passwordEncode);
        return personDao.save(person);
    }

    @Override
    @Transactional
    public Person update(Person person) {
        return personDao.update(person);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        personDao.deleteById(id);
    }

    @Override
    public String login(String email, String password) {
        Optional user = personDao.login(email);
        if (user.isPresent()) {
            String token = null;

            Person personToken = (Person) user.get();

            if (!bCryptPasswordEncoder.matches(password, personToken.getPassword())) {
                return StringUtils.EMPTY;
            }

            try {
                token = Jwts.builder()
                        .setSubject(personToken.getEmail())
                        .setIssuer(personToken.getName())
                        .setIssuedAt(new Date())
                        .signWith(SignatureAlgorithm.HS512, "123456".getBytes("UTF-8"))
                        .compact();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            personToken.setToken(token);
            personToken.setModified(new Date());
            personToken.setLastLogin(new Date());
            personDao.update(personToken);
            return token;
        }

        return StringUtils.EMPTY;
    }

    @Override
    public Boolean logout(String email) {
        Optional user = personDao.login(email);
        if (user.isPresent()) {

            Person personToken = (Person) user.get();
            if (personToken.getToken() != null) {
                personToken.setToken(null);
                personToken.setModified(new Date());
                personToken.setLastLogin(new Date());
                personDao.update(personToken);
                return true;
            }
        }

        return false;
    }

    @Override
    public Optional<User> findByToken(String token) {
        Optional userGetToken = personDao.findByToken(token);
        if (userGetToken.isPresent()) {
            Person personGet = (Person) userGetToken.get();
            User user =
                    new User(personGet.getEmail(), personGet.getPassword(), personGet.getIsActive(), true, true, true,
                            AuthorityUtils.createAuthorityList("USER"));
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
