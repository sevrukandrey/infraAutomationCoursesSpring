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

        List<CarEntity> byPlateNumber = carDao.findByPlateNumber("12-12");

        assertThat(byPlateNumber).hasSize(1);
        assertThat(byPlateNumber.get(0).getId()).isEqualTo(carEntity.getId());
        assertThat(byPlateNumber.get(0).getPlateNumber()).isEqualTo(carEntity.getPlateNumber());
        assertThat(byPlateNumber.get(0).getBrand()).isEqualTo(carEntity.getBrand());
        assertThat(byPlateNumber.get(0).getColor()).isEqualTo(carEntity.getColor());
        assertThat(byPlateNumber.get(0).getModel()).isEqualTo(carEntity.getModel());

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