package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertEntityRepository extends JpaRepository<AdvertEntity, Long> {
    List<AdvertEntity>  findByStatus(AdvertStatus status);

    List<AdvertEntity> findByCarIdAndStatus(long carId, AdvertStatus status);

    void deleteByCarId(Long car_id);
}
