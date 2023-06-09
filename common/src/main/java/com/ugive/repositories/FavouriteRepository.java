package com.ugive.repositories;

import com.ugive.models.Favourite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    Page<Favourite> findAllByUserId(Long userId, Pageable pageable);
    List<Favourite> findAllByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Favourite f WHERE f.isDeleted = true AND f.changed < :expirationDate")
    void deleteExpiredFavourite(@Param("expirationDate") Timestamp expirationDate);
}