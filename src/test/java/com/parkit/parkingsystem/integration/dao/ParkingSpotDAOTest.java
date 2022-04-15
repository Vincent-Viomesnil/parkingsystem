package com.parkit.parkingsystem.integration.dao;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private static ParkingSpot parkingSpot;
    @Mock
    private static DataBaseTestConfig dataBaseTestConfig;
    @Mock
    private static Connection con;
    @Mock
    private static PreparedStatement ps;
    @Mock
    private static ResultSet rs;

    @BeforeEach
    private void setUpPerTest() throws Exception {

        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE, true);
        when(dataBaseTestConfig.getConnection()).thenReturn(con);
    }

    @Test
    void nextAvailableParkingSpotTest() throws SQLException, ClassNotFoundException {
        // WHEN

        when(con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(2);

        // THEN
        assertEquals(2, parkingSpotDAO.getNextAvailableSlot((ParkingType.BIKE)));

    }

    @Test
    void UpdateParkingTest() throws Exception {

        when(con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);

        assertEquals(parkingSpotDAO.updateParking(parkingSpot), true);
    }
}

