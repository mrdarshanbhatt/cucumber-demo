package mission;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class ApiClient {

    String apiKey = null;
    String apiKeyValue = null;

    public ApiClient(String baseUri) {
        RestAssured.baseURI = baseUri;
        apiKey = LoadProp.prop.get("api-key").toString();
        apiKeyValue = LoadProp.prop.get("api-key-value").toString();
    }

    public Response get(String path, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return RestAssured.given().header(apiKey, apiKeyValue).contentType(ContentType.JSON).when().get(path).andReturn();
        }
        return RestAssured.given().queryParams(params).contentType(ContentType.JSON).when().get(path).andReturn();
    }

    public Response post(String path, Object body) {
        return RestAssured.given().header(apiKey, apiKeyValue).contentType(ContentType.JSON).body(body).when().post(path).andReturn();
    }
}
