package com.ugive.repositories;

import com.ugive.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;
@Cacheable("users")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(String userName);

//    @Cacheable("l_users_roles")
//    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
//    List<Role> findRolesByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.isDeleted = true AND u.changed < :expirationDate")
    void deleteExpiredUsers(@Param("expirationDate") Timestamp expirationDate);
}
