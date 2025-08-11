package org.example.myClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.example.myClasses.Manager.objectMapper;
import static org.example.myClasses.Manager.scan;


public class Account {

    private String acctNumber;
    private String acctAddress;
    private String phoneNumber;
    private String email;
    private ArrayList<String> resvNumbers;


    public Account () {

    }

    /**
     * Parameterized constructor
     * @param acctNumber
     * @param acctAddress
     * @param email
     * @param phoneNumber
     */
    public Account(String acctNumber, String acctAddress, String phoneNumber, String email, ArrayList<String> resvNumbers) {
        this.acctNumber = acctNumber;
         this.acctAddress = acctAddress;
         this.phoneNumber = phoneNumber;
         this.email = email;
         this.resvNumbers = resvNumbers;
    }


    /**
     * @param resv
     * @return
     */
    public void draftReservation(Reservation resv) {

        String mainPath = "C:\\Users\\keita\\Projects\\SWEN646\\LodgingReservation\\data\\accounts";
        String resvJsonPath = mainPath + File.separator + resv.getAcctNum() + File.separator + "res-" + resv.getResvNum() + ".json";
        File resvFile = new File(resvJsonPath);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
        try {
            objectMapper.writeValue(resvFile, resv);
            System.out.println("\nReservation with the account #" + resv.getResvNum() + " is created successfully");
            String str;
            if (resv.getResvNum().charAt(0) == 'H') {
                HotelReservation hr = (HotelReservation)resv;
                str = hr.toString();
                System.out.println(str);
            }
            else if (resv.getResvNum().charAt(0) == 'C') {
                CabinReservation hr = (CabinReservation)resv;
                str = hr.toString();
                System.out.println(str);
            }
            else if (resv.getResvNum().charAt(0) == 'O') {
                HouseReservation hr = (HouseReservation)resv;
                str = hr.toString();
                System.out.println(str);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Reservation updateReservation(Reservation resv) {

        System.out.println("Please enter a number for the corresponding account data you would like to update or change");
        System.out.println("\n1 for lodging address");
        System.out.println("2 for lodging email");
        System.out.println("3 for start date");
        System.out.println("4 for number of night");
        System.out.println("5 for number of bedroom");
        System.out.println("6 for number of bed");
        System.out.println("7 for number of bathroom");
        System.out.println("8 for lodging size");
        System.out.println("Press 0 to exit or other to finish");

        String strInput;
        int intInput;

        while(true) {
            System.out.println("\nEnter a number that corresponds to the account field to be updated");
            System.out.println("Otherwise enter 0, any other number or character to end the update");
            int option = scan.nextInt();
            scan.nextLine();
            switch(option) {
                case 1:
                    System.out.println("Update lodging address --> : ");
                    strInput = scan.nextLine();
                    resv.setLodgingAddress(strInput);
                    break;
                case 2:
                    System.out.println("Update lodging email --> : ");
                    strInput = scan.nextLine();
                    resv.setLodgingEmail(strInput);
                    break;
                case 3:
                    System.out.println("Update start date --> : ");
                    strInput = scan.nextLine();
                    resv.setStartDate(strInput);
                    break;
                case 4:
                    System.out.println("Update number of night --> : ");
                    intInput = scan.nextInt();
                    resv.setNumNight(intInput);
                    scan.nextLine();
                    break;
                case 5:
                    System.out.println("Update number of bedroom --> : ");
                    intInput = scan.nextInt();
                    resv.setNumBedroom(intInput);
                    scan.nextLine();
                    break;
                case 6:
                    System.out.println("Update number of bed --> : ");
                    intInput = scan.nextInt();
                    resv.setNumBed(intInput);
                    scan.nextLine();
                case 7:
                    System.out.println("Update number of bathroom --> : ");
                    intInput = scan.nextInt();
                    resv.setNumBathroom(intInput);
                    scan.nextLine();
                case 8:
                    System.out.println("Update lodging size --> : ");
                    intInput = scan.nextInt();
                    resv.setLodgingSize(intInput);
                    scan.nextLine();
                    break;
                case 0:
                default:
                    return resv;
            }
        }
    }


    public Reservation updateHotelResv(Reservation resv) {

        System.out.println("Please enter a number for the corresponding account data you would like to update or change");
        System.out.println("\n1 for lodging address");
        System.out.println("2 for lodging email");
        System.out.println("3 for start date");
        System.out.println("4 for number of night");
        System.out.println("5 for lodging size");
        System.out.println("Press 0 to exit or other to finish");

        String strInput;
        int intInput;

        while(true) {
            System.out.println("\nEnter a number that corresponds to the account field to be updated");
            System.out.println("Otherwise enter 0, any other number or character to end the update");
            int option = scan.nextInt();
            scan.nextLine();
            switch(option) {
                case 1:
                    System.out.println("Update lodging address --> : ");
                    strInput = scan.nextLine();
                    resv.setLodgingAddress(strInput);
                    break;
                case 2:
                    System.out.println("Update lodging email --> : ");
                    strInput = scan.nextLine();
                    resv.setLodgingEmail(strInput);
                    break;
                case 3:
                    System.out.println("Update start date --> : ");
                    strInput = scan.nextLine();
                    resv.setStartDate(strInput);
                    break;
                case 4:
                    System.out.println("Update number of night --> : ");
                    intInput = scan.nextInt();
                    resv.setNumNight(intInput);
                    scan.nextLine();
                    break;
                case 5:
                    System.out.println("Update lodging size --> : ");
                    intInput = scan.nextInt();
                    resv.setLodgingSize(intInput);
                    scan.nextLine();
                    break;
                case 0:
                default:
                    return resv;
            }
        }
    }


    /**
     * @return acctNumber
     */
    public String getAcctNumber() {
        return this.acctNumber;
    }

    public void setAcctNumber(String acctNumber) {
        this.acctNumber = acctNumber;
    }

    /**
     * @return acctAddress
     */
    public String getAcctAddress() {
        return this.acctAddress;
    }

    /**
     * @param acctAddress
     */
    public void setAcctAddress(String acctAddress) {
        this.acctAddress = acctAddress;
    }

    /**
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }


    public ArrayList<String> getResvNumbers() {
        return resvNumbers;
    }

    public void setResvNumbers(ArrayList<String> resvNumbers) {
        this.resvNumbers = resvNumbers;
    }


    /**
     * @return account information
     */
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Error converting to JSON: " + e.getMessage();
        }
    }

}
