package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("TicketDAOTest")
public class TicketDAOTest {

    private static TicketDAO ticketDAOTest;
    private Ticket ticket;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @BeforeEach
    private void SetUpPerTest() {
        ticketDAOTest = new TicketDAO();
        ticket = new Ticket();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
    }

    @Test
    @DisplayName("Save a ticket for a vehicle when he has been registered")
    public void saveTicketDaoTest() {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        //THEN
        assertTrue(ticketDAOTest.saveTicket(ticket));
    }

    @Test
    @DisplayName("Update a ticket with all the parameters associated ")
    public void updateTicketDAOTest() {

        //GIVEN
        ticket.setVehicleRegNumber("ABC");
        ticket.setPrice(3);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        ticket.setInTime(timestamp);
        ticket.setOutTime(timestamp);

        //THEN
        assertTrue(ticketDAOTest.updateTicket(ticket));
    }

    @Test
    @DisplayName("Get a ticket not null, with the good information")
    public void getTicketDAOTest() {
        //GIVEN

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        //THEN
        assertThat(ticketDAOTest.getTicket("ABCDEF")).isNotNull();
    }

    @Test
    @DisplayName("Calculate the number of tickets registered in the parking spot")
    public void getTicketsDAOTest() {
        //GIVEN
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("AAA");
        ticketDAOTest.saveTicket(ticket);
        ticketDAOTest.saveTicket(ticket);

        //THEN
        assertThat(ticketDAOTest.getTickets("AAA")).isEqualTo(2);
    }

    @Test
    @DisplayName("the method saveTicket should fail with a ticket null")
    public void saveTicketDaoShouldFailTest() {
        assertThrows(RuntimeException.class, () -> {
            ticketDAOTest.saveTicket(null);
        });
    }

    @Test
    @DisplayName("the method updateTicket should fail with a ticket null")
    public void updateTicketDaoShouldFailTest() {
        assertThrows(RuntimeException.class, () -> {
            ticketDAOTest.updateTicket(null);
        });
    }

}