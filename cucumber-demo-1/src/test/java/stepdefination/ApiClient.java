package stepdefination;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class ApiClient {

    public ApiClient(String baseUri) {
        RestAssured.baseURI = baseUri;
    }

    public Response get(String path, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return RestAssured.given().contentType(ContentType.JSON).when().get(path).andReturn();
        }
        return RestAssured.given().queryParams(params).contentType(ContentType.JSON).when().get(path).andReturn();
    }

    public Response post(String path, Object body) {
        return RestAssured.given().contentType(ContentType.JSON).body(body).when().post(path).andReturn();
    }

}
