package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Test;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class ClientRepositoryTest extends AbstractDaoTest {

    @Test
    @DataSet(value = "find-by-phone-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByPhoneNumber() {
        ClientEntity clientEntity = constructClientEntity();

        List<ClientEntity> byPhoneNumber = clientDao.findByPhoneNumber("0937746730");

        assertThat(byPhoneNumber, hasSize(1));
        assertThat(byPhoneNumber.get(0), samePropertyValuesAs(clientEntity));
    }

    @Test
    @DataSet(
            value = "empty-client.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    @ExpectedDataSet("add-client.xml")
    @Commit
    public void shouldSaveCar(){
        clientDao.save(constructClientEntity());
    }
}