package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.token;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.aeonbits.owner.ConfigFactory;
import org.apache.http.HttpStatus;
import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetTokenEndpoint extends BaseEndPoint<GetTokenEndpoint, String> {


    @Override
    protected Type getModelType() {
        return String.class;
    }

    public String extractTokenFromResponse() {
        return response.then().extract().jsonPath().getString("access_token");

    }
    @Step("Get access token")
    @Override
    public GetTokenEndpoint sendRequest() {
        EnvironmentConfig environmentConfig = ConfigFactory.create(EnvironmentConfig.class);

        response = given().contentType(ContentType.URLENC)
                .auth().preemptive().basic(environmentConfig.getClientId(), environmentConfig.getClientSecret())
                .param("grant_type", "client_credentials")
                .when().post(environmentConfig.getAccessTokenUrl());
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
