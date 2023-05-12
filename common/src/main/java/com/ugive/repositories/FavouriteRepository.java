package com.ugive.repositories;

import com.ugive.models.Favourite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Optional;

public interface FavouriteRepository extends
        JpaRepository<Favourite, Long>,
        PagingAndSortingRepository<Favourite, Long>,
        CrudRepository<Favourite, Long> {
    Page<Favourite> findAllByUserId(Long userId, Pageable pageable);

    Optional<Favourite> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("DELETE FROM Favourite f WHERE f.isDeleted = true AND f.changed < :expirationDate")
    void deleteExpiredFavourite(@Param("expirationDate") Timestamp expirationDate);
}