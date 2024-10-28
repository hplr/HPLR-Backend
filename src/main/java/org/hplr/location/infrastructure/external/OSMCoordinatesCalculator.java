package org.hplr.location.infrastructure.external;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.hplr.library.exception.LocationCalculationException;

public class OSMCoordinatesCalculator {

    private OSMCoordinatesCalculator() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Double> getCoordinatesFromAddress(String country, String city, String street, String houseNumber) throws LocationCalculationException, IOException, InterruptedException {
        Map<String, Double> coordinates = new HashMap<>();
        String address = country + "," + city + "," + street + "," + houseNumber;
        String apiUrl = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&format=json";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray array;
            try {
                array = new JSONArray(response.body());
            } catch (JSONException e) {
                throw new LocationCalculationException("Timeout on API!");
            }
            JSONObject object = array.getJSONObject(0);
            if (object.has("place_id")) {
                coordinates.put("lat", object.getDouble("lat"));
                coordinates.put("lon", object.getDouble("lon"));

                return coordinates;
            } else {
                throw new LocationCalculationException("Couldn't determine coordinates for given address");
            }
        }

    }
}
