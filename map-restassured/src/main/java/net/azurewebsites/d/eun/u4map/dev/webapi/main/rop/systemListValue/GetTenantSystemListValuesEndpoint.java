package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetTenantSystemListValuesEndpoint extends BaseEndPoint<GetTenantSystemListValuesEndpoint, SystemListValue[]> {

    @Override
    protected Type getModelType() {
        return SystemListValue[].class;
    }

    @Override
    public GetTenantSystemListValuesEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                .when()
                    .get("SystemListValue/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
