package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class UpdateRegionalOfficeEndpoint extends BaseEndPoint<UpdateRegionalOfficeEndpoint, RegionalOffice> {
    private RegionalOffice regionalOffice;

    public UpdateRegionalOfficeEndpoint setRegionalOffice(RegionalOffice regionalOffice) {
        this.regionalOffice = regionalOffice;
        return this;
    }

    @Override
    protected Type getModelType() {
        return RegionalOffice.class;
    }

    @Override
    public UpdateRegionalOfficeEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .body(regionalOffice)
                    .when()
                        .put("RegionalOffice/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
