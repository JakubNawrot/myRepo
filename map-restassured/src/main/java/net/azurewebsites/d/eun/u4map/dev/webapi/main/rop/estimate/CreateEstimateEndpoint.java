package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class CreateEstimateEndpoint extends BaseEndPoint<CreateEstimateEndpoint, Estimate> {

    private Estimate estimate;

    public CreateEstimateEndpoint setEstimate(Estimate estimate) {
        this.estimate = estimate;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Estimate.class;
    }

    @Override
    public CreateEstimateEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                .body(estimate)
                .pathParam("tenantId", tenantId)
                .when().post("estimates/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_CREATED;
    }
}
