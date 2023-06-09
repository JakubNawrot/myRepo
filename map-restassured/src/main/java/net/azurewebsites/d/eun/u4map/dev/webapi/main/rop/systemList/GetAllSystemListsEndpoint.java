package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class GetAllSystemListsEndpoint extends BaseEndPoint <GetAllSystemListsEndpoint, SystemList[]> {

    @Override
    protected Type getModelType() {
        return SystemList[].class;
    }

    @Override
    public GetAllSystemListsEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                .when()
                    .get("SystemList/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
