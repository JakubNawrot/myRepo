package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetSystemListEndpoint extends BaseEndPoint <GetSystemListEndpoint, SystemList> {
    private String systemListId;

    public GetSystemListEndpoint setSystemListId(String systemListId) {
        this.systemListId = systemListId;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemList.class;
    }

    @Override
    public GetSystemListEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .pathParam("systemListId", systemListId)
                .when()
                    .get("SystemList/{tenantId}/{systemListId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
