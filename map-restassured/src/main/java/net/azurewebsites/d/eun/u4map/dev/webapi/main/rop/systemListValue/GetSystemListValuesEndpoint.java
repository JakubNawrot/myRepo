package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetSystemListValuesEndpoint extends BaseEndPoint<GetSystemListValuesEndpoint, SystemListValue[]> {
    private String systemListId;

    public GetSystemListValuesEndpoint setSystemListId(String systemListId) {
        this.systemListId = systemListId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemListValue[].class;
    }

    @Override
    public GetSystemListValuesEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .pathParam("systemListId", tenantId)
                .when()
                    .get("SystemListValue/{tenantId}/systemList/{systemListId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
