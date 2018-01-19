package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.dao.entity.CarEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CarRepositoryTest extends AbstractDaoTest {

    @Autowired
    CarEntityRepository dao;

    @Test
    @DataSet(
            value = "find-by-plate-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByPlateNumber() {
        CarEntity carEntity = new CarEntity("12-12", 2012, "green", "x5", "bmw");
        carEntity.setId(1L);

        List<CarEntity> resultCar = dao.findByPlateNumber("12-12");

        assertThat(resultCar).hasSize(1);
        assertThat(resultCar.get(0)).isEqualToComparingFieldByField(carEntity);
    }

    @Test
    @DataSet(
            value = "find-by-plate-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldReturnEmptyListIfCarByPlateNumberNotFound() {
        List<CarEntity> resultCar = dao.findByPlateNumber("12-99");
        assertThat(resultCar).isEmpty();
    }

}