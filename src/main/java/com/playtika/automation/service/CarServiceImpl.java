package com.playtika.automation.service;

import com.playtika.automation.dao.AdvertEntityRepository;
import com.playtika.automation.dao.CarEntityRepository;
import com.playtika.automation.dao.ClientEntityRepository;
import com.playtika.automation.dao.DealEntityRepository;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.dao.entity.DealEntity;
import com.playtika.automation.domain.*;
import com.playtika.automation.web.exceptions.AdvertClosedException;
import com.playtika.automation.web.exceptions.AdvertNotFoundException;
import com.playtika.automation.web.exceptions.DealNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static com.playtika.automation.domain.AdvertStatus.CLOSED;
import static com.playtika.automation.domain.AdvertStatus.OPEN;
import static com.playtika.automation.domain.DealStatus.*;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

@Service
public class CarServiceImpl implements CarService {

    private CarEntityRepository carEntityRepository;
    private ClientEntityRepository clientEntityRepository;
    private AdvertEntityRepository advertEntityRepository;
    private DealEntityRepository dealEntityRepository;

    public CarServiceImpl(CarEntityRepository carEntityRepository,
                          ClientEntityRepository clientEntityRepository,
                          AdvertEntityRepository advertEntityRepository,
                          DealEntityRepository dealEntityRepository) {
        this.carEntityRepository = carEntityRepository;
        this.clientEntityRepository = clientEntityRepository;
        this.advertEntityRepository = advertEntityRepository;
        this.dealEntityRepository = dealEntityRepository;
    }

    @Transactional
    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        CarEntity carEntity = getOrCreateCarEntity(car);

        ClientEntity clientEntity = getOrCreateClientEntity(ownerContacts);

        AdvertEntity advertEntity = getOrCreateAdvertEntity(carEntity, clientEntity, price);


