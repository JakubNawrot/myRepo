package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class CreateSystemListValueEndpoint extends BaseEndPoint<CreateSystemListValueEndpoint, SystemListValue> {
    private SystemListValue systemListValue;

    public CreateSystemListValueEndpoint setSystemListValue(SystemListValue systemListValue) {
        this.systemListValue = systemListValue;
        return this;
    }

    @Override
    protected Type getModelType() {
        return SystemListValue.class;
    }

    @Override
    public CreateSystemListValueEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                    .pathParam("tenantId", tenantId)
                    .body(systemListValue)
                .when()
                    .post("SystemListValue/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_CREATED;
    }
}
