package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class DeleteSystemListValueEndpoint extends BaseEndPoint<DeleteSystemListValueEndpoint, SystemListValue> {
    private String id;

    public DeleteSystemListValueEndpoint setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemListValue.class;
    }

    @Override
    public DeleteSystemListValueEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                        .pathParam("tenantId", tenantId)
                        .pathParam("id", id)
                    .when()
                        .delete("SystemListValue/{tenantId}/{id}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_NO_CONTENT;
    }
}
