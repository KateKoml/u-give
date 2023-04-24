package com.ugive.repositories;

import com.ugive.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoleRepository extends
        JpaRepository<Role, Integer>,
        PagingAndSortingRepository<Role, Integer>,
        CrudRepository<Role, Integer> {

}