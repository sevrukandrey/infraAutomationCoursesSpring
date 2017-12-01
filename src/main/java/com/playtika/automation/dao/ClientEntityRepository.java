package com.playtika.automation.dao;

import com.playtika.automation.dao.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientEntityRepository extends JpaRepository<ClientEntity, Long> {
    List<ClientEntity> findByPhoneNumber(String ownerContacts);
}
