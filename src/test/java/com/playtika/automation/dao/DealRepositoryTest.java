package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.DealEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.playtika.automation.domain.DealStatus.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;

public class DealRepositoryTest extends AbstractDaoTest {
    @Autowired
    DealEntityRepository dao;

    @Test
    @DataSet(value = "deal-by-id.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByDealId() {
        DealEntity dealEntity = new DealEntity(2L, null, 200, null, ACTIVE);

        DealEntity findByIdDeal = dao.findById(2L);

        assertThat(findByIdDeal).isEqualToIgnoringNullFields(dealEntity);
    }

    @Test
    @DataSet(value = "deal-by-advertId.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByAdvertId() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(2L);
        dealEntity.setPrice(200.0);
        dealEntity.setStatus(ACTIVE);
        dealEntity.setAdvert(new AdvertEntity(1L, 200.0, 1L));

        List<DealEntity> findById = dao.findByAdvertId(1L);

        assertThat(findById).hasSize(1);
        assertThat(findById.get(0)).isEqualToComparingFieldByFieldRecursively(dealEntity);
    }

    @Test
    @DataSet(value = "deal-by-advertId.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByAdvertIdAndStatus() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(2L);
        dealEntity.setPrice(200.0);
        dealEntity.setStatus(ACTIVE);
        dealEntity.setAdvert(new AdvertEntity(1L, 200.0, 1L));

        List<DealEntity> findById = dao.findByAdvertIdAndStatus(1L, ACTIVE);

        assertThat(findById).hasSize(1);

        assertThat(findById.get(0)).isEqualToComparingFieldByFieldRecursively(dealEntity);
    }

}