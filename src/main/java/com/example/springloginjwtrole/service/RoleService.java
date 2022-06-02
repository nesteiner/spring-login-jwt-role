package com.example.springloginjwtrole.service;

import com.example.springloginjwtrole.dao.RoleDao;
import com.example.springloginjwtrole.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    RoleDao roleDao;

    public Role findByName(String name) {
        Role role = roleDao.findRoleByName(name);
        return role;
    }
}
