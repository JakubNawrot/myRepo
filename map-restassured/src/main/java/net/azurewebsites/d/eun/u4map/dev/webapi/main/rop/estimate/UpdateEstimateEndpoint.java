package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class UpdateEstimateEndpoint extends BaseEndPoint<UpdateEstimateEndpoint, Estimate> {
    private Estimate estimate;

    public UpdateEstimateEndpoint setEstimate(Estimate estimate) {
        this.estimate = estimate;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Estimate.class;
    }

    @Override
    public UpdateEstimateEndpoint sendRequest() {
        response = given()
                        .spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .body(estimate)
                    .when()
                        .put("estimates/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
