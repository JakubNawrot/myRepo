package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project;

import io.qameta.allure.Step;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetProjectEndpoint extends BaseEndPoint <GetProjectEndpoint, Project> {

    private String projectId;

    public GetProjectEndpoint setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Project.class;
    }
    @Step("Get project")
    @Override
    public GetProjectEndpoint sendRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .pathParam("projectId", projectId)
                    .when()
                        .get("projects/{tenantId}/{projectId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}