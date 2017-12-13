package com.playtika.automation.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories({"com.playtika.automation.dao.jpa", "com.playtika.automation.dao.entity"})
//@EnableCouchbaseRepositories({"com.playtika.automation.dao.couchbase", "com.playtika.automation.dao.entity"})
public class RepositoryConfiguration {
}
