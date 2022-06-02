package com.example.springloginjwtrole.dao;

import com.example.springloginjwtrole.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByName(String name);
}
