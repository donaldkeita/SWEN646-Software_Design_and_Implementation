package org.example.myClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class HouseReservation extends Reservation{

    private int floorNum;

    public HouseReservation() {
    }

    public HouseReservation(String acctNumber, String resvNumber, String lodgingAddress, String lodgingEmail, String startDate,
                            int numNight, int numBed, int numBedroom, int numBathroom, int size, float price, String status, int floorNum){
        super(acctNumber, "O" + resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, numBed, numBedroom, numBathroom,
                size, price, status);
        this.floorNum = floorNum;
    }


    /**
     * @return floorNum
     */
    public int getFloorNum() {
        return this.floorNum;
    }

    /**
     * @param floorNum
     */
    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    /**
     * calculate the price the house for a reservation
     */
    public void updatePrice() {
        super.updatePrice();
    }

    /**
     * display house reservation info
     * @return HouseReservation object data
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
