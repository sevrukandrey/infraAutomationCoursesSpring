package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.CarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CarEntityRepository extends CrudRepository<CarEntity, Long> {
    List<CarEntity> findByPlateNumber(String plateNumber);
}
