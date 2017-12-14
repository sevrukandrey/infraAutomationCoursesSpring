package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.CarEntity;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest extends AbstractDaoTest {

    @Test
    @DataSet(
        value = "find-by-plate-number.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByPlateNumber() {
        CarEntity carEntity = constructCarEntity();

        List<CarEntity> resultCar = carDao.findByPlateNumber("12-12");

        assertThat(resultCar).hasSize(1);
        assertCarEntitiesIsEqual(resultCar.get(0), carEntity);
    }

    @Test
    @DataSet(
        value = "empty-car.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("add-car.xml")
    @Commit
    public void shouldSaveCar() {
        carDao.save(constructCarEntity());
    }
}