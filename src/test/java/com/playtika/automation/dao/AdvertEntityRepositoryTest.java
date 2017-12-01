package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class AdvertEntityRepositoryTest {

    @Autowired
    AdvertEntityRepository advertEntityRepository;


    @Test
    @DataSet("")
    public void shouldFindAdvertByStatus(){
          assertThat(advertEntityRepository.findByStatus(AdvertStatus.OPEN)).hasSize(1);
    }


    @Test
    public void shouldFindAdvertByCarIdAndStatus(){

    }
    @Test
    public void shouldDeleteByCarId(){

    }



}