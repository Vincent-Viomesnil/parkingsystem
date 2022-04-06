package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketDAOTest {

    private static TicketDAO ticketDAOTest;
    private Ticket ticket;
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    @Test
    public void saveTicketDaoTest() {
        //GIVEN
        ticketDAOTest = new TicketDAO();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        //THEN
        assertTrue(ticketDAOTest.saveTicket(ticket));

    }

    @Test
    public void updateTicketDAOTest() {
        ticketDAOTest = new TicketDAO();
        ticket = new Ticket();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
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
        ticketDAOTest = new TicketDAO();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        //THEN
        assertThat(ticketDAOTest.getTicket("ABCDEF")).isNotNull();
    }

    @Test
    public void getTicketsDAOTest() {
        //GIVEN
        ticketDAOTest = new TicketDAO();
        ticketDAOTest.dataBaseConfig = dataBaseTestConfig;
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("000"); //changer la plaque d'immatriculation à chaque test
        ticketDAOTest.saveTicket(ticket);
        ticketDAOTest.saveTicket(ticket);

        //THEN
        assertThat(ticketDAOTest.getTickets("000")).isEqualTo(2);
    }
}