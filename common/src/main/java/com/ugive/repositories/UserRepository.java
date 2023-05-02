package com.ugive.repositories;

import com.ugive.models.Role;
import com.ugive.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        PagingAndSortingRepository<User, Long>,
        CrudRepository<User, Long> {
    Optional<User> findById(Long id);

    User findByUserName(String userName);

    @Cacheable("l_users_roles")
    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    List<Role> findRolesByUserId(@Param("userId") Long userId);
}
