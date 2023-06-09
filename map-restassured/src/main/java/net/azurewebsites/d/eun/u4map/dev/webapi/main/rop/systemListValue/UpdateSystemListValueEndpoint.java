package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class UpdateSystemListValueEndpoint extends BaseEndPoint <UpdateSystemListValueEndpoint, SystemListValue> {
    private SystemListValue systemListValue;

    public UpdateSystemListValueEndpoint setSystemListValue(SystemListValue systemListValue) {
        this.systemListValue = systemListValue;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemListValue.class;
    }

    @Override
    public UpdateSystemListValueEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .body(systemListValue)
                .when()
                    .put("SystemListValue/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
