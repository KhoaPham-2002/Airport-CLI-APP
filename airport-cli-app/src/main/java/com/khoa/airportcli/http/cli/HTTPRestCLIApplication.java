//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.http.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.khoa.airportcli.domain.Aircraft;
import com.khoa.airportcli.domain.Airport;
import com.khoa.airportcli.domain.Passenger;
import com.khoa.airportcli.http.client.RESTClient;

public class HTTPRestCLIApplication {
    private RESTClient restClient;

    public void setRestClient(RESTClient restClient) {
        this.restClient = restClient;
    }

    public RESTClient getRestClient() {
        return restClient;
    }

    // 1. What airports are there in each city?
    public void printAirportsGroupedByCity() {
        List<Airport> airports = restClient.getAllAirports();
        Map<String, List<Airport>> cityAirportMap = new HashMap<>();

        for (Airport airport : airports) {
            String cityName = airport.getCity() != null ? airport.getCity().getName() : "Unknown City";

            cityAirportMap.putIfAbsent(cityName, new ArrayList<>());
            cityAirportMap.get(cityName).add(airport);
        }

        for (String city : cityAirportMap.keySet()) {
            System.out.println("City: " + city);
            for (Airport airport : cityAirportMap.get(city)) {
                System.out.println("  - " + airport.getName() + " (" + airport.getCode() + ")");
            }
        }
    }

    // 2. What aircraft has each passenger flown on?
    public void printAircraftByPassenger() {
        List<Passenger> passengers = restClient.getAllPassengers();

        for (Passenger p : passengers) {
            System.out.println("Passenger: " + p.getFirstName() + " " + p.getLastName());
            if (p.getAircraft() == null || p.getAircraft().isEmpty()) {
                System.out.println("  - No flights recorded.");
            } else {
                for (Aircraft a : p.getAircraft()) {
                    System.out.println("  - " + a.getType() + " (Airline: " + a.getAirlineName() + ")");
                }
            }
        }
    }

    // 3. What airports do aircraft take off from and land at?
    public void printAirportsByAircraft() {
        List<Aircraft> aircraftList = restClient.getAllAircraft();

        if (aircraftList.isEmpty()) {
            System.out.println("No aircraft found.");
            return;
        }

        for (Aircraft aircraft : aircraftList) {
            System.out.println("\nAircraft: " + aircraft.getType() + " (" + aircraft.getAirlineName() + ")");
            List<Airport> airports = aircraft.getAirports(); // ✅ Use List

            if (airports == null || airports.isEmpty()) {
                System.out.println("  - No airport assigned.");
            } else {
                System.out.println("  - Aircraft take off from and land at:");
                for (Airport airport : airports) {
                    System.out.println("     * " + airport.getName() + " (" + airport.getCode() + ")");
                }
            }
        }
    }


    // 4. What airports have passengers used?
    public void printAirportsUsedByPassengers() {
        List<Passenger> passengers = restClient.getAllPassengers();

        if (passengers.isEmpty()) {
            System.out.println("No passenger found.");
            return;
        }

        for (Passenger passenger : passengers) {
            System.out.println("\nPassenger: " + passenger.getFirstName() + " " + passenger.getLastName());

            Set<Aircraft> flights = passenger.getAircraft(); 
            Set<String> airportNames = new HashSet<>();

            if (flights != null && !flights.isEmpty()) {
                for (Aircraft aircraft : flights) {
                    List<Airport> airports = aircraft.getAirports(); // ✅ Use List
                    if (airports != null) {
                        for (Airport airport : airports) {
                            airportNames.add(airport.getName() + " (" + airport.getCode() + ")");
                        }
                    }
                }
            }

            if (airportNames.isEmpty()) {
                System.out.println("  - No airports used.");
            } else {
                System.out.println("  - Airports used:");
                for (String name : airportNames) {
                    System.out.println("     * " + name);
                }
            }
        }
    }

    // Entry point
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HTTPRestCLIApplication app = new HTTPRestCLIApplication();

        System.out.print("Enter API base URL (e.g., http://localhost:8080): ");
        String baseUrl = scanner.nextLine();
        app.setRestClient(new RESTClient(baseUrl));

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. What airports are there in each city?");
            System.out.println("2. What aircraft has each passenger flown on?");
            System.out.println("3. What airports do aircraft take off from and land at?");
            System.out.println("4. What airports have passengers used?");
            System.out.println("5. Exit");

            System.out.print("Your choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> app.printAirportsGroupedByCity();
                case "2" -> app.printAircraftByPassenger();
                case "3" -> app.printAirportsByAircraft();
                case "4" -> app.printAirportsUsedByPassengers();
                case "5" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

