package com.playtika.automation.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@DBUnit
public abstract class AbstractDaoTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Qualifier("jpaAdvertEntityRepository")
    @Autowired
    protected AdvertEntityRepository advertDao;

    @Qualifier("jpaCarEntityRepository")
    @Autowired
    protected CarEntityRepository carDao;

    @Qualifier("jpaClientEntityRepository")
    @Autowired
    protected ClientEntityRepository clientDao;

    CarEntity constructCarEntity() {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setBrand("bmw");
        carEntity.setModel("x5");
        carEntity.setColor("green");
        carEntity.setPlateNumber("12-12");
        carEntity.setYear(2012);
        return carEntity;
    }

    ClientEntity constructClientEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName("andrey");
        clientEntity.setSurname("sevruk");
        clientEntity.setPhoneNumber("0937746730");
        return clientEntity;
    }

    AdvertEntity constructAdvertEntity(CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setId(1L);
        advertEntity.setStatus(AdvertStatus.OPEN);
        advertEntity.setPrice(200.0);
        advertEntity.setClient(clientEntity);
        advertEntity.setCar(carEntity);
        return advertEntity;
    }

    void assertAdvertEntitiesAreEqual(AdvertEntity actual, AdvertEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertCarEntitiesIsEqual(actual.getCar(), expected.getCar());
        assertClientEntitiesAreEqual(actual.getClient(), expected.getClient());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
    }

    void assertClientEntitiesAreEqual(ClientEntity actual, ClientEntity expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSurname()).isEqualTo(expected.getSurname());
    }

    void assertCarEntitiesIsEqual(CarEntity actual, CarEntity expected) {
        assertThat(actual.getPlateNumber()).isEqualTo(expected.getPlateNumber());
        assertThat(actual.getBrand()).isEqualTo(expected.getBrand());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getModel()).isEqualTo(expected.getModel());
        assertThat(actual.getColor()).isEqualTo(expected.getColor());
    }
}
