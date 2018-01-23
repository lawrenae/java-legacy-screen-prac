package com.neopragma.legacy.screen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws URISyntaxException, IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Please enter info about a job candidate or 'quit' to quit");

            System.out.println("First name?");
            String firstName = scanner.nextLine();
            if (firstName.equals("quit")) {
                scanner.close();
                System.out.println("Bye-bye!");
                break;
            }

            System.out.println("Middle name?");
            String middleName = scanner.nextLine();

            System.out.println("Last name?");
            String lastName = scanner.nextLine();

            System.out.println("SSN?");
            String ssn = scanner.nextLine();

            System.out.println("Zip Code?");
            String zipCode = scanner.nextLine();

            JobApplicant jobApplicant = new JobApplicant();
            jobApplicant.setName(firstName, middleName, lastName);
            jobApplicant.setSsn(ssn);
            jobApplicant.setZipCode(zipCode);
            jobApplicant.save();
        }
    }
}
