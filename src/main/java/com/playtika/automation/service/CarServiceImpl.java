package com.playtika.automation.service;

import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
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

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        CarEntity carEntity = getOrCreateCarEntity(car);

        ClientEntity clientEntity = getOrCreateClientEntity(ownerContacts);

        AdvertEntity advertEntity = persistAdvertEntity(price, carEntity, clientEntity);
        return advertEntity.getCar().getId();
    }

    @Override
    public List<CarSaleInfo> getAllCars() {
        return entityManager.createQuery("SELECT a from advert a " +
                "where a.status='OPEN'", AdvertEntity.class)
                .getResultList().stream()
                .map(this::toCarSaleInfo)
                .collect(toList());
    }

    @Override
    public void deleteCar(long carId) {
        entityManager.createQuery("delete from advert a where a.car.id=:carId ")
                .setParameter("carId", carId)
                .executeUpdate();
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long carId) {
        return entityManager.createQuery("select a from advert a join a.car c " +
                "where c.id=:carId AND a.status = 'OPEN'", AdvertEntity.class)
                .setParameter("carId", carId)
                .getResultList()
                .stream()
                .findFirst()
                .map(this::toSaleInfo);
    }

    private CarEntity getOrCreateCarEntity(Car car) {
        return entityManager.createQuery("select c from car c " +
                "where c.plateNumber=:plateNumber", CarEntity.class)
                .setParameter("plateNumber", car.getPlateNumber())
                .getResultList()
                .stream()
                .findFirst()
                .orElseGet(() -> saveCarAndGetCarEntity(car));
    }

    private AdvertEntity persistAdvertEntity(double price, CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCar(carEntity);
        advertEntity.setPrice(price);
        advertEntity.setClient(clientEntity);
        advertEntity.setStatus(AdvertStatus.OPEN);

        entityManager.persist(advertEntity);
        return advertEntity;
    }

    private ClientEntity getOrCreateClientEntity(String ownerContacts) {
        return entityManager.createQuery("select c from client c " +
                "where c.phoneNumber=:phoneNumber", ClientEntity.class)
                .setParameter("phoneNumber", ownerContacts)
                .getResultList()
                .stream()
                .findFirst()
                .orElseGet(() -> saveClientAndGetClientEntity(ownerContacts));
    }

    private ClientEntity saveClientAndGetClientEntity(String ownerContacts) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setPhoneNumber(ownerContacts);

        entityManager.persist(clientEntity);

        return clientEntity;
    }

    private CarEntity saveCarAndGetCarEntity(Car car) {
        CarEntity carEntity = new CarEntity();
        carEntity.setModel(car.getModel());
        carEntity.setColor(car.getColor());
        carEntity.setBrand(car.getBrand());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setYear(car.getYear());

        entityManager.persist(carEntity);

        return carEntity;
    }

    private CarSaleInfo toCarSaleInfo(AdvertEntity advert) {
        return new CarSaleInfo(advert.getId(), toCar(advert.getCar()), toSaleInfo(advert));
    }

    private SaleInfo toSaleInfo(AdvertEntity advert) {
        return new SaleInfo(advert.getClient().getPhoneNumber(), advert.getPrice());
    }

    private Car toCar(CarEntity carEntity) {
        return new Car(carEntity.getBrand(),
                carEntity.getModel(),
                carEntity.getPlateNumber(),
                carEntity.getColor(),
                carEntity.getYear());
    }
}
