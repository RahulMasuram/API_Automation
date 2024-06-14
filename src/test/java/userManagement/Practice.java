package userManagement;

import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;
import pojo.postRequestBody;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;


import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Practice {

    @Test
    void practiceAPI() {
        // Set base URI for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";

        // Create a Map to hold headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer <your_token_here>");

        // Set up the cookies to send with the request
        Map<String, String> cookies = new HashMap<>();
        cookies.put("cookie1", "value1");
        cookies.put("cookie2", "value2");

//        postRequestBody postRequest = new postRequestBody();
//        postRequest.setJob("leader");
//        postRequest.setName("morpheus");
//        postRequest.setLanguages(listLanguage);
//        postRequest.setCityRequestBody(cityRequests);

        // Send a GET request and validate the response body using 'then'
        RestAssured.given()
                .auth()
                .digest("postman", "password")// digest authentication
                // .basic("postman", "password")  //basic authentication

                .header("Content-Type", "application/json") //we can have multiple headers
                .header("Authorization", "bearer ywtefdu13tx4fdub1t3ygdxuy3gnx1iuwdheni1u3y4gfuy1t3bx")
                .headers(headers)  //use Map in sending multiple headers

                .queryParam("page", 2)  //we can have Single or multiple query param
                .queryParam("per_page", 3)
                .queryParam("rtqsdr", 4)
                .pathParam("raceSeason", 2017)  // Automate path param
                .contentType("application/x-www-form-urlencoded")  //form param
                .formParam("name", "John Doe")
                .formParam("job", "Developer")

                .cookies(cookies)

                .multiPart("file", "G:\\Automation Testing\\RestAssured\\rahul.txt") // To upload a file

                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }")  //Sending Request Body as String
                //Sending Request Body as File
                // .body(IOUtils.toString(new FileInputStream(new File(System.getProperty("user.dir") + "/resources/TestData/postRequestBody.JSON"))))
                // .body(postRequest) // sending object as a body - serialization
                // postRequestBody responseBody = response.as(postRequestBody.class);
                // We then deserialize the response to a postRequestBody object using the as() method and passing in the postRequestBody.class argument.

          .when()
                .get("/todos/1")
                //.delete("https://reqres.in/api/users/2")

          .then()
                .assertThat()
                .statusCode(200)
                .body(not(isEmptyString()))
                .body("title", equalTo("delectus aut autem"))
                .body("userId", equalTo(1))
                .body("data[0].first_name", equalTo("Eve"))  //here, "data is an Array of objects
                .body("data[0].first_name", is("Eve"))
                .body("data[0].last_name", equalTo("Holt"))
                .body("cookies.cookie1", equalTo("value1")) // accessing the cookie value and performing validations
                .body("cookies.cookie2", equalTo("value2"))

                //handle headers validation in response
                .header("Content-Type", equalTo("application/json; charset=utf-8"))
                .header("Cache-Control", equalTo("max-age=14400"))
                .headers("Server", "cloudflare", "CF-RAY", notNullValue())
                .log().all(); //print the entire response to the console for debugging purposes

    }

    @Test
    public void validateGetResponseBody() {
        // Set base URI for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        // Send a GET request and store the response in a variable
        Response response = given()
                .multiPart("file", "G:\\Automation Testing\\RestAssured\\rahul.txt") // To upload a file
                .when()
                .get("/todos/1");
                // .delete("https://reqres.in/api/users/2")

        /* - optional
        .then()
        .extract()
        .response();
         */

        Headers headers = response.getHeaders();
        //if you want to print all headers
        for (Header h : headers) {
            if (h.getName().contains("Server")) {
                System.out.println(h.getName() + " : " + h.getValue());
                assertEquals(h.getValue(), "cloudflare");
                System.out.println("testFetchHeaders Executed Successfully");
            }
        }

        Map<String, String> cookies = response.getCookies();
        Cookies cookies1 = response.getDetailedCookies();
        cookies1.getValue("server");
        assertEquals(cookies1.getValue("server"), "cloudflare");
        assertThat(cookies, hasKey("JSESSIONID"));
        assertThat(cookies, hasValue("ABCDEF123456"));


        //To print the response body in the console
        System.out.println(response.body().asString());

        //Validate the response code
        assertEquals(response.getStatusCode(),204);
        // Validate that the response body is not empty
        assertThat(response.getBody().asString(), not(isEmptyString()));
        // Validate that the response contains a specific value
        assertThat(response.getBody().asString(), containsString("delectus aut autem"));
        // Validate that the response has a specific JSON attribute
        assertThat(response.getBody().jsonPath().get("userId"), equalTo(1));
        // Validate that the response has a specific XML element (if applicable)
        assertThat(response.getBody().xmlPath().get("element"),equalTo("expectedValue"));

        // Use Hamcrest to check that the response body contains specific items
        assertThat(response.jsonPath().getList("title"), hasItems("sunt aut facere repellat", "qui est esse"));
        // Use Hamcrest to check that the response body has a specific size
        assertThat(response.jsonPath().getList(""), hasSize(500));

        // Use Hamcrest to check that the response body contains specific items
        List<String> expectedNames = Arrays.asList("Leanne Graham", "Ervin Howell", "Clementine Bauch");
        assertThat(response.jsonPath().getList("name"), hasItems(expectedNames.toArray(new String[0])));

        // Use Hamcrest to check that the response body contains specific items in a specific order
        List<String> expectedEmails = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz","Lew@alysha.tv","Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmails.toArray(new String[0])));


    }
}
