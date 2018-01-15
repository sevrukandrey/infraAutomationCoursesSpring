package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertEntityRepository extends JpaRepository<AdvertEntity, Long> {

    List<AdvertEntity> findByStatus(AdvertStatus status);

    List<AdvertEntity> findByCarIdAndStatus(long carId, AdvertStatus status);

    List<AdvertEntity> findByCarIdAndClientIdAndPriceAndStatus(long carId, long clientId, double price, AdvertStatus advertStatus);

    List<AdvertEntity> findByCarIdAndClientIdAndStatus(long carId, long clientId, AdvertStatus advertStatus);

    void deleteByCarId(long carId);

    AdvertEntity findById(Long id);

}
