package com.ugive.services;

import com.ugive.models.Role;
import com.ugive.models.UserBalance;
import com.ugive.requests.RoleRequest;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> create(RoleRequest roleRequest);

    Optional<UserBalance> update(Long id, RoleRequest roleRequest);

    List<Role> findAll();

    Role findOne(Long id);

    void delete(Long id);
}
