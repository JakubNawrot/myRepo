package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetProjectsEstimatesEndpoint extends BaseEndPoint<GetProjectsEstimatesEndpoint, Estimate[]> {
    private String projectId;

    public GetProjectsEstimatesEndpoint setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Estimate[].class;
    }

    @Override
    public GetProjectsEstimatesEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                .pathParam("tenantId", tenantId)
                .pathParam("projectId", projectId)
                .when().get("estimates/{tenantId}/project/{projectId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
