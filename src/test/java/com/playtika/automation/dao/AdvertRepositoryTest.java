package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvertRepositoryTest extends AbstractDaoTest{

    @Qualifier("jpaAdvertEntityRepository")
    @Autowired
    AdvertEntityRepository dao;

    @Test
    @DataSet(value = "advert-by-status.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByStatus() {
        List<AdvertEntity> findByStatus = dao.findByStatus(AdvertStatus.OPEN);

        assertThat(findByStatus).hasSize(1);
    }

    @Test
    @DataSet(
        value = "advert-by-status-and-car-id.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByCarIdAndStatus() {
        CarEntity carEntity = constructCarEntity();
        carEntity.setId(1L);

        ClientEntity clientEntity = constructClientEntity();
        clientEntity.setId(1L);

        AdvertEntity expectedAdvert = constructAdvertEntity(carEntity, clientEntity);
        expectedAdvert.setId(1L);

        List<AdvertEntity> resultsAdvert = dao.findByCarIdAndStatus(1L, AdvertStatus.OPEN);

        assertThat(resultsAdvert).hasSize(1);
        assertAdvertEntitiesAreEqual(resultsAdvert.get(0), expectedAdvert);
    }

    @Test
    @DataSet(value = "advert-for-delete.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("expected-after-delete.xml")
    @Commit
    public void shouldDeleteByCarId() {
        dao.deleteByCarId(1);

    }

    @Test
    @DataSet(value = "empty-dataset.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("add-advert.xml")
    @Commit
    public void shouldAddAdvert() {
        CarEntity carEntity = new CarEntity("12-12", 2012, "green", "x5", "bmw");
        carEntity.setId(1L);
        ClientEntity clientEntity = new ClientEntity("andrey", "sevruk", "0937746730");
        clientEntity.setId(2l);

        AdvertEntity advertEntity = new AdvertEntity(carEntity, clientEntity, null, 100, AdvertStatus.OPEN);

        dao.save(advertEntity);
    }

    private CarEntity constructCarEntity() {
        return new CarEntity("12-12", 2012, "green", "x5", "bmw");
    }

    private ClientEntity constructClientEntity() {
        return new ClientEntity("andrey", "sevruk", "0937746730");
    }

    private AdvertEntity constructAdvertEntity(CarEntity carEntity, ClientEntity clientEntity) {
        return new AdvertEntity(carEntity, clientEntity, null, 200.0, AdvertStatus.OPEN);
    }

}