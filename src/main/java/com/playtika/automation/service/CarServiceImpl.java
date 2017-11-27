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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Map<Long, CarSaleInfo> cars = new ConcurrentHashMap<>();

    @PersistenceContext
    private  EntityManager manager;

    @Override
    public long addCar(Car car, double price, String ownerContacts) {
        
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCarId(saveCarAndGetCarEntity(car).getId());
        advertEntity.setPrice(price);
        advertEntity.setSellerId(getSaveSellerAndGetSellerEntity(ownerContacts).getId());
        
        manager.persist(advertEntity);
        //manager.flush();

        return advertEntity.getId();
    }

   
    @Override
    public List<CarSaleInfo> getAllCars() {

return new ArrayList<>();
    }

    @Override
    public void deleteCar(long id) {
        manager.createQuery("delete from CarEntity where id=:id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public Optional<SaleInfo> getSaleInfo(long id) {

        Query query = manager.createQuery
                ("select a from AdvertEntity a where a.carId=: id");

        query.setParameter("id", id);

        Long sellerId = query.getResultList().get(0).getSellerId();
        Double price = query.getResultList().get(0).getPrice();

        TypedQuery<ClientEntity> query2 = manager.createQuery("select * from ClientEntity where clientId=:sellerId", ClientEntity.class)
                .setParameter("sellerId", sellerId);
        String phoneNumber = query2.getResultList().get(0).getPhoneNumber();

        SaleInfo saleInfo = new SaleInfo(phoneNumber, price);


        return Optional.ofNullable(saleInfo);
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
        manager.flush();

        return carEntity;
    }

}
