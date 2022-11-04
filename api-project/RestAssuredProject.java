package liveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class RestAssuredProject {

    //Request specification
    RequestSpecification requestSpec;
    String key;
    int keyId;
    @BeforeClass
    public void setUp(){
        //Request specification
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://api.github.com/user/keys")
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization","token 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")
                .build();

    }
    @Test(priority  =0)
    public void postRequestTest(){
        //Request Body
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key","ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCBXAnuu5qPgxB5xcKA1NJHqT7LnpHzDX670fyuIvhUkiKhWytETy2hHbVZpKPRbjx0nL92eHQ18jSOARiVQAUE23lv024x6N+vGUeJICzGu385xXP5HcmQEj8rtwqqiqGtlTeirKceWT4h3E+7IlfxA0ideL/i8iHweK2nf2jUMX2XRW1SMzkumZqX21ip+kTMHgXg1s0vmSAzW2ie4yhpnRZwH8ZVGqS4BBaXTD3w7kYeNdlFF+a5YwmuBf8ShsKKRhEV0dgJK3JcJ3kTH6qGNc6BMN6O7x47W/deGZPq7tXAdnRMl55atXF/HmPe+llSjyugn1KnvsTrUI0ydyd1");
        //Generate response
        Response response =
                given().spec(requestSpec).body(reqBody).log().all().when().post();
        System.out.println(response.getBody().asPrettyString());
        Reporter.log(response.getBody().asPrettyString());
        // extract key id from response
        keyId = response.then().extract().path("id");
        //Assertions
        response.then().statusCode(201);
    }

    @Test(priority = 1)
    public void getRequestTest(){
        //Generate response
       Response getResponse =  given().spec(requestSpec).pathParam("keyId",keyId).log().all().
                when().get("/{keyId}");
        System.out.println(getResponse.getBody().asPrettyString());
        Reporter.log(getResponse.getBody().asPrettyString());
        getResponse.then().statusCode(200);

    }
    @Test(priority = 2)
    public void deleteRequestTest(){
        //Generate response
        Response deleteResponse = given().spec(requestSpec).pathParam("keyId",keyId).log().all().
                when().delete("/{keyId}");
        System.out.println(deleteResponse.getBody().asPrettyString());
        Reporter.log(deleteResponse.getBody().asPrettyString());
        deleteResponse.then().statusCode(204);

    }
}
