package com.ugive.repositories;

import com.ugive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        PagingAndSortingRepository<User, Long>,
        CrudRepository<User, Long> {
    Optional<User> findById(Long id);

    User findByUserName(String userName);
}
