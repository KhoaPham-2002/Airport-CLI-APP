//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.http.client;

import com.khoa.airportcli.domain.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.ArrayList;
import java.util.List;

public class RESTClient {

    private final String serverURL;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public RESTClient(String serverURL) {
        this.serverURL = serverURL;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.httpClient = HttpClient.newHttpClient();
    }

    // Generic request sender
    protected HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response;
        } else {
            System.err.println("HTTP Error: " + response.statusCode());
            throw new IOException("Failed request with status: " + response.statusCode());
        }
    }

    // GET /passengers
    public List<Passenger> getAllPassengers() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + "/passenger"))
                    .GET()
                    .build();

            HttpResponse<String> response = sendRequest(request);

            return objectMapper.readValue(response.body(), new TypeReference<List<Passenger>>() {});
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch passenger: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // GET /aircrafts
    public List<Aircraft> getAllAircraft() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + "/aircraft"))
                    .GET()
                    .build();

            HttpResponse<String> response = sendRequest(request);

            return objectMapper.readValue(response.body(), new TypeReference<List<Aircraft>>() {});
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch aircraft: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // GET /airport
    public List<Airport> getAllAirports() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + "/airport"))
                    .GET()
                    .build();

            HttpResponse<String> response = sendRequest(request);

            return objectMapper.readValue(response.body(), new TypeReference<List<Airport>>() {});
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch airports: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Optional: If needed later
    public List<City> getAllCities() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverURL + "/cities"))
                    .GET()
                    .build();

            HttpResponse<String> response = sendRequest(request);

            return objectMapper.readValue(response.body(), new TypeReference<List<City>>() {});
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch cities: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

