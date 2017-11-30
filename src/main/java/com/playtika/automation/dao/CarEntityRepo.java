package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CarEntityRepo extends JpaRepository<CarEntity, Long>, CarEntityStatisticRepo {
    List<CarEntity> findByYearAndBrandOrderByPlateNumber(int year, String brand, Sort sort);
    Page<CarEntity> findByYearAndOwnerFirstName(int year, String firstName, Pageable pageable);

    Optional<CarEntity> getByPlateNumber(String plateNumber);



    @Query(select c.year, count(c) from CarEntty where c =:brand group by c,yeR)
    Map<Integer, Integer> getSoldCarPerYear(String brand);





}
