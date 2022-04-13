package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

    private static DataBasePrepareService dataBasePrepareService;
    private static Ticket ticket;


    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() throws Exception {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();

    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        // when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown() {

    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability

        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        parkingService.processIncomingVehicle();

        ParkingSpot parkingSpotTest = new ParkingSpot(1, ParkingType.CAR, true);

        verify(parkingSpotDAO, times(1)).updateParking(parkingSpotTest);

    }

     /*@Test
    public void testParkingLotExit() {
        testParkingACar();

        //Mettre la date de sortie pour process exiting vehicle

        when(parkingService.when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        Date outTime = new Date();
        ticket = new Ticket();
        ticket.setOutTime(outTime);

        Créer un nouveau ticket, saver au niveau de la base.
         Quand on demande un ticket on retourne le ticket
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class)); */

    //TODO: check that the fare generated and out time are populated correctly in the database


}

