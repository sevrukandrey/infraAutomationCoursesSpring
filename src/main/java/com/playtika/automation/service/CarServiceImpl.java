package com.playtika.automation.service;

import com.playtika.automation.dao.entity.*;
import com.playtika.automation.domain.AdvertStatus;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

@Service
@Transactional
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    @PersistenceContext
    private final EntityManager manager;

    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        CarEntity carEntity = getOrCreateCarEntity(car);

        ClientEntity clientEntity = getOrCreateClientEntity(ownerContacts);

        AdvertEntity advertEntity = persistAdvertEntity(price, carEntity, clientEntity);
        return advertEntity.getCar().getId();
    }

    @Override
    public List<CarSaleInfo> getAllCars() {
        List<AdvertEntity> resultList = manager.createQuery("SELECT a from advert a " +
            "where a.status='OPEN'", AdvertEntity.class)
            .getResultList();

        return resultList
            .stream()
            .map(this::toCarSaleInfo)
            .collect(toList());
    }

    @Override
    public void deleteCar(long carId) {
        List<AdvertEntity> resultList = manager.createQuery("select a from advert a join a.car c " +
            "where c.id=:carId", AdvertEntity.class)
            .setParameter("carId", carId)
            .getResultList();

        resultList.forEach(manager::remove);
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long carId) {
        List<AdvertEntity> resultList = manager.createQuery("select a from advert a join a.car c " +
            "where c.id=:carId AND a.status = 'OPEN'", AdvertEntity.class)
            .setParameter("carId", carId)
            .getResultList();

        AdvertEntity advertEntity = singleResult(resultList);

        return ofNullable(advertEntity).map(advert ->
            new SaleInfo(advert.getClientId().getPhoneNumber(), advert.getPrice()));

    }

    private CarEntity getOrCreateCarEntity(Car car) {
        List<CarEntity> carEntities = manager.createQuery("select c from car c " +
            "where c.plateNumber=:plateNumber", CarEntity.class)
            .setParameter("plateNumber", car.getPlateNumber())
            .getResultList();

        CarEntity carEntity = singleResult(carEntities);
        if (carEntity == null) {
            carEntity = saveCarAndGetCarEntity(car);
        }
        return carEntity;
    }

    private AdvertEntity persistAdvertEntity(double price, CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCar(carEntity);
        advertEntity.setPrice(price);
        advertEntity.setClientId(clientEntity);
        advertEntity.setStatus(AdvertStatus.OPEN);

        manager.persist(advertEntity);
        return advertEntity;
    }

    private ClientEntity getOrCreateClientEntity(String ownerContacts) {
        List<ClientEntity> clientEntities = manager.createQuery("select c from client c " +
                "where c.phoneNumber=:phoneNumber", ClientEntity.class)
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
        clientEntity.setPhoneNumber(ownerContacts);

        manager.persist(clientEntity);

        return clientEntity;
    }

    private CarEntity saveCarAndGetCarEntity(Car car) {
        CarEntity carEntity = new CarEntity();
        carEntity.setModel(car.getModel());
        carEntity.setColor(car.getColor());
        carEntity.setBrand(car.getBrand());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setYear(car.getYear());

        manager.persist(carEntity);

        return carEntity;
    }

    private CarSaleInfo toCarSaleInfo(AdvertEntity advert) {
        return new CarSaleInfo(advert.getId(), toCar(advert.getCar()), toSaleInfo(advert));
    }

    private SaleInfo toSaleInfo(AdvertEntity advert) {
        return new SaleInfo(advert.getClientId().getPhoneNumber(), advert.getPrice());
    }

    private Car toCar(CarEntity carEntity) {
        return new Car(carEntity.getBrand(),
            carEntity.getModel(),
            carEntity.getPlateNumber(),
            carEntity.getColor(),
            carEntity.getYear());
    }
}
