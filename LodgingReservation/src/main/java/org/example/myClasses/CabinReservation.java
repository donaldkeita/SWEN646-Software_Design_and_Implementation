package org.example.myClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CabinReservation extends Reservation{

    private boolean fullKitchen;
    private String loft;


    /**
     * Default constructor
     */
    public CabinReservation() {
    }


    public CabinReservation(String acctNumber, String resvNumber, String lodgingAddress, String lodgingEmail,
                            String startDate, int numNight, int numBed, int numBedroom, int numBathroom, int size, float price, String status,
                            boolean fullKitchen, String loft) {
        super(acctNumber, "C" + resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, numBed, numBedroom, numBathroom,
                size, price, status);
        this.fullKitchen = fullKitchen;
        this.loft = loft;
    }

    /**
     * @return fullKitchen
     */
    public boolean getFullKitchen() {
        return this.fullKitchen;
    }

    /**
     * @param fullKitchen
     */
    public void setFullKitchen(boolean fullKitchen) {
        this.fullKitchen = fullKitchen;
    }

    /**
     * @return loft
     */
    public String getLoft() {
        return this.loft;
    }

    /**
     * @param loft
     */
    public void setLoft(String loft) {
        this.loft = loft;
    }

    /**
     * calculate the price of the Cabin for a reservation
     */
    public void updatePrice() {
        super.updatePrice();

        if (this.fullKitchen) this.price += 20;
        this.price += (this.getNumBathroom() - 1 ) * 5;
    }


    /**
     * display cabin reservation info
     * @return CabinReservation object data
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
