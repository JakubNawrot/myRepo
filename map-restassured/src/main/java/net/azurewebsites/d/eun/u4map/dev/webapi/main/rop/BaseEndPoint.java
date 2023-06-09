package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import org.aeonbits.owner.ConfigFactory;
import org.assertj.core.api.Assertions;

import java.lang.reflect.Type;


public abstract class BaseEndPoint <E, M> {
    //field to store the response
    protected Response response;
    //getting tenantID from the EnvironmentConfig property file
    protected String tenantId = ConfigFactory.create(EnvironmentConfig.class).tenantId();

    //returns the TYPE of the POJO class
    protected abstract Type getModelType();

    //sends the request, returns endpoint class
    public abstract E sendRequest();

    //returns response success code
    protected abstract int getSuccessStatusCode();

    //returns response in the model format
    public M getResponseModel() {
        return response.as(getModelType());
    }

    //verifies if status code is success
    public E assertRequestSuccess() {
        return assertStatusCode(getSuccessStatusCode());
    }

    //verifies if the status code is success. Returns endpoint class
    public E assertStatusCode(int statusCode) {
        Assertions.assertThat(response.getStatusCode()).as("Status code").isEqualTo(statusCode);
        return (E) this;
    }

    public E assertBody(String body) {
        Assertions.assertThat(response.getBody().asString()).as("Body").isEqualTo(body);
        return (E) this;
    }

    public JsonPath extractBodyFromResponse() {
        return response.then().extract().jsonPath();
    }
}
