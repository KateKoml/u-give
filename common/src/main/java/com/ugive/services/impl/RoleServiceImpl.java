package com.ugive.services.impl;

import com.ugive.exceptions.EntityNotFoundException;
import com.ugive.exceptions.ForbiddenChangeException;
import com.ugive.mappers.RoleMapper;
import com.ugive.models.Role;
import com.ugive.repositories.RoleRepository;
import com.ugive.requests.RoleRequest;
import com.ugive.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = Logger.getLogger(RoleServiceImpl.class);
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role create(RoleRequest roleRequest) {
        Role role;
        try {
        role = roleMapper.toEntity(roleRequest);
        } catch (ForbiddenChangeException e) {
            logger.error("Wrong mapping for entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Integer id, RoleRequest roleRequest) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("This role doesn't exist!"));
        try {
            roleMapper.updateEntityFromRequest(roleRequest, role);
        } catch (ForbiddenChangeException e) {
            logger.error("Error updating role request to entity. " + e.getMessage());
            throw new ForbiddenChangeException(e.getMessage());
        }
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAllByIsDeletedFalse();
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
