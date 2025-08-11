package org.example.myClasses;


public class Reservation {

    protected String acctNum;
    protected String resvNum;
    protected String lodgingAddress;
    protected String lodgingEmail;
    protected String startDate;
    protected int    numNight;
    protected int    numBed;
    protected int    numBedroom;
    protected int    numBathroom;
    protected int    lodgingSize;
    protected float  price;
    protected String status;  // cancelled, completed, draft

    //1st constructor with default values without passing values
    public Reservation() {
        this.acctNum = "-99";
        this.status = "draft";
        this.price = 0;
    }


    //2nd constructor with more attributes
    public Reservation(String acctNum, String resvNum, String lodgingAddress, String lodgingEmail, String startDate,
                       int numNight, int numBed, int numBedroom, int numBathroom, int lodgingSize, float price, String status) {
        this.acctNum = acctNum;
        this.resvNum = resvNum;
        this.lodgingAddress = lodgingAddress;
        this.lodgingEmail = lodgingEmail;
        this.startDate = startDate;
        this.numNight = numNight;
        this.numBed = numBed;
        this.numBedroom = numBedroom;
        this.numBathroom = numBathroom;
        this.lodgingSize = lodgingSize;
        this.status = status;
        this.price = 0;
    }


    public String getAcctNum() {
        return this.acctNum;
    }

    public void setAcctNum(String accountNum) {
        this.acctNum = accountNum;
    }

    public String getResvNum() {
        return this.resvNum;
    }

    public void setResvNum(String resvNum) {
        this.resvNum = resvNum;
    }

    public String getLodgingAddress() {
        return this.lodgingAddress;
    }

    public void setLodgingAddress(String lodgingAddress) {
        this.lodgingAddress = lodgingAddress;
    }

    public String getLodgingEmail() {
        return this.lodgingEmail;
    }

    public void setLodgingEmail(String lodgingEmail) {
        this.lodgingEmail = lodgingEmail;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getNumNight() {
        return this.numNight;
    }

    public void setNumNight(int numNight) {
        this.numNight = numNight;
    }

    public int getNumBed() {
        return this.numBed;
    }

    public void setNumBed(int numBed) {
        this.numBed = numBed;
    }

    public int getNumBedroom() {
        return numBedroom;
    }

    public void setNumBedroom(int numBedroom) {
        this.numBedroom = numBedroom;
    }

    public int getNumBathroom() {
        return this.numBathroom;
    }

    public void setNumBathroom(int numBathroom) {
        this.numBathroom = numBathroom;
    }

    public int getLodgingSize() {
        return lodgingSize;
    }

    public void setLodgingSize(int lodgingSize) {
        this.lodgingSize = lodgingSize;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String toString() {
        return "This is my implementation of changing strings";
    }

    public void updatePrice() {
        if (this.lodgingSize > 900) this.price = 135;
        else this.price = 120;
    }

}
