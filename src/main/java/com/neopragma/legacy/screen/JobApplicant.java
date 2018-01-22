package com.neopragma.legacy.screen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * Job applicant class.
 */
public class JobApplicant {

    private String firstName = null;
    private String middleName = null;
    private String lastName = null;
    private String ssn;
    private String city;
    private String state;
    private String zipCode;

    private String[] specialCases = new String[] {
            "219099999",
            "078051120"
    };
    private ZipCodeSearch zipCodeSearch;

    public JobApplicant() {
        zipCodeSearch = new ZipCodeSearch();
    }

    public JobApplicant(ZipCodeSearch zipCodeSearch) {
        this.zipCodeSearch = zipCodeSearch;
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        JobApplicant jobApplicant = new JobApplicant();
        Scanner scanner = new Scanner(System.in);
        String firstName;
        String middleName;
        String lastName;
        String ssn;
        String zipCode;
        while (true) {
            System.out.println("Please enter info about a job candidate or 'quit' to quit");
            System.out.println("First name?");
            firstName = scanner.nextLine();
            if (firstName.equals("quit")) {
                scanner.close();
                System.out.println("Bye-bye!");
                break;
            }
            System.out.println("Middle name?");
            middleName = scanner.nextLine();
            System.out.println("Last name?");
            lastName = scanner.nextLine();
            System.out.println("SSN?");
            ssn = scanner.nextLine();
            System.out.println("Zip Code?");
            zipCode = scanner.nextLine();
            jobApplicant.setName(firstName, middleName, lastName);
            jobApplicant.setSsn(ssn);
            jobApplicant.setZipCode(zipCode);
            jobApplicant.save();
        }
    }

    void setName(String firstName, String middleName, String lastName) {
        this.firstName = emptyStringIfNull(firstName);
        this.middleName = emptyStringIfNull(middleName);
        this.lastName = emptyStringIfNull(lastName);
    }

    void setSpanishName(String primerNombre, String segundoNombre,
                        String primerApellido, String segundoApellido) {
        this.firstName = emptyStringIfNull(primerNombre);
        this.middleName = emptyStringIfNull(segundoNombre);
        if (primerApellido != null) {
            StringBuilder sb = new StringBuilder(primerApellido);
            sb.append(segundoApellido == null ? null : " " + segundoApellido);
            this.lastName = sb.toString();
        } else {
            this.lastName = "";
        }
    }

    String formatLastNameFirst() {
        StringBuilder sb = new StringBuilder(lastName);
        sb.append(", ");
        sb.append(firstName);
        if (middleName.length() > 0) {
            sb.append(" ");
            sb.append(middleName);
        }
        return sb.toString();
    }

    boolean validateName() {
        return firstName.length() > 0 && lastName.length() > 0;
    }

    void setSsn(String ssn) {
        if (ssn.matches("(\\d{3}-\\d{2}-\\d{4}|\\d{9})")) {
            this.ssn = ssn.replaceAll("-", "");
        } else {
            this.ssn = "";
        }
    }

    String formatSsn() {
        StringBuilder sb = new StringBuilder(11);
        sb.append(ssn.substring(0, 3));
        sb.append("-");
        sb.append(ssn.substring(3, 5));
        sb.append("-");
        sb.append(ssn.substring(5));
        return sb.toString();
    }

    boolean validateSsn() {
        if (!ssn.matches("\\d{9}")) {
            return false;
        }

        if ("000".equals(ssn.substring(0, 3)) ||
                "666".equals(ssn.substring(0, 3)) ||
                "9".equals(ssn.substring(0, 1))) {
            return false;
        }

        if ("0000".equals(ssn.substring(5))) {
            return false;
        }

        for (String specialCase : specialCases) {
            if (ssn.equals(specialCase)) {
                return false;
            }
        }
        return true;
    }

    void setZipCode(String zipCode) throws URISyntaxException, IOException {
        this.zipCode = zipCode;
        CityState result = this.zipCodeSearch.find(this.zipCode);
        this.city = result.getCity();
        this.state = result.getState();
    }

    String getCity() {
        return city;
    }

    String getState() {
        return state;
    }

    String getZipCode() {
        return zipCode;
    }

    private void save() {
        //TODO save information to a database
        System.out.println("Saving to database: " + formatLastNameFirst());
    }

    private String emptyStringIfNull(String string) {
        return string == null ? "" : string;
    }
}
