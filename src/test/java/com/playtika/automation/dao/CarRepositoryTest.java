package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.CarEntity;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class CarRepositoryTest extends AbstractDao<CarEntityRepository> {
    @Test
    @DataSet(
            value = "find-by-plate-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByPlateNumber() {
        CarEntity carEntity = constructCarEntity();

        List<CarEntity> byPlateNumber = carDao.findByPlateNumber("12-12");

        assertThat(byPlateNumber, hasSize(1));
        assertThat(byPlateNumber.get(0), samePropertyValuesAs(carEntity));
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