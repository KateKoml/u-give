package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferStatusRepository extends JpaRepository<OfferStatus, Integer> {
}