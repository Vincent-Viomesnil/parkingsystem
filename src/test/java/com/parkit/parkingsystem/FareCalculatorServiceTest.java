package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("FareCalculatorServiceTest")
public class FareCalculatorServiceTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private static TicketDAO ticketDAOTest;


    @BeforeAll
    private static void setUp() {
        ticketDAOTest = new TicketDAO();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @DisplayName("A fare calculation for a car parked during one hour")
    @Test
    public void calculateFareCar() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        fareCalculatorService.calculateFare(ticket);

        //Act
        assertEquals(Fare.CAR_RATE_PER_HOUR, ticket.getPrice());
    }

    @DisplayName("A fare calculation for a bike parked during one hour")
    @Test
    public void calculateFareBike() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        fareCalculatorService.calculateFare(ticket);

        //Act
        assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
    }

    @DisplayName("A fare calculation for an unknown type of vehicle")
    @Test
    public void calculateFareUnknownType() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @DisplayName("A fare calculation for a bike with an inversion between out time and in time")
    @Test
    public void calculateFareBikeWithFutureInTime() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @DisplayName("A fare calculation for a bike parked during 45 min")
    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000)); //45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        fareCalculatorService.calculateFare(ticket);

        //Act
        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @DisplayName("A fare calculation for a car parked during 45 min")
    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));//45 minutes parking time should give 3/4th parking fare
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        fareCalculatorService.calculateFare(ticket);

        //Act
        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @DisplayName("A fare calculation for a car parked during one day")
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));//24 hours parking time should give 24 * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Assert
        fareCalculatorService.calculateFare(ticket);

        //Act
        assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
    }

    @DisplayName("A calculation for a car parked under 30 min and should be free")
    @Test
    public void calculateFareCarWith30MinFree() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket); // ticket.setPrice()

        //Assert
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice()); // fare car for less than 30 min parked, should be equal to 0
    }

    @DisplayName("A calculation for a bike parked under 30 min and should be free")
    @Test
    public void calculateFareBikeWith30MinFree() {
        //Arrange
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (29 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        //Act
        fareCalculatorService.calculateFare(ticket);

        //Assert
        assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice()); // fare bike for less than 30 min parked, should be equal to 0

    }

    @DisplayName("A calculation for recurring users car and should receive a 5% discount")
    @Test
    public void calculateFareForRecurringUsersCar() {
        //Arrange
        Date inTime = new Date();
        Date outTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABC");
        ticket.setRecurringUser(true);

        //Act
        fareCalculatorService.calculateFare(ticket); // ticket.setPrice()

        //Assert
        assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice()); //Discount 5% if the user car is recurring
    }

    @DisplayName("A calculation for recurring users bike and should receive a 5% discount")
    @Test
    public void calculateFareForRecurringUsersBike() {
        //Arrange
        Date inTime = new Date();
        Date outTime = new Date();
        inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("321BCA");
        ticket.setRecurringUser(true);

        //Act
        fareCalculatorService.calculateFare(ticket); // ticket.setPrice()

        //Assert
        assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice()); //Discount 5% if the user bike is recurring
    }
}






