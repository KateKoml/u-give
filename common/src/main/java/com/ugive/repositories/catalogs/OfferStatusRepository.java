package com.ugive.repositories.catalogs;

import com.ugive.models.catalogs.OfferStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@Cacheable("c_offer_status")
public interface OfferStatusRepository extends JpaRepository<OfferStatus, Integer> {
}