package com.playtika.automation.dao;

import javax.persistence.EntityManager;
import java.util.Map;

public class CarEntityStatisticRepoImpl implements CarEntityStatisticRepo {
    private  final EntityManager entityManager;

    public CarEntityStatisticRepoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Map<Integer, Integer> getSoldCarPerYear() {
        return null;
    }
}
