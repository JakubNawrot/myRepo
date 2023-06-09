package net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.tenantInfo;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.tenantInfo.TenantInfo;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.request.configuration.RequestConfigurationBuilder;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.BaseEndPoint;
import org.apache.http.HttpStatus;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;

public class UpdateTenantInfoEndpoint extends BaseEndPoint<UpdateTenantInfoEndpoint, TenantInfo> {
    private TenantInfo tenantInfo;

    public UpdateTenantInfoEndpoint setTenantInfo(TenantInfo tenantInfo) {
        this.tenantInfo = tenantInfo;
        return this;
    }

    @Override
    protected Type getModelType() {
        return TenantInfo.class;
    }

    @Override
    public UpdateTenantInfoEndpoint sendRequest() {
        response = given().spec(RequestConfigurationBuilder.getDefaultRequestSpecification())
                .pathParam("tenantId", tenantId)
                .body(tenantInfo)
                .when().put("TenantInfo/{tenantId}");
        return this;
    }

    @Override
    protected int getSuccessStatusCode() {
        return HttpStatus.SC_OK;
    }
}
