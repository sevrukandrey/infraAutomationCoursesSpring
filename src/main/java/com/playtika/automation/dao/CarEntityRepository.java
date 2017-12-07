package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarEntityRepository extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findByPlateNumber(String plateNumber);
}
