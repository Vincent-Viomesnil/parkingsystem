package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
    public TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        double duration = outHour - inHour;

        double ratio = 1;

        if (ticket.getRecurringUser() == true) {
            ratio = 0.95;
        }

        //TODO: Some tests are failing here. Need to check if this logic is correct

        if (duration > (30 * 60 * 1000)) {
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR:
                    ticket.setPrice((duration / (1000 * 60 * 60)) * Fare.CAR_RATE_PER_HOUR * ratio);
                    break;
                case BIKE:
                    ticket.setPrice((duration / (1000 * 60 * 60)) * Fare.BIKE_RATE_PER_HOUR * ratio);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        } else {
            ticket.setPrice(0);
        }
    }
}
