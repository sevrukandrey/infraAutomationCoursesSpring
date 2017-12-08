package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.domain.AdvertStatus;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


@DataSet(cleanBefore = true)
public class AdvertRepositoryTest extends AbstractDaoTest<AdvertRepositoryTest> {

    @Test
    @DataSet(value = "expected-advert.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByStatus() {
        List<AdvertEntity> findByStatus = dao.findByStatus(AdvertStatus.OPEN);

        assertThat(findByStatus, hasSize(1));
    }


    @Test
    @DataSet(value = "expected-advert.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindAdvertByCarIdAndStatus() {

        List<AdvertEntity> findByCarIdAndStatus = dao.findByCarIdAndStatus(1, AdvertStatus.OPEN);

        assertThat(findByCarIdAndStatus,hasSize(1));
    }

    @Test
    @DataSet(value = "expected-advert-for-delete.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldDeleteByCarId() {
        dao.deleteByCarId(1L);
            assertThat(dao.findAll(), hasSize(0));

    }


}