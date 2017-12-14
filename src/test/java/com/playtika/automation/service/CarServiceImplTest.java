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

    private Car car;
    private SaleInfo saleInfo;
    private AdvertEntity advertEntity;
    private CarEntity carEntity;
    private ClientEntity clientEntity;

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
        saleInfo = new SaleInfo("093", 1000.0);
        carEntity = constructCarEntity(car);
        clientEntity = constructClientEntity();
        advertEntity = constructAdvertEntity(carEntity, clientEntity);
    }

    @Test
    public void shouldReturnAllCars() {
        when(advertEntityRepository.findByStatus(OPEN)).thenReturn(singletonList(advertEntity));

        List<CarSaleInfo> allCarsResult = carService.getAllCars();

        assertThat(allCarsResult).hasSize(1);
        CarSaleInfo carSaleInfo = allCarsResult.get(0);
        assertThat(carSaleInfo.getId()).isEqualTo(1);
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
        carService.deleteCar(1L);

        verify(advertEntityRepository).deleteByCarId(1L);
    }

    @Test
    public void shouldGetCarSaleInfoById() {

        when(advertEntityRepository.findByCarIdAndStatus(1L, OPEN)).thenReturn(singletonList(advertEntity));

        assertThat(carService.getSaleInfo(1L))
            .isEqualTo(of(new SaleInfo("093", 1000.0)));
    }

    @Test
    public void shouldAddCar() {

        when(carEntityRepository.findByPlateNumber(car.getPlateNumber())).thenReturn(emptyList());
        when(clientEntityRepository.findByPhoneNumber(saleInfo.getOwnerContacts())).thenReturn(emptyList());

        when(carEntityRepository.save(any(CarEntity.class))).thenReturn(carEntity);
        when(clientEntityRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);
        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        long id = carService.addCar(car, 100, "093");

        assertThat(id).isEqualTo(1);
    }

    @Test
    public void shouldNotSaveClientAndCarIfSameExist() {

        when(carEntityRepository.findByPlateNumber(car.getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(saleInfo.getOwnerContacts())).thenReturn(singletonList(clientEntity));
        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        carService.addCar(car, 1000.0, "093");

        verify(clientEntityRepository, never()).save(any(ClientEntity.class));
        verify(carEntityRepository, never()).save(any(CarEntity.class));
    }

    private AdvertEntity constructAdvertEntity(CarEntity carEntity, ClientEntity clientEntity) {

        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setId(1L);
        advertEntity.setCar(carEntity);
        advertEntity.setStatus(OPEN);
        advertEntity.setClient(clientEntity);
        advertEntity.setPrice(1000.0);
        advertEntity.setDeal(null);

        return advertEntity;
    }

    private ClientEntity constructClientEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(2L);
        clientEntity.setName("093");
        clientEntity.setPhoneNumber("093");
        return clientEntity;
    }

    private CarEntity constructCarEntity(Car car) {

        CarEntity carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setModel(car.getModel());
        carEntity.setYear(car.getYear());
        carEntity.setBrand(car.getBrand());
        carEntity.setPlateNumber(car.getPlateNumber());
        carEntity.setColor(car.getColor());
        return carEntity;
    }

}
