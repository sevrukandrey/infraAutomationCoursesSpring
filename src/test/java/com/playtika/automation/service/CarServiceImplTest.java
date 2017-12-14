package com.playtika.automation.service;

import com.playtika.automation.dao.AdvertEntityRepository;
import com.playtika.automation.dao.CarEntityRepository;
import com.playtika.automation.dao.ClientEntityRepository;
import com.playtika.automation.dao.entity.AdvertEntity;
import com.playtika.automation.dao.entity.CarEntity;
import com.playtika.automation.dao.entity.ClientEntity;
import com.playtika.automation.domain.Car;
import com.playtika.automation.domain.CarSaleInfo;
import com.playtika.automation.domain.SaleInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.playtika.automation.domain.AdvertStatus.OPEN;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceImplTest {

    private double price = 1000.0;
    private String owner = "093";
    private long carId = 1L;
    private long clientId = 2L;
    private long advertId = 3L;
    private Car car;
    private SaleInfo saleInfo;

    private CarService carService;

    @Mock
    private CarEntityRepository carEntityRepository;

    @Mock
    private ClientEntityRepository clientEntityRepository;

    @Mock
    private AdvertEntityRepository advertEntityRepository;

    @Before
    public void init() {
        carService = new CarServiceImpl(carEntityRepository, clientEntityRepository, advertEntityRepository);
        car = new Car("ford", "fiesta", "12-12", "green", 2016);
        saleInfo = new SaleInfo(owner, price);
    }

    @Test
    public void shouldReturnAllCars() {
        AdvertEntity advertEntity = constructAdvertEntity(car, saleInfo);

        when(advertEntityRepository.findByStatus(OPEN)).thenReturn(singletonList(advertEntity));

        List<CarSaleInfo> allCarsResult = carService.getAllCars();

        assertThat(allCarsResult).hasSize(1);
        CarSaleInfo carSaleInfo = allCarsResult.get(0);
        assertThat(carSaleInfo.getId()).isEqualTo(advertId);
        assertThat(carSaleInfo.getCar()).isEqualTo(car);
        assertThat(carSaleInfo.getSaleInfo()).isEqualTo(saleInfo);
    }

    @Test
    public void shouldReturnEmptyListIfThereAreNoCarsInDB() {
        when(advertEntityRepository.findByStatus(OPEN)).thenReturn(emptyList());

        assertThat(carService.getAllCars())
            .isEmpty();
    }

    @Test
    public void shouldDeleteCarById() {
        carService.deleteCar(carId);

        verify(advertEntityRepository).deleteByCarId(carId);
    }

    @Test
    public void shouldGetCarSaleInfoById() {
        AdvertEntity advertEntity = constructAdvertEntity(car, saleInfo);

        when(advertEntityRepository.findByCarIdAndStatus(carId, OPEN)).thenReturn(singletonList(advertEntity));

        assertThat(carService.getSaleInfo(carId))
            .isEqualTo(of(new SaleInfo(owner, price)));
    }

    @Test
    public void shouldAddCar() {
        CarEntity carEntity = constructCarEntity();
        ClientEntity clientEntity = constructClientEntity();
        AdvertEntity advertEntity = constructAdvert(carEntity, clientEntity);

        when(carEntityRepository.findByPlateNumber(car.getPlateNumber())).thenReturn(emptyList());
        when(clientEntityRepository.findByPhoneNumber(owner)).thenReturn(emptyList());

        when(carEntityRepository.save(any(CarEntity.class))).thenReturn(carEntity);
        when(clientEntityRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        long id = carService.addCar(car, price, owner);

        assertThat(id).isEqualTo(carId);
    }

    @Test
    public void shouldNotSaveClientAndCarIfSameExist() {
        ClientEntity clientEntity = constructClientEntity();
        CarEntity carEntity = constructCarEntity();

        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setCar(carEntity);
        advertEntity.setClient(clientEntity);
        advertEntity.setPrice(price);
        advertEntity.setStatus(OPEN);

        when(carEntityRepository.findByPlateNumber(carEntity.getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(clientEntity.getPhoneNumber())).thenReturn(singletonList(clientEntity));
        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        carService.addCar(car, price, owner);

        verify(clientEntityRepository, never()).save(any(ClientEntity.class));
        verify(carEntityRepository, never()).save(any(CarEntity.class));
    }

    private AdvertEntity constructAdvert(CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setId(advertId);
        advertEntity.setClient(clientEntity);
        advertEntity.setPrice(price);
        advertEntity.setCar(carEntity);
        advertEntity.setStatus(OPEN);
        return advertEntity;
    }

    private ClientEntity constructClientEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setPhoneNumber(owner);
        return clientEntity;
    }

    private CarEntity constructCarEntity() {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(carId);
        carEntity.setColor(car.getColor());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setBrand(car.getBrand());
        carEntity.setYear(car.getYear());
        carEntity.setModel(car.getModel());
        return carEntity;
    }

    private AdvertEntity constructAdvertEntity(Car car, SaleInfo saleInfo) {

        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setId(advertId);
        advertEntity.setCar(constructCarEntity(car));
        advertEntity.setStatus(OPEN);
        advertEntity.setClient(constructClientEntity(saleInfo.getOwnerContacts()));
        advertEntity.setPrice(saleInfo.getPrice());
        advertEntity.setDeal(null);

        return advertEntity;
    }

    private ClientEntity constructClientEntity(String owner) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(clientId);
        clientEntity.setName(owner);
        clientEntity.setPhoneNumber("093");
        return clientEntity;
    }

    private CarEntity constructCarEntity(Car car) {

        CarEntity carEntity = new CarEntity();
        carEntity.setId(carId);
        carEntity.setModel(car.getModel());
        carEntity.setYear(car.getYear());
        carEntity.setBrand(car.getBrand());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setColor(car.getColor());
        return carEntity;
    }

}
