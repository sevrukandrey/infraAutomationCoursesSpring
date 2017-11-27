package com.playtika.automation.service;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Map<Long, CarSaleInfo> cars = new ConcurrentHashMap<>();

    @PersistenceContext
    private EntityManager manager;

    @Override
    public long addCar(Car car, double price, String ownerContacts) {

        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCarId(saveCarAndGetCarEntity(car).getId());
        advertEntity.setPrice(price);
        advertEntity.setSellerId(getSaveSellerAndGetSellerEntity(ownerContacts).getId());

        manager.persist(advertEntity);

        return advertEntity.getId();
    }


    @Override
    public List<CarSaleInfo> getAllCars() {

return Collections.emptyList();
    }

    @Override
    public void deleteCar(long id) {
        manager.createQuery("delete from CarEntity where id=:id", CarEntity.class)
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long carId) {

        List<CarEntity> resultList = manager.createQuery("select c from car c join c.clientEntities where c.id=:id", CarEntity.class)
                .setParameter("id", id)
                .getResultList();

        resultList.get(0).getClientEntities().get(0).getPhoneNumber();


        manager.createQuery("select * from ");


        return Optional.empty();
    }

    private ClientEntity getSaveSellerAndGetSellerEntity(String ownerContacts) {
        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setName("ClientName");
        clientEntity.setSurname("ClientSurname");
        clientEntity.setPhoneNumber(ownerContacts);

        manager.persist(clientEntity);
        manager.flush();

        return clientEntity;


    }

    private CarEntity saveCarAndGetCarEntity(Car car) {

        CarEntity carEntity = new CarEntity();

        carEntity.setModel(car.getModel());
        carEntity.setColor(car.getColor());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setYear(car.getYear());
        manager.persist(carEntity);

        return carEntity;
    }

}
