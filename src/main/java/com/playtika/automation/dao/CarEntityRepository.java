package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.CarEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CarEntityRepository extends CrudRepository {
    List<CarEntity> findByPlateNumber(String plateNumber);
}
