package com.online.university.hrynyk.repository;

import com.online.university.hrynyk.model.PriceData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PriceRepo extends JpaRepository<PriceData, Long> {
    PriceData findByPrice(float price);
}
