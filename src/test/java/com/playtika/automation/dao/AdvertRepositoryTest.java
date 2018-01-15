package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvertRepositoryTest extends AbstractDaoTest {

    @Autowired
    AdvertEntityRepository dao;

    @Test
    @DataSet(value = "advert-by-status.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByStatus() {

        CarEntity carEntity = new CarEntity("12-12", 2012, "green", "x5", "bmw");
        carEntity.setId(1L);
        ClientEntity clientEntity = new ClientEntity("andrey", "sevruk", "0937746730");
        clientEntity.setId(2L);

        List<AdvertEntity> findByStatus = dao.findByStatus(AdvertStatus.OPEN);

        assertThat(findByStatus).hasSize(1);

        assertThat(findByStatus.get(0).getId()).isEqualTo(1L);
        assertThat(findByStatus.get(0).getPrice()).isEqualTo(200);
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
        assertThat(resultsAdvert.get(0).getId()).isEqualTo(1L);
        assertThat(resultsAdvert.get(0).getClient()).isEqualToComparingFieldByField(clientEntity);
        assertThat(resultsAdvert.get(0).getCar()).isEqualToComparingFieldByField(carEntity);
        assertThat(resultsAdvert.get(0).getStatus()).isEqualTo(AdvertStatus.OPEN);

    }

    @Test
    @DataSet(value = "advert-for-delete.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("expected-after-delete.xml")
    @Commit
    public void shouldDeleteByCarId() {
        dao.deleteByCarId(1);

        assertThat(dao.count()).isZero();
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
        clientEntity.setId(2L);

        AdvertEntity advertEntity = new AdvertEntity(carEntity, clientEntity, null, 100, AdvertStatus.OPEN);

        dao.save(advertEntity);
    }

    @Test
    @DataSet(value = "add-advert.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldReturnEmptyListIfAdvertByStatusNotFound() {
        List<AdvertEntity> findByStatus = dao.findByStatus(AdvertStatus.CLOSED);

        assertThat(findByStatus).isEmpty();
    }

    @Test
    @DataSet(value = "add-advert.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldReturnEmptyListIfAdvertByStatusAndCarNotFound() {
        List<AdvertEntity> findByStatus = dao.findByCarIdAndStatus(2, AdvertStatus.CLOSED);

        assertThat(findByStatus).isEmpty();
    }


    @Test
    @DataSet(value = "advert-by-carid-clientid-status-price.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByCarIdAndClientIdAndPriceAndStatus() {
        List<AdvertEntity> expectedAdvert =
                dao.findByCarIdAndClientIdAndPriceAndStatus(1L, 1L, 200.0, AdvertStatus.OPEN);

        assertThat(expectedAdvert).hasSize(1);
        assertThat(expectedAdvert.get(0).getCar().getId()).isEqualTo(1);
        assertThat(expectedAdvert.get(0).getClient().getId()).isEqualTo(1);
        assertThat(expectedAdvert.get(0).getPrice()).isEqualTo(200);
        assertThat(expectedAdvert.get(0).getStatus()).isEqualTo(AdvertStatus.OPEN);

    }

    @Test
    @DataSet(value = "advert-by-carid-clientid-status.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void findByCarIdAndClientIdAndStatus() {
       List<AdvertEntity> expectedAdvert =
                dao.findByCarIdAndClientIdAndStatus(1L, 1L, AdvertStatus.OPEN);

        assertThat(expectedAdvert).hasSize(1);
        assertThat(expectedAdvert.get(0).getCar().getId()).isEqualTo(1);
        assertThat(expectedAdvert.get(0).getClient().getId()).isEqualTo(1);
        assertThat(expectedAdvert.get(0).getPrice()).isEqualTo(200);
        assertThat(expectedAdvert.get(0).getStatus()).isEqualTo(AdvertStatus.OPEN);
    }

    @Test
    @DataSet(value = "advert-by-carid-clientid-status.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindById() {

        CarEntity carEntity = new CarEntity("12-12", 2012, "green", "x5", "bmw");
        carEntity.setId(1L);
        ClientEntity clientEntity = new ClientEntity("andrey", "sevruk", "0937746730");
        clientEntity.setId(1L);

        AdvertEntity advertEntity = new AdvertEntity(carEntity, clientEntity, null, 200, AdvertStatus.OPEN);

        AdvertEntity advertById = dao.findById(1L);

        assertThat(advertById).isEqualToComparingOnlyGivenFields(advertEntity);
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