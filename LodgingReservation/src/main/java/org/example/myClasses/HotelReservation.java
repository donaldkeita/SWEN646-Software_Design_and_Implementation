package org.example.myClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class HotelReservation extends Reservation{

    private boolean kitchenette;


    public HotelReservation() {
    }

    public HotelReservation(String acctNumber, String resvNumber, String lodgingAddress, String lodgingEmail, String startDate,
                            int numNight, int numBed, int numBedroom, int numBathroom, int size, float price, String status,
                            boolean kitchenette) {
        super(acctNumber, "H" + resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, numBed, numBedroom, numBathroom,
                size, price, status);
        this.kitchenette = kitchenette;
    }


    /**
     * @return kitchenette
     */
    public boolean isKitchenette() {
        return kitchenette;
    }

    /**
     */
    public void setKitchenette(boolean kitchenette) {
        this.kitchenette = kitchenette;
    }


    /***
     * calculate the price of the hotel for a reservation
     */
    public void updatePrice() {
        super.updatePrice();

        this.price += 50;
        if (this.kitchenette) this.price += 10;
    }

    /***
     * display hotel reservation info
     * @return HotelReservation object data
     */
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }

}
