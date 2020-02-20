package com.java.rest.models.dao.implement;

import com.java.rest.models.dao.IPhoneDao;
import com.java.rest.models.entity.Phone;
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
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class PhoneDaoImpl implements IPhoneDao {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Phone> findAll() {
        Session session;
        Query<Phone> allQuery = null;
        try {

            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Phone> query = cb.createQuery(Phone.class);
            Root<Phone> rootEntry = query.from(Phone.class);
            CriteriaQuery<Phone> selectAll = query.select(rootEntry);

            allQuery = session.createQuery(selectAll);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Phone> phonesResult;
        try {
            phonesResult = allQuery.list();
        } catch (NoResultException e) {
            return Collections.EMPTY_LIST;
        }
        return phonesResult;
    }

    @Override
    public Phone findById(Integer id) {
        Phone phone = null;
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            phone = session.find(Phone.class, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return phone;
    }

    @Override
    public Phone save(Phone phone) {
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            session.persist(phone);
            session.getTransaction().commit();

        } catch (Exception e) {
            logger.error("Error saving new phone", e);
        }
        return phone;
    }

    @Override
    public Phone update(Phone phone) {
        Session session;
        Phone personMerged = null;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            session.evict(phone);
            personMerged = (Phone) session.merge(phone);
            session.getTransaction().commit();

        } catch (Exception e) {
            logger.error("Error updating new phone", e);
        }
        return personMerged;
    }

    @Override
    public void delete(Integer id) {
        Session session;
        try {
            session = getSession();
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }
            Phone person = session.find(Phone.class, id);
            session.delete(person);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session getSession() {
        Session session = sessionFactory.getCurrentSession();
        if (session == null) {
            session = sessionFactory.openSession();
        }
        return session;
    }
}
