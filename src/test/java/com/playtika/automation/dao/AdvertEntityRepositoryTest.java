package com.playtika.automation.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AdvertEntityRepositoryTest {

    @Autowired
    AdvertEntityRepository advertEntityRepository;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    protected TestEntityManager em;

    @Autowired
    protected AdvertEntityRepository dao;


    @Test
    @DataSet("expected-advert.xml")
    public void shouldFindAdvertByStatus(){
        List<AdvertEntity> byStatus = dao.findByStatus(AdvertStatus.OPEN);
        assertThat(byStatus.get(0)).isNotNull();
    }


//    @Test
//    public void shouldFindAdvertByCarIdAndStatus(){
//
//    }
//    @Test
//    public void shouldDeleteByCarId(){
//
//    }



}