        return advertEntity.getCar().getId();
    }

    @Override
    public List<CarSaleInfo> getAllCars() {
        return advertEntityRepository.findByStatus(OPEN)
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
        return advertEntityRepository.findByCarIdAndStatus(carId, OPEN)
            .stream()
            .findFirst()
            .map(this::toSaleInfo);
    }

    @Override
    public void rejectDeal(long dealId) {
        DealEntity dealEntity = dealEntityRepository.findById(dealId);

        if (dealEntity == null) throw new DealNotFoundException(String.format("Deal with id %s not found", dealId));

        dealEntity.setStatus(DealStatus.REJECTED);

        dealEntityRepository.save(dealEntity);
    }

    @Override
    public long putCarToSale(CarOnSaleRequest carOnSaleRequest) {
        CarEntity carEntity = getOrCreateCarEntity(carOnSaleRequest.getCar());
        ClientEntity clientEntity = getOrCreateClientEntity(carOnSaleRequest.getClient());

        return getOrCreateAdvertEntity(carEntity, clientEntity, carOnSaleRequest.getPrice())
                .getId();
    }

    @Override
    @Transactional
    public long chooseBestDealByAdvertId(long advertId) {

        AdvertEntity advertEntity = isAdvertExist(advertId);

        List<DealEntity> allDealByAdvertId = dealEntityRepository.findByAdvertId(advertId);

        DealEntity dealWithHigherPrice = getDealWithHigherPrice(allDealByAdvertId);
        dealWithHigherPrice.setStatus(APPROVED);

        rejectDealWithLowerPrice(allDealByAdvertId);

        dealEntityRepository.save(allDealByAdvertId);

        closeAndSaveAdvert(advertEntity, dealWithHigherPrice);

        return dealWithHigherPrice.getId();
    }

    @Override
    public long createDeal(DealRequest dealRequest, long advertId) {
        AdvertEntity advertEntity = isAdvertExist(advertId);

        ClientEntity clientEntity= getOrCreateClientEntity(dealRequest.getClient());

        return getOrCreateDealEntity(clientEntity, dealRequest.getPrice(), advertEntity);
    }

    @Override
    public long getAdvertIdByCarId(long carId) {
        AdvertEntity advertEntity = advertEntityRepository.findByCarId(carId);

        if(advertEntity == null)
            throw new AdvertNotFoundException(String.format("Advert with carId %s not found", carId));

        return advertEntity.getId();


    }

    private long getOrCreateDealEntity(ClientEntity clientEntity, double price, AdvertEntity advertEntity) {
        return dealEntityRepository.findByAdvertIdAndBuyerIdAndPriceAndStatus(advertEntity.getId(), clientEntity.getId(), price, ACTIVE)
            .stream()
            .findFirst()
            .orElseGet(() -> persistDealEntity(clientEntity, price, advertEntity)).getId();
    }

    private DealEntity persistDealEntity(ClientEntity clientEntity, double price, AdvertEntity advertEntity) {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setStatus(DealStatus.ACTIVE);
        dealEntity.setBuyer(clientEntity);
        dealEntity.setAdvert(advertEntity);
        dealEntity.setPrice(price);
        return dealEntityRepository.save(dealEntity);
    }


    private CarEntity getOrCreateCarEntity(Car car) {
        return carEntityRepository.findByPlateNumber(car.getPlateNumber())
            .stream()
            .findFirst()
            .orElseGet(() -> saveCarAndGetCarEntity(car));
    }

    private AdvertEntity persistAdvertEntity(double price, CarEntity carEntity, ClientEntity clientEntity) {
        List<AdvertEntity> byCarIdAndStatus = advertEntityRepository.findByCarIdAndClientIdAndStatus(carEntity.getId(),
                clientEntity.getId(), OPEN);

        return byCarIdAndStatus.isEmpty() ? createNewAdvert(price, carEntity, clientEntity)
                : updateAdvert(byCarIdAndStatus.get(0), price);
    }

    private AdvertEntity updateAdvert(AdvertEntity advertEntity, double price) {
        advertEntity.setPrice(price);
        return advertEntityRepository.save(advertEntity);
    }

    private AdvertEntity createNewAdvert(Double price, CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCar(carEntity);
        advertEntity.setPrice(price);
        advertEntity.setClient(clientEntity);
        advertEntity.setStatus(OPEN);
        return advertEntityRepository.save(advertEntity);
    }

    private ClientEntity getOrCreateClientEntity(String ownerContacts) {
        return clientEntityRepository.findByPhoneNumber(ownerContacts)
            .stream()
            .findFirst()
            .orElseGet(() -> saveClientAndGetClientEntity(ownerContacts));
    }

    private ClientEntity getOrCreateClientEntity(Client client) {
        return clientEntityRepository.findByPhoneNumber(client.getPhoneNumber())
            .stream()
            .findFirst()
            .orElseGet(() -> saveClientAndGetClientEntity(client));
    }

    private ClientEntity saveClientAndGetClientEntity(String ownerContacts) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setPhoneNumber(ownerContacts);

        return clientEntityRepository.save(clientEntity);
    }

    private ClientEntity saveClientAndGetClientEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setPhoneNumber(client.getPhoneNumber());
        clientEntity.setName(client.getName());
        clientEntity.setSurname(client.getSureName());

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

    private AdvertEntity getOrCreateAdvertEntity(CarEntity carEntity, ClientEntity clientEntity, double price) {
        return advertEntityRepository.findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(),
                price, OPEN)
                .stream()
                .findFirst()
                .orElseGet(() -> persistAdvertEntity(price, carEntity, clientEntity));
    }

    private void closeAndSaveAdvert(AdvertEntity advertEntity, DealEntity dealWithHigherPrice) {
        advertEntity.setStatus(CLOSED);
        advertEntity.setDealId(dealWithHigherPrice.getId());

        advertEntityRepository.save(advertEntity);
    }

    private void rejectDealWithLowerPrice(List<DealEntity> allDealByAdvertId) {
        allDealByAdvertId.stream()
                .filter(dealEntity -> dealEntity.getStatus() != APPROVED)
                .forEach(dealEntity -> dealEntity.setStatus(REJECTED));
    }

    private DealEntity getDealWithHigherPrice(List<DealEntity> allDealByAdvertId) {
        return allDealByAdvertId
                .stream()
                .filter(dealEntity -> dealEntity.getStatus() == ACTIVE)
                .max(comparingDouble(DealEntity::getPrice))
                .orElseThrow(() -> new DealNotFoundException("There is no Deal with Status Active"));
    }

    private AdvertEntity isAdvertExist(long advertId) {
        AdvertEntity advertEntity = advertEntityRepository.findById(advertId);

        if(advertEntity == null)
            throw new AdvertNotFoundException(String.format("Advert with is %s not found", advertId));
        if(advertEntity.getStatus() == AdvertStatus.CLOSED) {
            throw  new AdvertClosedException(String.format("Advert with id %s is already closed", advertId));
        }
        return advertEntity;
    }

}
