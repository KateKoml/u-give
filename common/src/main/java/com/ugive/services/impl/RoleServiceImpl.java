package com.ugive.services.impl;

import com.ugive.models.Role;
import com.ugive.models.UserBalance;
import com.ugive.requests.RoleRequest;
import com.ugive.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public Optional<Role> create(RoleRequest roleRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<UserBalance> update(Long id, RoleRequest roleRequest) {
        return Optional.empty();
    }

    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public Role findOne(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
