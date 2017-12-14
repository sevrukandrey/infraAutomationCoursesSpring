package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvertRepositoryTest extends AbstractDaoTest {
    @Test
    @DataSet(value = "expected-advert-by-status.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByStatus() {
        List<AdvertEntity> findByStatus = advertDao.findByStatus(AdvertStatus.OPEN);

        assertThat(findByStatus).hasSize(1);
    }

    @Test
    @DataSet(
        value = "expected-advert-by-status-and-car-id.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByCarIdAndStatus() {
        CarEntity carEntity = constructCarEntity();
        ClientEntity clientEntity = constructClientEntity();
        AdvertEntity expectedAdvert = constructAdvertEntity(carEntity, clientEntity);

        List<AdvertEntity> resultsAdvert = advertDao.findByCarIdAndStatus(1L, AdvertStatus.OPEN);

        assertThat(resultsAdvert).hasSize(1);
        assertAdvertEntitiesAreEqual(resultsAdvert.get(0), expectedAdvert);
    }

    @Test
    @DataSet(value = "expected-advert-for-delete.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldDeleteByCarId() {
        advertDao.deleteByCarId(1L);

        assertThat(advertDao.findOne(1L)).isNull();
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