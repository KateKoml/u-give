package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OfferStatusRepository extends
        JpaRepository<OfferStatus, Integer>,
        PagingAndSortingRepository<OfferStatus, Integer>,
        CrudRepository<OfferStatus, Integer> {
}