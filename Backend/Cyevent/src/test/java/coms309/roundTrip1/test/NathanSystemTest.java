package coms309.roundTrip1.test;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class NathanSystemTest {

    @LocalServerPort
    int port = 8080;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void eventTest() throws JSONException {
        // Prepare the JSON body
        String requestBody = """
            {
                "name": "Music Concert",
                "description": "Rock music",
                "date": "12/23/2024",
                "time": "8pm",
                "location": "Grandstands",
                "organizer": {
                    "id": 3
                }
            }
        """;

        // Send the POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/events");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body
        String responseBody = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Assert main fields in response
        assertEquals("Event created successfully", jsonResponse.getString("message"));

        JSONObject event = jsonResponse.getJSONObject("Event");
        assertEquals(104, event.getInt("id"));
        assertEquals("Music Concert", event.getString("name"));
        assertEquals("Rock music", event.getString("description"));
        assertEquals("12/23/2024", event.getString("date"));
        assertEquals("8pm", event.getString("time"));
        assertEquals("Grandstands", event.getString("location"));

        JSONObject organizer = event.getJSONObject("organizer");
        assertEquals(3, organizer.getInt("id"));
        assertEquals(JSONObject.NULL, organizer.get("username"));
        assertEquals(JSONObject.NULL, organizer.get("email"));
        assertEquals(JSONObject.NULL, organizer.get("password"));
        assertEquals(JSONObject.NULL, organizer.get("userType"));
        assertEquals(JSONObject.NULL, organizer.get("interests"));

        assertEquals(JSONObject.NULL, event.get("approval"));
    }

    @Test
    public void profileTest() throws JSONException {
        // Prepare the JSON body
        String requestBody = """
            {
                "username": "Tom Goodale",
                "email": "tomgood@gmail.com",
                "password": "t0M@ood"
            }
        """;

        // Send the POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/profiles");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body
        String responseBody = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Validate fields directly at the root
        assertEquals(43, jsonResponse.getInt("id"));
        assertEquals("Tom Goodale", jsonResponse.getString("username"));
        assertEquals("tomgood@gmail.com", jsonResponse.getString("email"));
        assertEquals("t0M@ood", jsonResponse.getString("password"));
    }

    @Test
    public void loginTest() throws JSONException {
        // Prepare the JSON body for login
        String loginRequestBody = """
        {
            "username": "Fred",
            "password": "newPass"
        }
    """;

        // Send the POST request to the login endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginRequestBody)
                .when()
                .post("/login");

        // Check status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Check response body
        String responseBody = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(responseBody);

        assertEquals(33, jsonResponse.getInt("id"));
        assertEquals("Fred", jsonResponse.getString("username"));
        assertEquals("BobyBurner@outlook.com", jsonResponse.getString("email"));
        assertEquals("newPass", jsonResponse.getString("password"));
    }

    @Test
    public void userTypeTest() throws JSONException {
        // Prepare the JSON body for the request
        String requestBody = """
        {
            "type": "TestType"
        }
    """;

        // Send the POST request
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post("/userTypes"); // Replace with your actual endpoint

        // Check the status code
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        // Parse the response body
        String responseBody = response.getBody().asString();
        JSONObject jsonResponse = new JSONObject(responseBody);

        // Validate the returned information
        assertEquals(13, jsonResponse.getInt("id")); // Validate the ID
        assertEquals("TestType", jsonResponse.getString("type")); // Validate the type
    }


}

