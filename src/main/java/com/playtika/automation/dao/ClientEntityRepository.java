package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.ClientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ClientEntityRepository extends CrudRepository {
    List<ClientEntity> findByPhoneNumber(String ownerContacts);
}
