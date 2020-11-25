package HW2;

import Utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import static org.testng.Assert.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class QuestionsAndAnswers {
    @BeforeClass
    public void baseURL(){
        baseURI = ConfigurationReader.get("spartan_api_url");
    }

    /*
    Given accept type is json
    And path param id is 20
    When user sends a get request to "/api/spartans/{id}"
    Then status code is 200
    And content-type is "application/json;charset=UTF-8"
    And response header contains Date
    And Transfer-Encoding is chunked
    And response payload values match the following:
    id is 20,
    name is "Lothario",
    gender is "Male",
    phone is 7551551687
    */

    @Test
    public void question_1(){
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .pathParam("id","20")
                .get("/api/spartans/{id}");
        assertEquals(response.getStatusCode(),200);
        assertTrue(response.contentType().equals("application/json;charset=UTF-8"));
        assertTrue(response.headers().hasHeaderWithName("Date"));
        assertEquals(response.getHeader("Transfer-Encoding"),"chunked");
        assertEquals(response.path("id"),(Integer) 20);
        assertEquals(response.path("name"),"Lothario");
        assertEquals(response.path("gender"),"Male");
        assertEquals(response.path("phone"),(Long) 7551551687l);

    }

    /*Q2:
    Given accept type is json
    And query param gender = Female
    And query param nameContains = r
    When user sends a get request to "/api/spartans/search"
    Then status code is 200
    And content-type is "application/json;charset=UTF-8"
    And all genders are Female
    And all names contains r
    And size is 20
    And totalPages is 1
    And sorted is false
    */

    @Test
    public void question_2(){
        Response response = given()
                .accept(ContentType.JSON)
                .and()
                .queryParam("nameContains","r")
                .and()
                .queryParam("gender","Female")
                .when()
                .get("/api/spartans/search");
        assertEquals(response.statusCode(),200);
        assertTrue(response.contentType().equals("application/json;charset=UTF-8"));
        List<String> genders = response.path("content.gender");
        for (String gender : genders) {
            assertEquals(gender,"Female");
        }

        List<String> names = response.path("content.name");
        for (String name : names) {
            assertTrue(name.toLowerCase().contains("r"));
        }

        assertEquals(response.path("pageable.pageSize"),(Integer) 20);
        assertEquals(response.path("totalPages"),(Integer) 1);

    }

    //nette farkli bir yöntem gördüm
    //onun uygulamasini denedim
    //tek queryParam() metodu icinde birden fazla parametre verilebilir
    @Test
    public void question_2_V2(){
        Response response = given()
                .accept(ContentType.JSON)
                .and()
                .queryParams("nameContains","r","gender","Female")
                .when()
                .get("/api/spartans/search");
        response.prettyPeek();
        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");
        List<String> genders = response.path("content.gender");
        for (String gender : genders) {
            assertEquals(gender,"Female");
        }

        List<String> names = response.path("content.name");
        for (String name : names) {
            assertTrue(name.toLowerCase().contains("r"));
        }

        assertEquals(response.path("pageable.pageSize"),(Integer) 20);
        assertEquals(response.path("totalPages"),(Integer) 1);

    }
}
