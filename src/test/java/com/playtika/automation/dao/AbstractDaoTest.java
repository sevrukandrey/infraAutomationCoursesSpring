package com.playtika.automation.dao;

import com.github.database.rider.core.DBUnitRule;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest

public abstract class AbstractDaoTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    void assertAdvertEntitiesAreEqual(AdvertEntity actual, AdvertEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertCarEntitiesIsEqual(actual.getCar(), expected.getCar());
        assertClientEntitiesAreEqual(actual.getClient(), expected.getClient());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
    }

    private void assertClientEntitiesAreEqual(ClientEntity actual, ClientEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
    }

    private void assertCarEntitiesIsEqual(CarEntity actual, CarEntity expected) {
        assertThat(actual.getPlateNumber()).isEqualTo(expected.getPlateNumber());
        assertThat(actual.getBrand()).isEqualTo(expected.getBrand());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getModel()).isEqualTo(expected.getModel());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());
    }
}
