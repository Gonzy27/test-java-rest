package com.java.rest.models.services.implement;

import com.java.rest.models.dao.IPhoneDao;
import com.java.rest.models.entity.Phone;
import com.java.rest.models.services.IPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneServiceImpl implements IPhoneService {

    @Autowired
    private IPhoneDao phoneDao;

    @Override
    public List<Phone> findAll() {
        return phoneDao.findAll();
    }

    @Override
    public Phone findById(Integer id) {
        return phoneDao.findById(id);
    }

    @Override
    public Phone save(Phone phone) {
        return phoneDao.save(phone);
    }

    @Override
    public Phone update(Phone phone) {
        return phoneDao.update(phone);
    }

    @Override
    public void delete(Integer id) {
        phoneDao.delete(id);
    }

}
