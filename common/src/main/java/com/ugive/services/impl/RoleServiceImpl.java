package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.mappers.RoleMapper;
import com.ugive.models.Role;
import com.ugive.repositories.RoleRepository;
import com.ugive.requests.RoleRequest;
import com.ugive.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role create(RoleRequest roleRequest) {
        Role role = roleMapper.toEntity(roleRequest);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Integer id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist!"));
        roleMapper.updateEntityFromRequest(roleRequest, role);
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findOne(Integer id) {
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist!"));
    }

    @Override
    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }
}
