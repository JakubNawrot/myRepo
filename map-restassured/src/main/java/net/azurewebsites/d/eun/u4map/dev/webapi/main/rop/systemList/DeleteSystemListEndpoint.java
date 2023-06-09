package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class DeleteSystemListEndpoint extends BaseEndPoint <DeleteSystemListEndpoint, SystemList> {
    private String id;

    public DeleteSystemListEndpoint setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemList.class;
    }

    @Override
    public DeleteSystemListEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .pathParam("id", id)
                .when()
                    .delete("SystemList/{tenantId}/{id}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_NO_CONTENT;
    }
}
