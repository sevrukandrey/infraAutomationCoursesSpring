package com.playtika.automation.dao;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.dao.entity.ClientEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DealRepositoryTest extends AbstractDaoTest {
    @Autowired
    DealEntityRepository dao;

    @Test
    @DataSet(value = "update-deal-with-reject-status.xml",
            useSequenceFiltering = false,
            disableConstraints = true)
    @ExpectedDataSet("expected-deal-after-reject.xml")
    @Commit
    public void shouldRejectDealById() {
       dao.updateDealWithRejectStatus(1);
    }

}