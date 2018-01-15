package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.DealEntity;
import com.playtika.automation.domain.AdvertStatus;
import com.playtika.automation.domain.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealEntityRepository extends JpaRepository<DealEntity, Long> {
    @Modifying
    @Query("UPDATE deal d SET d.status = 'REJECTED' WHERE d.id = :id")
    int updateDealWithRejectStatus(@Param("id") long id);

    @Modifying
    @Query("UPDATE deal d SET d.status = 'APPROVED' WHERE d.id = :id")
    int updateDealWithApproveStatus(@Param("id") long id);

    List<DealEntity> findByAdvertId(Long id);

    List<DealEntity> findByAdvertIdAndStatus(Long id, DealStatus dealStatus);

    List<DealEntity> findById(Long id);

}
