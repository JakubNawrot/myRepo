package net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.token.GetTokenEndpoint;
import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.BeforeSuite;

public class SuiteTestBase {

    @BeforeSuite
    public void setupConfiguration() throws Exception {
        EnvironmentConfig environmentConfig = ConfigFactory.create(EnvironmentConfig.class);

        RestAssured.baseURI = environmentConfig.baseUri();
        RestAssured.basePath = environmentConfig.basePath();

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter(), new AllureRestAssured());

        String token = new GetTokenEndpoint()
                .sendRequest()
                .assertRequestSuccess().extractTokenFromResponse();

        RestAssured.authentication = RestAssured.oauth2(token);
    }
}