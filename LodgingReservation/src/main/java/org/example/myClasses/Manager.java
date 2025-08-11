package org.example.myClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.exceptions.DuplicateObjectException;
import org.example.exceptions.IllegalLoadException;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;


public class Manager {

    private Account account;
    public Reservation resv;
    static public Scanner scan = new Scanner(System.in);
    ArrayList<Account> acctList;
    ArrayList<Reservation> resvs;
    private File tempDirectory;
    private final File myDirectory;
    private String acctNumber;
    static public final ObjectMapper objectMapper = new ObjectMapper();


    public Manager() {
        myDirectory = new File("C:\\Users\\keita\\Projects\\SWEN646\\LodgingReservation\\data\\accounts");
        getAllAccounts();
        getAllReservations();
    }


    /**
     * Create an account
     *
     * @return
     */
    public void createAccount(String acctNumber) throws IOException {

        this.acctNumber = acctNumber;
        // check if account already exists based on account number
        try {
            Optional<Account> foundAcct =  this.acctList.stream().filter(acct -> acct.getAcctNumber()
                    .equalsIgnoreCase(this.acctNumber)).findAny();
            if (foundAcct.isPresent()) throw new DuplicateObjectException("Error! Account with account number#" + this.acctNumber +
                    " already exists");

            System.out.println("Enter the address that is associated with the account number:");
            String address =  scan.nextLine();
            System.out.println("Enter phone number (should be 10 digits):");
            String phoneNumber =  scan.nextLine();
            System.out.println("Enter the email address associated with the account number:");
            String email =  scan.nextLine();
            this.account = new Account(acctNumber, address, phoneNumber, email, new ArrayList<String>());
            createAccountFolder();
            saveAccountIntoFile();
            // add account to the list of account in cache
            this.acctList.add(this.account);
            System.out.println("The account with account number#" + this.account.getAcctNumber() + " is successfully created");
            System.out.println(this.account.toString());
        }
        catch (DuplicateObjectException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Read an account info the files
     * @return
     */
    public void getAccount() {

        System.out.print("Enter the account number (A followed by 9 digits) -> : ");
        this.acctNumber = scan.nextLine();

        this.account =  this.acctList.stream().filter(acct -> acct.getAcctNumber().equalsIgnoreCase(acctNumber)).findAny()
                .orElseThrow(() -> new IllegalLoadException("Account with " + this.acctNumber + " does not exist"));
        System.out.println(this.account.toString());
    }

    public void getAllAccounts() {

        this.acctList = readInAllAccount();

        if (this.acctList.isEmpty()) System.out.println("The list of account is empty.");
        else {
            System.out.println("The list of loaded accounts");
            this.acctList.forEach(acct -> System.out.println(acct.toString()));

        }
    }

    public void getAllReservations() {

        ObjectMapper mp = new ObjectMapper();
        this.resvs = readInAllReservation();

        if (resvs.isEmpty()) System.out.println("The list of reservation is empty.");
        else {
            System.out.println("The list of loaded reservations");
            resvs.forEach(resv -> {
                try {
                    System.out.println(mp.writeValueAsString(resv));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void updateAccount() {

        System.out.print("Enter the account number (A followed by 9 digits) of the account to be updated : ");
        acctNumber = scan.nextLine();
        String strInput;

        this.account =  this.acctList.stream().filter(acct -> acct.getAcctNumber().equalsIgnoreCase(acctNumber)).findAny()
                .orElseThrow(() -> new IllegalLoadException("Account with " + this.acctNumber + " does not exist"));

        System.out.println("\nPlease enter a number for the corresponding account data you would like to update or change");

        while(true) {
            System.out.println("\nEnter a number that corresponds to the account field to be updated");
            System.out.println("1 to update address");
            System.out.println("2 to update phone number");
            System.out.println("3 to update email");
            System.out.println("Otherwise enter 0, any other number than 1, 2, 3 to end the update");
            int option = scan.nextInt();
            scan.nextLine();
            switch(option) {
                case 1:
                    System.out.print("Update address --> : ");
                    strInput = scan.nextLine();
                    this.account.setAcctAddress(strInput);
                    break;
                case 2:
                    System.out.print("Update phone number --> : ");
                    strInput = scan.nextLine();
                    this.account.setPhoneNumber(strInput);
                    break;
                case 3:
                    System.out.print("Update email --> : ");
                    strInput = scan.nextLine();
                    this.account.setEmail(strInput);
                    break;
                default:
                    String accountJsonPath = myDirectory + File.separator + this.acctNumber + File.separator + "acc-" +
                            this.acctNumber + ".json";
                    tempDirectory = new File(accountJsonPath);
                    saveAccountIntoFile();
                    System.out.println("The account with account number#" + this.account.getAcctNumber() + " is successfully updated");
                    System.out.println(this.account.toString());
                    return;
            }
        }
    }

    /**
     * Make a reservation
     * @return
     */
    public void makeReservation(String resvNumber) {

        try{
            Optional<String> foundResvNum =  this.account.getResvNumbers().stream().filter(resvNum -> resvNum
                    .equalsIgnoreCase(resvNumber)).findAny();
            if (foundResvNum.isPresent()) throw new DuplicateObjectException("Error! Reservation with reservation number#" + resvNumber +
                    " already exists");

            System.out.println("There are three types of accommodations: hotel room reservation, cabin reservation, " +
                    "and house reservation");
            System.out.println("Enter: H/h for hotel - C/c for cabin - Enter O/o for house");
            String str = scan.nextLine();
            while(!(str.equalsIgnoreCase("h") || str.equalsIgnoreCase("o") ||
                    str.equalsIgnoreCase("c"))) {
                System.out.println("Illegal character");
                System.out.println("Enter: H/h for hotel - C/c for cabin - Enter O/h for house");
                str = scan.nextLine();
            }
            // Switch case to get input according to the accommodation type
            String accountJsonPath;
            switch (str.toLowerCase()) {
                case "h":
                    resv = getHotelResvInput(resvNumber);
                    account.draftReservation(resv);
                    this.resvs.add(resv);
                    account.getResvNumbers().add(resv.getResvNum());
                    accountJsonPath = myDirectory + File.separator + this.acctNumber + File.separator + "acc-" +
                            this.acctNumber + ".json";
                    tempDirectory = new File(accountJsonPath);
                    saveAccountIntoFile();
                    break;
                case "c":
                    resv = getCabinResvInput(resvNumber);
                    account.draftReservation(resv);
                    this.resvs.add(resv);
                    account.getResvNumbers().add(resv.getResvNum());
                    accountJsonPath = myDirectory + File.separator + this.acctNumber + File.separator + "acc-" +
                            this.acctNumber + ".json";
                    tempDirectory = new File(accountJsonPath);
                    saveAccountIntoFile();
                    break;
                case "o":
                    resv = getHouseResvInput(resvNumber);
                    account.draftReservation(resv);
                    this.resvs.add(resv);
                    account.getResvNumbers().add(resv.getResvNum());
                    accountJsonPath = myDirectory + File.separator + this.acctNumber + File.separator + "acc-" +
                            this.acctNumber + ".json";
                    tempDirectory = new File(accountJsonPath);
                    saveAccountIntoFile();
                    break;
                default:
                    System.exit(0); break;
            }
        }
        catch (DuplicateObjectException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Update a reservation
     * @return
     */
    public void updateReservation() {

        try{
            System.out.println("This account has following reservation numbers: " + account.getResvNumbers());
            // prompt for input reservation number
            System.out.print("Enter a reservation number of the list displayed --> : ");
            String resvNumber = scan.nextLine();

            while(!account.getResvNumbers().contains(resvNumber)) {
                System.out.println("The value entered is not a reservation number of the account#" + account.getAcctNumber());
                System.out.print("Please enter a valid reservation number --> : ");
                resvNumber = scan.nextLine();
            }

            // get reservation based on reservation number
            String finalResvNumber = resvNumber;
            this.resv =  this.resvs.stream().filter(resvt -> resvt.getResvNum().equalsIgnoreCase(finalResvNumber)).findAny()
                    .orElseThrow(() -> new IllegalLoadException("Reservation with " + finalResvNumber + " does not exist"));

            // update reservation fields
            if (dateComparison(resv.getStartDate()) && resv.getStatus().equalsIgnoreCase("draft")) {
                if (resv.getResvNum().charAt(0) == 'H') resv = account.updateHotelResv(resv);
                else {
                    resv = account.updateReservation(resv);
                }
            }
            else {
                System.out.println("Error! Reservation can not be updated");
            }
            // save updated reservation into file
            saveResvFile();
        }
        catch (IllegalLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cancel a reservation
     * @return
     */
    public void cancelReservation() {

        try{
            // display the reservation numbers stored in the account
            System.out.println("This account has following reservation numbers: " + account.getResvNumbers());
            // prompt for input reservation number
            System.out.print("Enter a reservation number of the list displayed to cancel--> : ");
            String resvNumber = scan.nextLine();

            while(!account.getResvNumbers().contains(resvNumber)) {
                System.out.println("The value entered is not a reservation number of the account#" + account.getAcctNumber());
                System.out.print("Please enter a valid reservation number --> : ");
                resvNumber = scan.nextLine();
            }

            // get reservation based on reservation from file directory
            String finalResvNumber = resvNumber;
            this.resv =  this.resvs.stream().filter(resvt -> resvt.getResvNum().equalsIgnoreCase(finalResvNumber)).findAny()
                    .orElseThrow(() -> new IllegalLoadException("Reservation with " + finalResvNumber + " does not exist"));

            // update status to "cancelled" if the status is not "completed"
            if (dateComparison(resv.getStartDate()) && resv.getStatus().equalsIgnoreCase("draft")) {
                resv.setStatus("cancelled");
                // save updated reservation into file
                saveResvFile();
            }
            else {
                System.out.println("Reservation can not be cancelled");
            }
        }
        catch (IllegalLoadException e) {
            System.out.println(e.getMessage());
        }
    }


    public void completeReservation() {

        try{
            // display the reservation numbers stored in the account
            System.out.println("This account has following reservation numbers: " + account.getResvNumbers());
            // prompt for input reservation number
            System.out.print("Enter a reservation number of the list displayed --> : ");
            String resvNumber = scan.nextLine();

            while(!account.getResvNumbers().contains(resvNumber)) {
                System.out.println("The value entered is not a reservation number of the account#" + account.getAcctNumber());
                System.out.print("Please enter a valid reservation number --> : ");
                resvNumber = scan.nextLine();
            }

            String finalResvNumber = resvNumber;
            this.resv =  this.resvs.stream().filter(resvt -> resvt.getResvNum().equalsIgnoreCase(finalResvNumber)).findAny()
                    .orElseThrow(() -> new IllegalLoadException("Reservation with " + finalResvNumber + " does not exist"));

            // update status to "completed" and the price to the lodge price
            if (dateComparison(resv.getStartDate()) && resv.getStatus().equalsIgnoreCase("draft")) {
                resv.setStatus("completed");
                resv.updatePrice();
                // save updated reservation into file
                saveResvFile();
            }
            else {
                System.out.println("Reservation can not be cancelled");
            }
        }
        catch (IllegalLoadException e) {
            System.out.println(e.getMessage());
        }
    }

    public void pricePerNight() {

        try{
            // display the reservation numbers stored in the account
            System.out.println("This account has following reservation numbers: " + account.getResvNumbers());
            // prompt for input reservation number
            System.out.print("Enter a reservation number of the list displayed --> : ");
            String resvNumber = scan.nextLine();

            while(!account.getResvNumbers().contains(resvNumber)) {
                System.out.println("The value entered is not a reservation number of the account#" + account.getAcctNumber());
                System.out.print("Please enter a valid reservation number --> : ");
                resvNumber = scan.nextLine();
            }

            // get reservation based on reservation number from file directory
            String finalResvNumber = resvNumber;
            this.resv =  this.resvs.stream().filter(resvt -> resvt.getResvNum().equalsIgnoreCase(finalResvNumber)).findAny()
                    .orElseThrow(() -> new IllegalLoadException("Reservation with " + finalResvNumber + " does not exist"));

            if (dateComparison(resv.getStartDate()) && resv.getStatus().equalsIgnoreCase("draft")) {
                resv.updatePrice();
                System.out.println("The reservation with identifiable number #" + resv.getResvNum() + " has price per night : $"
                        + resv.getPrice());
            }
            else {
                System.out.println("Request price per night failed!");
            }
        }
        catch (IllegalLoadException e) {
            System.out.println(e.getMessage());
        }
    }


    public void totalReservationPrice() {

        try{
            // display the reservation numbers stored in the account
            System.out.println("This account has following reservation numbers: " + account.getResvNumbers());
            // prompt for input reservation number
            System.out.print("Enter a reservation number of the list displayed --> : ");
            String resvNumber = scan.nextLine();

            while(!account.getResvNumbers().contains(resvNumber)) {
                System.out.println("The value entered is not a reservation number of the account#" + account.getAcctNumber());
                System.out.print("Please enter a valid reservation number --> : ");
                resvNumber = scan.nextLine();
            }

            // get reservation based on reservation number
            String finalResvNumber = resvNumber;
            this.resv =  this.resvs.stream().filter(resvt -> resvt.getResvNum().equalsIgnoreCase(finalResvNumber)).findAny()
                    .orElseThrow(() -> new IllegalLoadException("Reservation with " + finalResvNumber + " does not exist"));

            if (dateComparison(resv.getStartDate()) && resv.getStatus().equalsIgnoreCase("draft")) {
                resv.updatePrice();
                System.out.println("The reservation with identifiable number #" + resv.getResvNum() + " has total reservation price : $"
                        + resv.getPrice() * resv.getNumNight());
            }
            else {
                System.out.println("Request for total reservation price failed!");
            }
        }
        catch (IllegalLoadException e) {
            System.out.println(e.getMessage());
        }
    }


    private Reservation getHotelResvInput(String resvNumber) {
        System.out.print("Enter lodging address --> : ");
        String lodgingAddress = scan.nextLine();
        System.out.print("Enter lodging email --> : ");
        String lodgingEmail = scan.nextLine();
        System.out.print("Enter lodging start date (yyyy-mm-dd) --> : ");
        String startDate = scan.nextLine();
        System.out.print("Enter number of night --> : ");
        int numNight = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter number lodging size --> : ");
        int size = scan.nextInt();
        scan.nextLine();
        System.out.print("Does the lodge have kitchenette? (true/false) --> :" );
        boolean kitchenette = scan.nextBoolean();
        this.resv = new HotelReservation(this.acctNumber, resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, 2, 1, 1,
                size, 0, "draft", kitchenette);
        return this.resv;
    }


    private Reservation getCabinResvInput(String resvNumber) {
        System.out.print("Enter lodging address --> : ");
        String lodgingAddress = scan.nextLine();
        System.out.print("Enter lodging email --> : ");
        String lodgingEmail = scan.nextLine();
        System.out.print("Enter lodging start date --> : ");
        String startDate = scan.nextLine();
        System.out.print("Enter number of night --> : ");
        int numNight = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter number lodging size --> : ");
        int size = scan.nextInt();
        scan.nextLine();
        System.out.print("Does the lodge have a full kitchen? (true/false) --> : " );
        boolean hasFullKitchen = scan.nextBoolean();
        scan.nextLine();
        System.out.print("Does the lodge include a loft --> (Y/N): ");
        String loft = scan.nextLine().toUpperCase();

        this.resv = new CabinReservation(this.acctNumber, resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, 2, 1, 1,
                size, 0, "draft", hasFullKitchen, loft);
        return this.resv;
    }

    private Reservation getHouseResvInput(String resvNumber) {
        System.out.print("Enter lodging address --> : ");
        String lodgingAddress = scan.nextLine();
        System.out.print("Enter lodging email --> : ");
        String lodgingEmail = scan.nextLine();
        System.out.print("Enter lodging start date --> : ");
        String startDate = scan.nextLine();
        System.out.print("Enter number of night --> : ");
        int numNight = scan.nextInt();
        scan.nextLine();
        System.out.print("Enter number lodging size --> : ");
        int size = scan.nextInt();
        scan.nextLine();
        System.out.print("How many floors does the lodge have? --> :" );
        int floorNum = scan.nextInt();

        this.resv = new HouseReservation(acctNumber, resvNumber, lodgingAddress, lodgingEmail, startDate, numNight, 2, 1, 1,
                size, 0, "draft", floorNum);
        return this.resv;
    }


    private ArrayList<Account> readInAllAccount() {

        ArrayList<Account> accountList = new ArrayList<>();
        Path strToPath = Paths.get(myDirectory.toString());

        try (DirectoryStream<Path> accountPaths = Files.newDirectoryStream(strToPath)) {
            // Iterate through parent directory content
            for (Path accountPath : accountPaths) {
                try (DirectoryStream<Path> jsonFilesPaths = Files.newDirectoryStream(accountPath)) {
                    // For each account directory, iterate through its json files
                    for (Path jsonFile : jsonFilesPaths) {
                        // if the file is account json file, then convert it to account object and add it to the account list
                        if (jsonFile.getFileName().toString().startsWith("acc-")) {
                            Account account = objectMapper.readValue(jsonFile.toFile(), Account.class);
                            accountList.add(account);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error accessing directory: " + e.getMessage());
            e.printStackTrace();
        }
        return accountList;
    }


    private ArrayList<Reservation> readInAllReservation() {

        ArrayList<Reservation> resvs = new ArrayList<>();
        Path strToPath = Paths.get(myDirectory.toString());

        try (DirectoryStream<Path> resvPaths = Files.newDirectoryStream(strToPath)) {
            // Iterate through parent directory content
            for (Path resvPath : resvPaths) {
                try (DirectoryStream<Path> jsonFilesPaths = Files.newDirectoryStream(resvPath)) {
                    // For each reservation directory, iterate through its json files
                    for (Path jsonFile : jsonFilesPaths) {
                        // if the file is reservation json file, then convert it to reservation object and add it to the reservation list
                        if (jsonFile.getFileName().toString().startsWith("res-")) {
                            String resvNumber = jsonFile.getFileName().toString();
                            if (resvNumber.charAt(4) == 'H') {
                                resv = objectMapper.readValue(jsonFile.toFile(), HotelReservation.class);
                            }
                            else if (resvNumber.charAt(4) == 'C') {
                                resv = objectMapper.readValue(jsonFile.toFile(), CabinReservation.class);
                            }
                            else if (resvNumber.charAt(4) == 'O') {
                                resv = objectMapper.readValue(jsonFile.toFile(), HouseReservation.class);
                            }
                            resvs.add(resv);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error accessing directory: " + e.getMessage());
            e.printStackTrace();
        }
        return resvs;
    }


    private void createAccountFolder() throws IOException {

        String accountStrPath = myDirectory.toString() + File.separator + this.acctNumber;
        Path accountPath = Paths.get(accountStrPath);
        Path accountDirectory = Files.createDirectory(accountPath);
        // Define the JSON file name
        String jsonFileName = "acc-" + this.acctNumber + ".json";
        // Append the JSON file name to the directory path
        Path jsonAcctPath = accountPath.resolve(jsonFileName);

        Path jsonAcctDirectory = Files.createFile(jsonAcctPath);

        tempDirectory = jsonAcctDirectory.toFile();

        // Now you can use the File object with older APIs
        if (tempDirectory.exists()) {
            System.out.println("File exists: " + tempDirectory.getAbsolutePath());
        } else {
            System.out.println("File does not exist.");
        }
    }


    private void saveAccountIntoFile() {

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            objectMapper.writeValue(tempDirectory, this.account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void saveResvFile() {

        String resvJsonPath = myDirectory.toString() + File.separator + account.getAcctNumber() + File.separator +
                "res-" + resv.getResvNum() + ".json";
        File resvFile = new File(resvJsonPath);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty print JSON
        try {
            mapper.writeValue(resvFile, resv);
            System.out.println("Reservation with the account #" + resv.getResvNum() + " is updated successfully");
            // display save reservation object on the console
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println(objectMapper.writeValueAsString(resv));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return;
    }


    private boolean dateComparison(String strDate) {

        LocalDate localStartDate = LocalDate.parse(strDate);
        Date startDate = Date.valueOf(localStartDate);

        LocalDate localcurDate = LocalDate.now();
        Date curDate = Date.valueOf(localcurDate);
        return curDate.before(startDate);
    }
}
