package com.playtika.automation.service;

import com.playtika.automation.dao.AdvertEntityRepository;
import com.playtika.automation.dao.CarEntityRepository;
import com.playtika.automation.dao.ClientEntityRepository;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.AdvertStatus;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CarServiceImpl implements CarService {

    private CarEntityRepository carEntityRepository;
    private ClientEntityRepository clientEntityRepository;
    private AdvertEntityRepository advertEntityRepository;

    public CarServiceImpl(CarEntityRepository carEntityRepository, ClientEntityRepository clientEntityRepository, AdvertEntityRepository advertEntityRepository) {
        this.carEntityRepository = carEntityRepository;
        this.clientEntityRepository = clientEntityRepository;
        this.advertEntityRepository = advertEntityRepository;
    }

    @Transactional
    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        CarEntity carEntity = getOrCreateCarEntity(car);

        ClientEntity clientEntity = getOrCreateClientEntity(ownerContacts);

        AdvertEntity advertEntity = persistAdvertEntity(price, carEntity, clientEntity);


        return advertEntity.getCar().getId();
    }

    @Override
    public List<CarSaleInfo> getAllCars() {
        return advertEntityRepository.findByStatus(AdvertStatus.OPEN)
            .stream()
            .map(this::toCarSaleInfo)
            .collect(toList());
    }

    @Override
    @Transactional
    public void deleteCar(long carId) {
        advertEntityRepository.deleteByCarId(carId);
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long carId) {
        return advertEntityRepository.findByCarIdAndStatus(carId, AdvertStatus.OPEN)
            .stream()
            .findFirst()
            .map(this::toSaleInfo);
    }

    private CarEntity getOrCreateCarEntity(Car car) {
        return carEntityRepository.findByPlateNumber(car.getPlateNumber())
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
        return advertEntityRepository.save(advertEntity);
    }

    private ClientEntity getOrCreateClientEntity(String ownerContacts) {
        return clientEntityRepository.findByPhoneNumber(ownerContacts)
            .stream()
            .findFirst()
            .orElseGet(() -> saveClientAndGetClientEntity(ownerContacts));
    }

    private ClientEntity saveClientAndGetClientEntity(String ownerContacts) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setPhoneNumber(ownerContacts);

        return clientEntityRepository.save(clientEntity);
    }

    private CarEntity saveCarAndGetCarEntity(Car car) {
        CarEntity carEntity = new CarEntity();
        carEntity.setModel(car.getModel());
        carEntity.setColor(car.getColor());
        carEntity.setBrand(car.getBrand());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setYear(car.getYear());
        return carEntityRepository.save(carEntity);
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
