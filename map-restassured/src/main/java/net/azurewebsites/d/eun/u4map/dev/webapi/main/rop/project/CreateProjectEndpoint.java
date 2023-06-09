package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project;

import io.qameta.allure.Step;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.FakePropertiesGenerator;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class CreateProjectEndpoint extends BaseEndPoint<CreateProjectEndpoint, Project> {

    private Project project;

    public CreateProjectEndpoint setProject(Project project) {
        this.project = project;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Project.class;
    }

    @Step("Create project")
    @Override
    public CreateProjectEndpoint sendRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .body(project)
                    .when()
                        .post("projects/{tenantId}");
        return this;
    }

    public CreateProjectEndpoint sendInvalidRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("fakeTenantId", new FakePropertiesGenerator().generateFakeUuid())
                        .body(project)
                    .when()
                        .post("projects/{fakeTenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_CREATED;
    }
}
