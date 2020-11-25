package HW1;

import Utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;
public class QuestionsAndAnswers {

    @BeforeClass
    public void baseURL(){
        baseURI = ConfigurationReader.get("hr_api_url");
    }

    /* ORDS API:
    Q1:
     - Given accept type is Json
     - Path param value- US
     - When users sends request to /countries
     - Then status code is 200
     - And Content - Type is Json
     - And country_id is US
     - And Country_name is United States of America
     - And Region_id is 2
     */

    @Test
    public void question_1(){
        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .pathParam("id","US")
                .get("/countries/{id}");
        assertEquals(response.getStatusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertEquals(response.path("country_id"),"US");
        assertEquals(response.path("country_name"),"United States of America");
        assertEquals(response.path("region_id"),(Integer) 2);

    }

    /*Q2:
    - Given accept type is Json
    - Query param value - q={"department_id":80}
    - When users sends request to /employees
    - Then status code is 200
    - And Content - Type is Json
    - And all job_ids start with 'SA'
    - And all department_ids are 80
    - Count is 25
    */

    @Test
    public void question2(){
        Response response = given()
                .accept(ContentType.JSON)
                .and()
                .queryParam("q","{\"department_id\":80}")
                .when()
                .get("/employees");
        assertEquals(response.getStatusCode(),200);
        assertEquals(response.contentType(),"application/json");
        List<String> jobIds = response.path("items.job_id");
        for (String jobId : jobIds) {
            assertTrue(jobId.substring(0,2).equals("SA"));
        }
        assertTrue(response.path("count").equals((Integer) 25));

    }
    /*Q3:
    - Given accept type is Json
    - Query param value q= region_id 3
    - When users sends request to /countries
    - Then status code is 200
    - And all regions_id is 3
    - And count is 6
    - And hasMore is false
    - And Country_name are;
    Australia,China,India,Japan,Malaysia,Singapore
    */

    @Test
    public void question3(){
        Response response = given()
                .accept(ContentType.JSON)
                .and()
                .queryParam("q","{\"region_id\":\"3\"}")
                .when()
                .get("/countries");
        List<Integer> regionIDs = response.path("items.region_id");
        for (Integer regionID : regionIDs) {
            assertTrue(regionID.equals((Integer) 3));
        }
        assertTrue(response.path("count").equals((Integer) 6 ));
        assertFalse(response.path("hasMore"));
        List<String> actualCountries = response.path("items.country_name");
        List<String> expectedCountries = new ArrayList<>(Arrays.asList("Australia","China","India","Japan","Malaysia","Singapore"));
        assertEquals(actualCountries,expectedCountries);
    }

    //Pinar abla'nin cözümü
    @Test
    public void tes2() {
/*
        Q3:
        - Given accept type is Json
        -Query param value q= region_id 3
                - When users sends request to /countries
                - Then status code is 200
                - And all regions_id is 3
                - And count is 6
                - And hasMore is false
                - And Country_name are;
        Australia,China,India,Japan,Malaysia,Singapore
                */
        Response response=given()
                .accept(ContentType.JSON)
                .and()
                .queryParam("q","{\"region_id\":\"3\"}")
                .when()
                .get("/countries");
        response.prettyPeek();
        assertEquals(response.statusCode(),200);
        assertTrue(response.contentType().equals("application/json"));
        List<Integer> regionIDs = response.path("items.region_id");
        for (int  regionID: regionIDs) {
            assertEquals(regionID,3);
        }
        JsonPath jsonPath = response.jsonPath();
        int count = jsonPath.getInt("count");
        assertEquals(count,6);
        boolean hasMore = jsonPath.getBoolean("hasMore");
        assertEquals(hasMore,false);
        List<String> countryNames=response.path("items.country_name");
        assertEquals(countryNames.toString(),"[Australia, China, India, Japan, Malaysia, Singapore]");
    }
}



