package org.example;

import org.example.myClasses.Manager;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;


public class TestReservationSystem {

    static Scanner scan = new Scanner(System.in);

    // Generate an Account number
    public static String numberGenerator() {
        Random rand = new Random();
        String strAcct = "A";
        // Generate a random number between 100,000,000 and 999,999,999
        long nineDigitNumber = rand.nextInt(900_000_000) + 100_000_000L;
        System.out.println("Generated 9-digit number: " + nineDigitNumber);
        //String str = "A" + nineDigitNumber;
        return String.valueOf(nineDigitNumber);
    }

    //=============================================================
    // Select a Task
    //=============================================================
    public static void options(int choice, Manager manager) throws IOException {

        String str;

        switch(choice) {
            case 1: // list of loaded accounts
                manager.getAllAccounts(); break;

            case 2: // retrieve an account object that matches a specific account number
                manager.getAccount(); break;

            case 3: // create a new account
                str = "A" + numberGenerator();
                manager.createAccount(str); break;

            case 4:  // updates specific account’s files with data stored in memory
                manager.updateAccount();  break;

            case 5:  // add draft lodging reservation to an account

                manager.makeReservation(numberGenerator());  break;

            case 6:  // complete reservation that is associated with an account
                manager.completeReservation();  break;

            case 7:  // cancel reservation that is associated with an account
                manager.cancelReservation();    break;

            case 8:  // change reservation values that can be changed
                manager.updateReservation();    break;

            case 9:  // request for price per night to be calculated and returned for a specific reservation
                manager.pricePerNight();    break;

            case 10: // Request for total reservation price to be calculated and returned for a specific reservation
                manager.totalReservationPrice();    break;
            case 0: // enter 0 to exit
                System.exit(0); break;
            default:
                System.exit(0); break;
        }
    }


    //=============================================================
    // Display Menu
    //=============================================================
    public static void displayMenu(Manager manager) throws IOException {

        System.out.println("\nWelcome to DONALD KEITA's Rental Reservation System for Hotel, House, and Cabin! ");
        System.out.println("\nPlease enter:");
        System.out.println("1 to get the list of loaded accounts from Manager");
        System.out.println("2 to retrieve a loaded account object that matches a specific account number");
        System.out.println("3 to add new account object to the list managed by Manager");
        System.out.println("4 to request that Manager updates specific account’s files with data stored in memory");
        System.out.println("5 to add draft lodging reservation to an account");
        System.out.println("6 to complete reservation that is associated with an account");
        System.out.println("7 to cancel reservation that is associated with an account");
        System.out.println("8 to change reservation values that can be changed");
        System.out.println("9 to request for price per night to be calculated and returned for a specific reservation");
        System.out.println("10 to request for total reservation price to be calculated and returned for a specific reservation");
        System.out.println("Otherwise enter 0 or any non-matching number or character to exit");

        System.out.print("\nChoose an option --> : ");
        int option = scan.nextInt();
        scan.nextLine();
        options(option, manager);
    }


    public static void main(String[] args) {
        Manager manager = new Manager();
        do {
            try {
                displayMenu(manager);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while(true);

    }
}