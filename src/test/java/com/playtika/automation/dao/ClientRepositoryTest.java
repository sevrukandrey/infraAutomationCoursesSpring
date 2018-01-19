package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientRepositoryTest extends AbstractDaoTest {
    @Autowired
    ClientEntityRepository dao;

    @Test
    @DataSet(value = "find-by-phone-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldFindByPhoneNumber() {
        ClientEntity clientEntity = new ClientEntity("andrey", "sevruk", "0937746730");
        clientEntity.setId(1L);

        List<ClientEntity> resultClientEntity = dao.findByPhoneNumber("0937746730");

        assertThat(resultClientEntity).hasSize(1);
        assertThat(resultClientEntity.get(0)).isEqualToComparingFieldByField(clientEntity);
    }


    @Test
    @DataSet(
            value = "find-by-phone-number.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    public void shouldReturnEmptyListIfClientByPhoneNumberNotFound() {
        List<ClientEntity> client = dao.findByPhoneNumber("12-99");
        assertThat(client).isEmpty();
    }
}