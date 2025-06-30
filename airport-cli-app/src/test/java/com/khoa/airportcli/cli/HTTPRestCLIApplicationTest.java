//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.khoa.airportcli.domain.Aircraft;
import com.khoa.airportcli.domain.Airport;
import com.khoa.airportcli.domain.City;
import com.khoa.airportcli.domain.Passenger;
import com.khoa.airportcli.http.cli.HTTPRestCLIApplication;
import com.khoa.airportcli.http.client.RESTClient;

public class HTTPRestCLIApplicationTest {

    @Mock
    private RESTClient mockRestClient;

    @InjectMocks
    private HTTPRestCLIApplication httpRestCLIApplicationTest;

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testPrintAirportsGroupedByCity() {
        City toronto = new City();
        toronto.setName("Toronto");

        Airport airport1 = new Airport();
        airport1.setName("Pearson");
        airport1.setCode("YYZ");
        airport1.setCity(toronto);

        Airport airport2 = new Airport();
        airport2.setName("Island");
        airport2.setCode("YTZ");
        airport2.setCity(toronto);

        when(mockRestClient.getAllAirports()).thenReturn(List.of(airport1, airport2));

        httpRestCLIApplicationTest.printAirportsGroupedByCity();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("City: Toronto"));
        Assertions.assertTrue(output.contains("Pearson (YYZ)"));
        Assertions.assertTrue(output.contains("Island (YTZ)"));
    }

    @Test
    void testPrintAircraftByPassenger_WithAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setType("Boeing 737");
        aircraft.setAirlineName("Air Canada");

        Passenger passenger = new Passenger();
        passenger.setFirstName("Khoa");
        passenger.setLastName("Pham");
        passenger.setAircraft(Set.of(aircraft));

        when(mockRestClient.getAllPassengers()).thenReturn(List.of(passenger));

        httpRestCLIApplicationTest.printAircraftByPassenger();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Passenger: Khoa Pham"));
        Assertions.assertTrue(output.contains("Boeing 737 (Airline: Air Canada)"));
    }

    @Test
    void testPrintAircraftByPassenger_NoAircraft() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("Jane");
        passenger.setLastName("Doe");
        passenger.setAircraft(Collections.emptySet());

        when(mockRestClient.getAllPassengers()).thenReturn(List.of(passenger));

        httpRestCLIApplicationTest.printAircraftByPassenger();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Passenger: Jane Doe"));
        Assertions.assertTrue(output.contains("No flights recorded"));
    }

    @Test
    void testPrintAirportsByAircraft_WithAirports() {
        Airport airport = new Airport();
        airport.setName("Pearson");
        airport.setCode("YYZ");
        airport.setCity(null);  
        Aircraft aircraft = new Aircraft();
        aircraft.setType("Boeing 777");
        aircraft.setAirlineName("Air Canada");
        aircraft.setAirports(List.of(airport));

        when(mockRestClient.getAllAircraft()).thenReturn(List.of(aircraft));

        httpRestCLIApplicationTest.printAirportsByAircraft();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Aircraft: Boeing 777 (Airline: Air Canada)"));
        Assertions.assertTrue(output.contains("Pearson (YYZ)"));
    }


    @Test
    void testPrintAirportsByAircraft_EmptyList() {
        when(mockRestClient.getAllAircraft()).thenReturn(Collections.emptyList());

        httpRestCLIApplicationTest.printAirportsByAircraft();

        String output = outContent.toString().trim();
        Assertions.assertEquals("No aircraft found.", output);
    }

    @Test
    void testPrintAirportsUsedByPassengers() {
        Airport airport = new Airport();
        airport.setName("Pearson");
        airport.setCode("YYZ");
        airport.setCity(null);  

        Aircraft aircraft = new Aircraft();
        aircraft.setAirports(List.of(airport));

        Passenger passenger = new Passenger();
        passenger.setFirstName("Alice");
        passenger.setLastName("Smith");
        passenger.setAircraft(Set.of(aircraft));

        when(mockRestClient.getAllPassengers()).thenReturn(List.of(passenger));

        httpRestCLIApplicationTest.printAirportsUsedByPassengers();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Passenger: Alice Smith"));
        Assertions.assertTrue(output.contains("Airports used:"));
        Assertions.assertTrue(output.contains("Pearson (YYZ)"));
    }

    @Test
    void testPrintAirportsUsedByPassengers_NoFlights() {
        Passenger passenger = new Passenger();
        passenger.setFirstName("Bob");
        passenger.setLastName("Jones");
        passenger.setAircraft(Collections.emptySet());

        when(mockRestClient.getAllPassengers()).thenReturn(List.of(passenger));

        httpRestCLIApplicationTest.printAirportsUsedByPassengers();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("Passenger: Bob Jones"));
        Assertions.assertTrue(output.contains("No airports used."));
    }

    @Test
    void testPrintAirportsUsedByPassengers_EmptyList() {
        when(mockRestClient.getAllPassengers()).thenReturn(Collections.emptyList());

        httpRestCLIApplicationTest.printAirportsUsedByPassengers();

        String output = outContent.toString();
        Assertions.assertTrue(output.contains("No passenger found."));
    }
}
