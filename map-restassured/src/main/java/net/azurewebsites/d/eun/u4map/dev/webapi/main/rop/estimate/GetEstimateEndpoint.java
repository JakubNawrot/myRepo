package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;

import static io.restassured.RestAssured.given;

import java.lang.reflect.Type;

import org.apache.http.HttpStatus;

public class GetEstimateEndpoint extends BaseEndPoint<GetEstimateEndpoint, Estimate>{

    private String estimateId;

    public GetEstimateEndpoint setEstimateId(String estimateId) {
        this.estimateId = estimateId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return Estimate.class;
    }

    @Override
    public GetEstimateEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                .pathParam("tenantId", tenantId)
                .pathParam("estimateId",estimateId)
                .when().get("estimates/{tenantId}/{estimateId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
    
}
