package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.DealEntity;
import com.playtika.automation.domain.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealEntityRepository extends JpaRepository<DealEntity, Long> {
    List<DealEntity> findByAdvertId(Long id);

    List<DealEntity> findByAdvertIdAndStatus(Long id, DealStatus dealStatus);

    DealEntity findById(Long id);

    List<DealEntity> findByAdvertIdAndBuyerIdAndPriceAndStatus(long advertId, Long ClientId, double price, DealStatus dealStatus);
}
