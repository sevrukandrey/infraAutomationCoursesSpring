package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AdvertEntityRepository extends CrudRepository<AdvertEntity, Long> {

    List<AdvertEntity> findByStatus(AdvertStatus status);

    List<AdvertEntity> findByCarIdAndStatus(long carId, AdvertStatus status);

    void deleteByCarId(long carId);
}
