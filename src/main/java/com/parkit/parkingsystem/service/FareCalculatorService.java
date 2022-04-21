package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {


    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();

        //duration between the out time and the in time in milliseconds
        double duration = outHour - inHour;

        double ratio = 1;

        //calculation of a discount for recurring users, equal to 5%
        if (ticket.getRecurringUser() == true) {
            ratio = 0.95;
        }

        //calculation for the users who stay more than 30 min. Fares depending on the vehicule's type
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
            //Set the price to 0$ if a user stay less than 30 min
            ticket.setPrice(0);
        }
    }
}
