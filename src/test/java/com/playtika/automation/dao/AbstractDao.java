package com.playtika.automation.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@DBUnit(cacheConnection = false)
public class AbstractDao<D> {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    protected TestEntityManager em;

    @Autowired
    protected AdvertEntityRepository advertDao;

    @Autowired
    protected CarEntityRepository carDao;

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
}
