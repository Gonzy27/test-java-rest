package com.java.rest.models.dao.implement;

import com.java.rest.models.dao.IPersonDao;
import com.java.rest.models.entity.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class PersonDaoImpl implements IPersonDao {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Person> findAll() {
        Session session;
        Query<Person> allQuery;
        try {

            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            Root<Person> rootEntry = query.from(Person.class);
            CriteriaQuery<Person> selectAll = query.select(rootEntry);

            allQuery = session.createQuery(selectAll);
        } catch (Exception e) {
            logger.error("Error finding all users");
            throw new RuntimeException(e);
        }
        List<Person> personList;
        try {
            personList = allQuery.list();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
        return personList;
    }

    @Override
    public Person findById(UUID id) {

        Person person;
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            person = session.find(Person.class, id);

        } catch (Exception e) {
            logger.error("Error finding a user by id");
            throw new RuntimeException(e);
        }
        return person;
    }

    @Override
    public Person save(Person person) {
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            session.persist(person);
            session.getTransaction().commit();

        } catch (Exception e) {
            logger.error("Error saving the new user");
            throw new RuntimeException(e);
        }
        return person;
    }

    @Override
    public Person update(Person person) {
        Session session;
        Person personMerged;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            session.evict(person);
            personMerged = (Person) session.merge(person);
            session.getTransaction().commit();

        } catch (Exception e) {
            logger.error("Error updating the user");
            throw new RuntimeException(e);
        }
        return personMerged;
    }

    @Override
    public void deleteById(UUID id) {
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            Person person = session.find(Person.class, id);
            session.delete(person);

            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error deleting the user");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional login(String email) {
        Session session;
        Query<Person> allQuery;

        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            Root<Person> rootEntry = query.from(Person.class);

            Predicate predicateForEmail
                    = cb.equal(rootEntry.get("email"), email);

            query.where(predicateForEmail);
            CriteriaQuery<Person> selectAll = query.select(rootEntry);

            allQuery = session.createQuery(selectAll);
        } catch (Exception e) {
            logger.error("Error when login");
            throw new RuntimeException(e);
        }
        Person personFound;
        try {
            personFound = allQuery.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.of(personFound);
    }

    @Override
    public Optional findByToken(String token) {
        Session session;
        Query<Person> allQuery;

        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }

            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Person> query = cb.createQuery(Person.class);
            Root<Person> rootEntry = query.from(Person.class);
            query.where(cb.equal(rootEntry.get("token"), token));

            CriteriaQuery<Person> selectAll = query.select(rootEntry);

            allQuery = session.createQuery(selectAll);

        } catch (Exception e) {
            logger.error("Error when logout");
            throw new RuntimeException(e);
        }
        Person personFound;
        try {
            personFound = allQuery.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }
        return Optional.of(personFound);
    }


    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;
    }
}
