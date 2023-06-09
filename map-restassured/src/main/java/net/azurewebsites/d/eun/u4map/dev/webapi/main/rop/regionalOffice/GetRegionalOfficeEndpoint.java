package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetRegionalOfficeEndpoint extends BaseEndPoint<GetRegionalOfficeEndpoint, RegionalOffice> {
    private String regionalOfficeId;

    public GetRegionalOfficeEndpoint setRegionalOfficeId(String regionalOfficeId) {
        this.regionalOfficeId = regionalOfficeId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return RegionalOffice.class;
    }

    @Override
    public GetRegionalOfficeEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .pathParam("regionalOfficeId", regionalOfficeId)
                    .when()
                        .get("RegionalOffice/{tenantId}/{regionalOfficeId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
