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
import com.playtika.automation.web.exceptions.DealNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.playtika.automation.domain.AdvertStatus.OPEN;
import static com.playtika.automation.domain.DealStatus.ACTIVE;
import static com.playtika.automation.domain.DealStatus.REJECTED;
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
    private DealEntity dealEntity;
    private ClientEntity clientEntity;
    private CarOnSaleRequest carOnSaleRequest;

    private CarService carService;

    @Mock
    private CarEntityRepository carEntityRepository;

    @Mock
    private ClientEntityRepository clientEntityRepository;

    @Mock
    private AdvertEntityRepository advertEntityRepository;


    @Mock
    private DealEntityRepository dealEntityRepository;

    @Captor
    private ArgumentCaptor<List<DealEntity>> dealEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<AdvertEntity> advertEntityArgumentCaptor;

    @Before
    public void init() {
        carService = new CarServiceImpl(carEntityRepository, clientEntityRepository, advertEntityRepository, dealEntityRepository);
        car = new Car("ford", "fiesta", "12-12", "green", 2016);
        Client client = new Client("Andrey", "Sevruk", "093");
        saleInfo = new SaleInfo("093", 1000.0);
        carEntity = constructCarEntity(car);
        clientEntity = constructClientEntity(client);
        advertEntity = constructAdvertEntity(carEntity, clientEntity);
        dealEntity = constructDealEntity();
        carOnSaleRequest = getCarOnSaleRequest(car, client);
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

    @Test
    public void shouldNotSaveAdvertIfSameExist() {

        when(carEntityRepository.findByPlateNumber(car.getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(saleInfo.getOwnerContacts())).thenReturn(singletonList(clientEntity));
        when(advertEntityRepository.findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(),
            clientEntity.getId(),
            1000.0,
            AdvertStatus.OPEN)).thenReturn(singletonList(advertEntity));

        carService.addCar(car, 1000.0, "093");

        verify(clientEntityRepository, never()).save(any(ClientEntity.class));
        verify(carEntityRepository, never()).save(any(CarEntity.class));
        verify(advertEntityRepository, never()).save(any(AdvertEntity.class));
    }


    @Test
    public void shouldRejectByDealId() {
        when(dealEntityRepository.findById(1L)).thenReturn(singletonList(dealEntity));

        carService.rejectDeal(1L);

        verify(dealEntityRepository).updateDealWithRejectStatus(1);
    }

    @Test(expected = DealNotFoundException.class)
    public void shouldThrowExceptionIfDealByIdNotFound() {
        when(dealEntityRepository.findById(1L)).thenReturn(emptyList());

        carService.rejectDeal(1L);
    }

    @Test
    public void shouldPutCarForSale() {

        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getPlateNumber())).thenReturn(emptyList());
        when(carEntityRepository.save(any(CarEntity.class))).thenReturn(carEntity);
        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getPhoneNumber())).thenReturn(emptyList());
        when(clientEntityRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), AdvertStatus.OPEN))
            .thenReturn(emptyList());
        when(advertEntityRepository
            .findByCarIdAndClientIdAndStatus(carEntity.getId(), clientEntity.getId(), AdvertStatus.OPEN))
            .thenReturn(emptyList());

        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        assertThat(carService.putCarToSale(carOnSaleRequest)).isEqualTo(advertEntity.getId());
    }

    @Test
    public void shouldReturnExistingAdvertId() {

        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getPhoneNumber())).thenReturn(singletonList(clientEntity));

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), AdvertStatus.OPEN))
            .thenReturn(singletonList(advertEntity));

        assertThat(carService.putCarToSale(carOnSaleRequest)).isEqualTo(advertEntity.getId());

        verify(advertEntityRepository, never()).save(any(AdvertEntity.class));

    }

    @Test
    public void shouldUpdatePriceForExitingAdvert() {
        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getPhoneNumber())).thenReturn(singletonList(clientEntity));

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), AdvertStatus.OPEN))
            .thenReturn(emptyList());

        when(advertEntityRepository
            .findByCarIdAndClientIdAndStatus(carEntity.getId(), clientEntity.getId(), AdvertStatus.OPEN))
            .thenReturn(singletonList(advertEntity));

        when(advertEntityRepository.save(advertEntityArgumentCaptor.capture())).thenReturn(advertEntity);


        carService.putCarToSale(carOnSaleRequest);

        assertThat(advertEntityArgumentCaptor.getValue()).isEqualToIgnoringNullFields(advertEntity);
    }

    @Test
    public void shouldChooseDealWithHigherPrice() {
        Client client = new Client("vova", "petrov", "099");
        ClientEntity secondClient = constructClientEntity(client);

        List<DealEntity> dealEntities = new ArrayList<>();

        dealEntities.add(new DealEntity(1L, clientEntity, 200, advertEntity, ACTIVE));
        dealEntities.add(new DealEntity(2L, secondClient, 400, advertEntity, ACTIVE));

        when(dealEntityRepository.findByAdvertId(1L)).thenReturn(dealEntities);
        when(dealEntityRepository.save(dealEntityArgumentCaptor.capture()))
            .thenReturn(anyListOf(DealEntity.class));

        when(advertEntityRepository.findById(1L)).thenReturn(advertEntity);

        when(advertEntityRepository.save(advertEntityArgumentCaptor.capture()))
            .thenReturn(any(AdvertEntity.class));

        carService.chooseBestDealByAdvertId(1L);

        assertThat(dealEntityArgumentCaptor.getValue()).hasSize(2);
        assertThat(dealEntityArgumentCaptor.getValue().get(0)).isEqualToIgnoringNullFields(dealEntities.get(0));
        assertThat(dealEntityArgumentCaptor.getValue().get(1)).isEqualToIgnoringNullFields(dealEntities.get(1));

        assertThat(advertEntityArgumentCaptor.getValue()).isEqualToIgnoringNullFields(advertEntity);
    }


    @Test(expected = DealNotFoundException.class)
    public void shouldFindDealWithHigherPrice() {
        carService.chooseBestDealByAdvertId(1L);
    }


    @Test
    public void shouldAddDeal(){
        DealRequest dealRequest = new DealRequest();
        dealRequest.setName("Andrey");
        dealRequest.setPhoneNumber("09633");
        dealRequest.setPrice(500);
        dealRequest.setSureName("Sevruk");

        when(dealEntityRepository.findByAdvertIdAndBuyerIdAndPriceAndStatus(advertEntity.getId(), clientEntity.getId(), 500, ACTIVE))
            .thenReturn(Collections.emptyList());

        when(advertEntityRepository.findById(advertEntity.getId())).thenReturn(advertEntity);

        carService.createDeal(dealRequest, 1L);

    }


    private CarOnSaleRequest getCarOnSaleRequest(Car car, Client client) {
        return CarOnSaleRequest.builder()
            .brand(car.getBrand())
            .color(car.getColor())
            .model(car.getModel())
            .name(client.getName())
            .sureName(client.getSureName())
            .phoneNumber(client.getPhoneNumber())
            .plateNumber(car.getPlateNumber())
            .price(100.0)
            .year(2016)
            .build();
    }

    private AdvertEntity constructAdvertEntity(CarEntity carEntity, ClientEntity clientEntity) {
        AdvertEntity advertEntity = new AdvertEntity();
        advertEntity.setId(1L);
        advertEntity.setCar(carEntity);
        advertEntity.setStatus(OPEN);
        advertEntity.setClient(clientEntity);
        advertEntity.setPrice(1000.0);
        advertEntity.setDealId(1L);

        return advertEntity;
    }

    private ClientEntity constructClientEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1L);
        clientEntity.setName(client.getName());
        clientEntity.setPhoneNumber(client.getPhoneNumber());
        clientEntity.setSurname(client.getSureName());
        return clientEntity;
    }

    private DealEntity constructDealEntity() {
        DealEntity dealEntity = new DealEntity();
        dealEntity.setId(1L);
        dealEntity.setStatus(REJECTED);
        dealEntity.setPrice(200);
        return dealEntity;
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
