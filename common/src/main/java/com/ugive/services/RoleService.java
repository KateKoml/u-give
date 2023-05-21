package com.ugive.services;

import com.ugive.models.Role;
import com.ugive.requests.RoleRequest;

import java.util.List;

public interface RoleService {
    Role create(RoleRequest roleRequest);

    Role update(Integer id, RoleRequest roleRequest);

    List<Role> findAll();

    Role findOne(Integer id);

    void delete(Integer id);
}
