package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project;

import io.qameta.allure.Step;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import org.apache.http.HttpStatus;
import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetProjectCountEndpoint extends BaseEndPoint <GetProjectCountEndpoint, Integer> {

    @Override
    protected Type getModelType() {
        return Integer.class;
    }

    @Step("Count the projects assigned to tenant")
    @Override
    public GetProjectCountEndpoint sendRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .pathParam("counter", "count")
                    .when().get("projects/{tenantId}/{counter}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
