package com.playtika.automation.service;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.dao.support.DataAccessUtils.requiredUniqueResult;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public long addCar(Car car, double price, String ownerContacts) {

        CarEntity carEntity = getOrCreateCarEntity(car);

        ClientEntity clientEntity = getOrCreateClientEntity(ownerContacts);

        return persistAdvertEntity(price, carEntity, clientEntity);
    }


    @Override
    public List<CarSaleInfo> getAllCars() {

        return Collections.emptyList();
    }

    @Override
    public void deleteCar(long id) {
        manager.createQuery("delete from car where id=:id", CarEntity.class)
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long carId) {

        List<CarEntity> resultList = manager.createQuery("select c from car c join c.clientEntities join c.advertEntities where c.id=:carId",
            CarEntity.class)
            .setParameter("carId", carId).getResultList();

        CarEntity carEntity = singleResult(resultList);

        if (carEntity == null) {
            return Optional.empty();
        }

        String phoneNumber = carEntity.getClientEntities().get(0).getPhoneNumber();
        Double price = carEntity.getAdvertEntities().get(0).getPrice();

        return Optional.of(new SaleInfo(phoneNumber, price));
    }


    private CarEntity getOrCreateCarEntity(Car car) {
        List<CarEntity> carEntities = manager.createQuery("select c from car c where c.plateNumber=:plateNumber", CarEntity.class)
            .setParameter("plateNumber", car.getPlateNumber()).getResultList();

        CarEntity carEntity = singleResult(carEntities);
        if (carEntity == null) {
            carEntity = saveCarAndGetCarEntity(car);
        }
        return carEntity;
    }

    private long persistAdvertEntity(double price, CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();

        advertEntity.setCarId(carEntity.getId());
        advertEntity.setPrice(price);
        advertEntity.setSellerId(clientEntity.getId());
        manager.persist(advertEntity);
        return advertEntity.getId();
    }

    private ClientEntity getOrCreateClientEntity(String ownerContacts) {

        List<ClientEntity> clientEntities = manager.createQuery("select c from client c where c.phoneNumber=:phoneNumber",
            ClientEntity.class)
            .setParameter("phoneNumber", ownerContacts)
            .getResultList();

        ClientEntity clientEntity = singleResult(clientEntities);
        if (clientEntity == null) {
            clientEntity = saveClientAndGetClientEntity(ownerContacts);
        }
        return clientEntity;
    }

    private ClientEntity saveClientAndGetClientEntity(String ownerContacts) {
        ClientEntity clientEntity = new ClientEntity();

        clientEntity.setName("ClientName");
        clientEntity.setSurname("ClientSurname");
        clientEntity.setPhoneNumber(ownerContacts);

        manager.persist(clientEntity);

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
