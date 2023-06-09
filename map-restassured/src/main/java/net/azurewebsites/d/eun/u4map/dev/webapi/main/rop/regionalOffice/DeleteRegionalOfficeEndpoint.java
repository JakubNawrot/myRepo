package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class DeleteRegionalOfficeEndpoint extends BaseEndPoint<DeleteRegionalOfficeEndpoint, RegionalOffice> {
    private String id;

    public DeleteRegionalOfficeEndpoint setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    protected Type getModelType() {
        return RegionalOffice.class;
    }

    @Override
    public DeleteRegionalOfficeEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .pathParam("id", id)
                .when()
                    .delete("RegionalOffice/{tenantId}/{id}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_NO_CONTENT;
    }
}
