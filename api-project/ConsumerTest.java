package liveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String,String> headers = new HashMap<>();
    //Resource path
    String resourcePath = "/api/users";

    //create the contract
    @Pact(consumer = "UserConsumer",provider = "userProvider")
    public RequestResponsePact consumerTest(PactDslWithProvider builder){
        // set the headers
        headers.put("Content-Type", "application/json");

        //Create the request and response body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",123)
                .stringType("firstName", "Adithya")
                .stringType("lastName", "kk")
                .stringType("email", "test@test.com");

        //Create the contract
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                .method("POST")
                .path(resourcePath)
                .headers(headers)
                .body(requestResponseBody)
                .willRespondWith()
                .status(201)
                .body(requestResponseBody)
                .toPact();
    }
@Test
    @PactTestFor(providerName = "userProvider", port = "8282")
    public void consumeTest(){
        // Set the baseURI
    String baseURI = "http://localhost:8282"+resourcePath;

    //set the request body
    Map<String, Object> reqBody = new HashMap<>();
    reqBody.put("id", 123);
    reqBody.put("firstName", "Adithya");
    reqBody.put("lastName", "kk");
    reqBody.put("email", "test@test.com");

    //Generate response and assert
    given().headers(headers).body(reqBody).log().all().
            when().post(baseURI).
            then().statusCode(201).log().all();



}


}
