package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class AdvertRepositoryTest extends AbstractDao<AdvertEntityRepository> {
    @Test
    @DataSet(value = "expected-advert-by-status.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindAdvertByStatus() {
        List<AdvertEntity> findByStatus = advertDao.findByStatus(AdvertStatus.OPEN);

        assertThat(findByStatus, hasSize(1));
    }

    @Test
    @DataSet(
            value = "expected-advert-by-status-and-car-id.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindAdvertByCarIdAndStatus() {
        CarEntity carEntity = constructCarEntity();

        List<AdvertEntity> findByCarIdAndStatus = advertDao.findByCarIdAndStatus(1, AdvertStatus.OPEN);

        assertThat(findByCarIdAndStatus, hasSize(1));
        assertThat(findByCarIdAndStatus.get(0).getCar(), samePropertyValuesAs(carEntity));
    }

    @Test
    @DataSet(value = "expected-advert-for-delete.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldDeleteByCarId() {
        advertDao.deleteByCarId(1L);

        assertThat(advertDao.findAll(), hasSize(0));
    }

    @Test
    @DataSet(value = "empty-advert.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    @ExpectedDataSet("add-advert.xml")
    @Commit
    public void shouldAddAdvert() {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setPrice(100.0);
        advertEntity.setStatus(AdvertStatus.OPEN);
        advertEntity.setCar(constructCarEntity());
        advertEntity.setClient(constructClientEntity());

        advertDao.save(advertEntity);
    }
}