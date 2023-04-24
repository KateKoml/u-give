package com.ugive.repositories;

import com.ugive.models.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavouritesRepository extends
        JpaRepository<Favourite, Long>,
        PagingAndSortingRepository<Favourite, Long>,
        CrudRepository<Favourite, Long> {
}