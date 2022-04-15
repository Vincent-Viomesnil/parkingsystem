package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void saveTicketDaoShouldFailTest() {
        assertThrows(RuntimeException.class, () -> {
            ticketDAOTest.saveTicket(null);
        });
    }

    @Test
    public void updateTicketDaoShouldFailTest() {
        assertThrows(RuntimeException.class, () -> {
            ticketDAOTest.updateTicket(null);
        });
    }

}