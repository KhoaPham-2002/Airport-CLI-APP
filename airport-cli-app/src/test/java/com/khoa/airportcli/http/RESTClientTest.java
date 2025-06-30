//Name: Khoa Pham
//Project: Midterm Sprint (Airport-CLI-App)
//Date: 06/29/2025

package com.khoa.airportcli.http;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.khoa.airportcli.domain.Aircraft;
import com.khoa.airportcli.domain.Airport;
import com.khoa.airportcli.domain.City;
import com.khoa.airportcli.domain.Passenger;
import com.khoa.airportcli.http.client.RESTClient;

public class RESTClientTest {

    private RESTClient restClient;

    // A subclass of RESTClient for mocking sendRequest
    private class TestableRESTClient extends RESTClient {
        private HttpResponse<String> mockedResponse;

        public TestableRESTClient(String serverURL) {
            super(serverURL);
        }

        public void setMockedResponse(HttpResponse<String> response) {
            this.mockedResponse = response;
        }

        @Override
        protected HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
            if (mockedResponse == null) {
                throw new IOException("No mock response set");
            }
            return mockedResponse;
        }
    }

    @BeforeEach
    void setup() {
        restClient = new TestableRESTClient("http://localhost:8080");
    }

    @Test
    void testGetAllPassengers_success() throws Exception {
        // Prepare sample JSON response body for passengers
        String jsonResponse = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Doe\"}]";

        // Mock HttpResponse
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        // Inject mocked response into restClient
        ((TestableRESTClient) restClient).setMockedResponse(mockResponse);

        List<Passenger> passengers = restClient.getAllPassengers();

        assertNotNull(passengers);
        assertEquals(1, passengers.size());
        assertEquals("John", passengers.get(0).getFirstName());
        assertEquals("Doe", passengers.get(0).getLastName());
    }

    @Test
    void testGetAllPassengers_failureReturnsEmpty() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);  // Simulate server error
        when(mockResponse.body()).thenReturn("");

        ((TestableRESTClient) restClient).setMockedResponse(mockResponse);

        List<Passenger> passengers = restClient.getAllPassengers();
        assertNotNull(passengers);
        assertTrue(passengers.isEmpty());
    }

    @Test
    void testGetAllAircraft_success() throws Exception {
        String jsonResponse = "[{\"id\":1,\"type\":\"Boeing 737\",\"airlineName\":\"Air Canada\"}]";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        ((TestableRESTClient) restClient).setMockedResponse(mockResponse);

        List<Aircraft> aircraftList = restClient.getAllAircraft();

        assertNotNull(aircraftList);
        assertEquals(1, aircraftList.size());
        assertEquals("Boeing 737", aircraftList.get(0).getType());
        assertEquals("Air Canada", aircraftList.get(0).getAirlineName());
    }

    @Test
    void testGetAllAirports_success() throws Exception {
        String jsonResponse = "[{\"id\":1,\"name\":\"Pearson\",\"code\":\"YYZ\"}]";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        ((TestableRESTClient) restClient).setMockedResponse(mockResponse);

        List<Airport> airports = restClient.getAllAirports();

        assertNotNull(airports);
        assertEquals(1, airports.size());
        assertEquals("Pearson", airports.get(0).getName());
        assertEquals("YYZ", airports.get(0).getCode());
    }

    @Test
    void testGetAllCities_success() throws Exception {
        String jsonResponse = "[{\"id\":1,\"name\":\"Toronto\",\"state\":\"Ontario\",\"population\":2800000}]";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        ((TestableRESTClient) restClient).setMockedResponse(mockResponse);

        List<City> cities = restClient.getAllCities();

        assertNotNull(cities);
        assertEquals(1, cities.size());
        assertEquals("Toronto", cities.get(0).getName());
        assertEquals("Ontario", cities.get(0).getState());
        assertEquals(2800000, cities.get(0).getPopulation());
    }
}

