package com.playtika.automation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.playtika.automation.dao.jpa", "com.playtika.automation.dao.entity"})
@EnableCouchbaseRepositories({"com.playtika.automation.dao.couchbase", "com.playtika.automation.dao.entity"})
public class CarsApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarsApplication.class, args);

	}


}
