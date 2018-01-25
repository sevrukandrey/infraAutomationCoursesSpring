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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static com.playtika.automation.domain.AdvertStatus.CLOSED;
import static com.playtika.automation.domain.AdvertStatus.OPEN;
import static com.playtika.automation.domain.DealStatus.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
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
    private DealRequest dealRequest;

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
    private ArgumentCaptor<List<DealEntity>> dealEntitiesArgumentCaptor;

    @Captor
    private ArgumentCaptor<AdvertEntity> advertEntityArgumentCaptor;

    @Captor
    private ArgumentCaptor<DealEntity> dealEntityArgumentCaptor;


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
        carOnSaleRequest = new CarOnSaleRequest(car, client, 100);
        dealRequest = new DealRequest(client, 500);
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
            OPEN)).thenReturn(singletonList(advertEntity));

        carService.addCar(car, 1000.0, "093");

        verify(clientEntityRepository, never()).save(any(ClientEntity.class));
        verify(carEntityRepository, never()).save(any(CarEntity.class));
        verify(advertEntityRepository, never()).save(any(AdvertEntity.class));
    }

    @Test
    public void shouldRejectByDealId() {
        when(dealEntityRepository.findById(1L)).thenReturn(dealEntity);
        when(dealEntityRepository.save(dealEntityArgumentCaptor.capture())).thenReturn(dealEntity);

        carService.rejectDeal(1L);

        assertThat(dealEntityArgumentCaptor.getValue())
            .isEqualToIgnoringNullFields(new DealEntity(1L, null, 200, null, REJECTED));
    }

    @Test(expected = DealNotFoundException.class)
    public void shouldThrowExceptionIfDealByIdNotFound() {
       when(dealEntityRepository.findById(1L)).thenReturn(null);

        carService.rejectDeal(1L);
    }

    @Test
    public void shouldPutCarForSale() {

        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getCar().getPlateNumber())).thenReturn(emptyList());
        when(carEntityRepository.save(any(CarEntity.class))).thenReturn(carEntity);

        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getClient().getPhoneNumber())).thenReturn(emptyList());
        when(clientEntityRepository.save(any(ClientEntity.class))).thenReturn(clientEntity);

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), OPEN))
            .thenReturn(emptyList());
        when(advertEntityRepository
            .findByCarIdAndClientIdAndStatus(carEntity.getId(), clientEntity.getId(), OPEN))
            .thenReturn(emptyList());

        when(advertEntityRepository.save(any(AdvertEntity.class))).thenReturn(advertEntity);

        assertThat(carService.putCarToSale(carOnSaleRequest)).isEqualTo(advertEntity.getId());
    }

    @Test
    public void shouldReturnExistingAdvertId() {

        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getCar().getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getClient().getPhoneNumber())).thenReturn(singletonList(clientEntity));

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), OPEN))
            .thenReturn(singletonList(advertEntity));

        assertThat(carService.putCarToSale(carOnSaleRequest)).isEqualTo(advertEntity.getId());

        verify(advertEntityRepository, never()).save(any(AdvertEntity.class));

    }

    @Test
    public void shouldUpdatePriceForExitingAdvert() {
        when(carEntityRepository.findByPlateNumber(carOnSaleRequest.getCar().getPlateNumber())).thenReturn(singletonList(carEntity));
        when(clientEntityRepository.findByPhoneNumber(carOnSaleRequest.getClient().getPhoneNumber())).thenReturn(singletonList(clientEntity));

        when(advertEntityRepository
            .findByCarIdAndClientIdAndPriceAndStatus(carEntity.getId(), clientEntity.getId(), carOnSaleRequest.getPrice(), OPEN))
            .thenReturn(emptyList());

        when(advertEntityRepository
            .findByCarIdAndClientIdAndStatus(carEntity.getId(), clientEntity.getId(), OPEN))
            .thenReturn(singletonList(advertEntity));

        when(advertEntityRepository.save(advertEntityArgumentCaptor.capture())).thenReturn(advertEntity);


        long advertId = carService.putCarToSale(carOnSaleRequest);

        assertThat(advertId).isEqualTo(advertEntity.getId());
        assertThat(advertEntityArgumentCaptor.getValue()).isEqualToIgnoringNullFields(new AdvertEntity(carEntity, clientEntity, null,
            carOnSaleRequest.getPrice(), OPEN));
    }



    @Test
    public void shouldChooseDealWithHigherPrice() {
        Client client = new Client("vova", "petrov", "099");
        ClientEntity secondClient = constructClientEntity(client);

        DealEntity dealWithLowerPrice = new DealEntity(1L, clientEntity, 200, advertEntity, ACTIVE);
        DealEntity dealWithHigherPrice = new DealEntity(2L, secondClient, 400, advertEntity, ACTIVE);

        when(dealEntityRepository.findByAdvertId(1L))
            .thenReturn(asList(dealWithLowerPrice, dealWithHigherPrice));
        when(dealEntityRepository.save(dealEntitiesArgumentCaptor.capture()))
            .thenReturn(anyListOf(DealEntity.class));

        when(advertEntityRepository.findById(1L))
            .thenReturn(advertEntity);
        when(advertEntityRepository.save(advertEntityArgumentCaptor.capture()))
            .thenReturn(any(AdvertEntity.class));

        long dealId = carService.chooseBestDealByAdvertId(1L);

        assertThat(dealId).isEqualTo(dealWithHigherPrice.getId());

        assertThat(dealEntitiesArgumentCaptor.getValue()).hasSize(2);
        assertThat(dealEntitiesArgumentCaptor.getValue().get(0))
            .isEqualToIgnoringNullFields(new DealEntity(1L, clientEntity, 200, advertEntity, REJECTED));
        assertThat(dealEntitiesArgumentCaptor.getValue().get(1))
            .isEqualToIgnoringNullFields(new DealEntity(2L, secondClient, 400, advertEntity, APPROVED));

        assertThat(advertEntityArgumentCaptor.getValue())
            .isEqualToIgnoringNullFields(new AdvertEntity(1L,carEntity,clientEntity,2L, 1000, CLOSED));
    }

    @Test(expected = AdvertNotFoundException.class)
    public void shouldThrowNotExceptionIfAdvertNotFound() {
        carService.chooseBestDealByAdvertId(1L);
    }

    @Test(expected = DealNotFoundException.class)
    public void shouldThrowExceptionIfDealNotFond() {
        when(advertEntityRepository.findById(1L))
            .thenReturn(advertEntity);

        carService.chooseBestDealByAdvertId(1L);
    }


    @Test
    public void shouldAddDeal(){
        when(advertEntityRepository.findById(1L))
            .thenReturn(advertEntity);

        when(clientEntityRepository.findByPhoneNumber(dealRequest.getClient().getPhoneNumber())).thenReturn(singletonList(clientEntity));

        when(dealEntityRepository.findByAdvertIdAndBuyerIdAndPriceAndStatus(advertEntity.getId(), clientEntity.getId(), 500, ACTIVE))
            .thenReturn(emptyList());
        when(dealEntityRepository.save(dealEntityArgumentCaptor.capture())).thenReturn(dealEntity);

        long dealId = carService.createDeal(dealRequest, 1L);

        assertThat(dealId).isEqualTo(dealEntity.getId());
        assertThat(dealEntityArgumentCaptor.getValue())
            .isEqualToComparingFieldByFieldRecursively(new DealEntity(clientEntity, 500, advertEntity, ACTIVE));

    }

    @Test(expected = AdvertNotFoundException.class)
    public void shouldThrowAdvertNotFoundException() {
       carService.createDeal(dealRequest, 1L);
    }

    @Test(expected = AdvertClosedException.class)
    public void shouldThrowAdvertClosedException() {
        when(advertEntityRepository.findById(1L))
            .thenReturn(new AdvertEntity(1L,carEntity,clientEntity,1L,100,AdvertStatus.CLOSED));

        carService.createDeal(dealRequest, 1L);
    }

    @Test
    public void shouldReturnAdvertIdByCarId() {
        long carId =1;
        when(advertEntityRepository.findByCarId(carId)).thenReturn(singletonList(advertEntity));

        assertThat(carService.getAdvertIdByCarId(carId)).isEqualTo(advertEntity.getId());
    }

    @Test(expected = AdvertNotFoundException.class)
    public void shouldReturnExceptionWhenAdvertByCarIdIsClosed() {
        long carId =1;
        when(advertEntityRepository.findByCarId(carId))
                .thenReturn(singletonList(new AdvertEntity(1L,carEntity,clientEntity,1L,100,AdvertStatus.CLOSED)));

        assertThat(carService.getAdvertIdByCarId(carId)).isEqualTo(advertEntity.getId());
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
        dealEntity.setStatus(ACTIVE);
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
