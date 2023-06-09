package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project;

import io.qameta.allure.Step;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.FakePropertiesGenerator;

import org.apache.http.HttpStatus;
import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetAllProjectsEndpoint extends BaseEndPoint <GetAllProjectsEndpoint, Project[]>{


    @Override
    protected Type getModelType() {
        return Project[].class;
    }

    @Step("Get all projects assigned to a tenant")
    @Override
    public GetAllProjectsEndpoint sendRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                   .when()
                        .get("projects/{tenantId}");
        return this;
    }
    @Step("Try to get all projects from invalid tenant")
    public GetAllProjectsEndpoint sendInvalidRequest() {
        FakePropertiesGenerator fakeTenantIdGenerator = new FakePropertiesGenerator();

        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("fakeTenantId", fakeTenantIdGenerator.generateFakeUuid())
                    .when()
                        .get("projects/{fakeTenantId}");
        return this;
    }
    @Step("Try to get all projects without authorization")
    public GetAllProjectsEndpoint sendUnauthorizedRequest() {
        FakePropertiesGenerator fakePropertyGenerator = new FakePropertiesGenerator();

        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .auth().oauth2(fakePropertyGenerator.generateFakeToken())
                    .when()
                        .get("projects/{tenantId}/");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}