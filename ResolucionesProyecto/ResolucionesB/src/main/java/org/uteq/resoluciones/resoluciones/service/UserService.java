package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.entities.User;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> findAll();
    User findById(Long id);
    void deleteByid(Long user);
    User update(User user);
}