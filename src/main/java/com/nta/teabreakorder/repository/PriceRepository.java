package com.nta.teabreakorder.repository;

import com.nta.teabreakorder.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price,Long> {
}
