package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientRepositoryTest extends AbstractDaoTest {

    @Test
    @DataSet(value = "find-by-phone-number.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    public void shouldFindByPhoneNumber() {
        ClientEntity clientEntity = constructClientEntity();

        List<ClientEntity> resultClientEntity = clientDao.findByPhoneNumber("0937746730");

        assertThat(resultClientEntity).hasSize(1);
        assertClientEntitiesAreEqual(resultClientEntity.get(0), clientEntity);
    }

    @Test
    @DataSet(
        value = "empty-client.xml",
        useSequenceFiltering = false,
        disableConstraints = true)
    @ExpectedDataSet("add-client.xml")
    @Commit
    public void shouldSaveCar() {
        clientDao.save(constructClientEntity());
    }
}