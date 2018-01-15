package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.dao.entity.DealEntity;
import com.playtika.automation.domain.AdvertStatus;
import com.playtika.automation.domain.DealStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DealRepositoryTest extends AbstractDaoTest {
    @Autowired
    DealEntityRepository dao;

    @Test
    @DataSet(value = "update-deal-with-reject-status.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("expected-deal-after-reject.xml")
    @Commit
    public void shouldRejectDealById() {
        dao.updateDealWithRejectStatus(1);
    }

    @Test
    @DataSet(value = "deal-by-id.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByDealId() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(2L);
        dealEntity.setPrice(200.0);
        dealEntity.setStatus(DealStatus.ACTIVE);

        List<DealEntity> findById = dao.findById(2L);

        assertThat(findById).hasSize(1);
        assertThat(findById.get(0)).isEqualToIgnoringNullFields(dealEntity);
    }

    @Test
    @DataSet(value = "deal-by-advertId.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByAdvertId() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(2L);
        dealEntity.setPrice(200.0);
        dealEntity.setStatus(DealStatus.ACTIVE);
        dealEntity.setAdvert(new AdvertEntity(1L,200.0));

        List<DealEntity> findById = dao.findByAdvertId(1L);

        assertThat(findById).hasSize(1);
        assertThat(findById.get(0)).isEqualToIgnoringNullFields(dealEntity);
    }

    @Test
    @DataSet(value = "deal-by-advertId.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByAdvertIdAndStatus() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(2L);
        dealEntity.setPrice(200.0);
        dealEntity.setStatus(DealStatus.ACTIVE);
        dealEntity.setAdvert(new AdvertEntity(1L,200.0));

        List<DealEntity> findById = dao.findByAdvertIdAndStatus(1L, DealStatus.ACTIVE);

        assertThat(findById).hasSize(1);
        assertThat(findById.get(0)).isEqualToIgnoringNullFields(dealEntity);
    }

}