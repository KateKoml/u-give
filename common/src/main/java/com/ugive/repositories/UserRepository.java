package com.ugive.repositories;

import com.ugive.models.User;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @CacheEvict(value = "users", allEntries = true)
    @NonNull
    <S extends User> S save(S entity);

    @Cacheable("users")
    @NonNull
    Optional<User> findById(Long id);

    @Cacheable("users")
    @NonNull
    Page<User> findAll (Pageable pageable);

    @Cacheable("users")
    @NonNull
    List<User> findAll();

    //сделать для админа
//    @Cacheable("l_users_roles")
//    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
//    List<Role> findRolesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.isDeleted = true AND u.changed < :expirationDate")
    void deleteExpiredUsers(@Param("expirationDate") Timestamp expirationDate);
}
