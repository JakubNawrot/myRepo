package net.azurewebsites.d.eun.u4map.dev.webapi.tests.tenantInfo;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.tenantInfo.TenantInfo;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.tenantInfo.GetTenantInfoEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.tenantInfo.UpdateTenantInfoEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.tenantInfo.TenantInfoTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetTenantInfoTest extends SuiteTestBase {
    TenantInfo tenantInfo;

    @BeforeMethod
    public void beforeTest() {
        tenantInfo = new TenantInfoTestDataGenerator().generateTenantInfo();

        tenantInfo = new UpdateTenantInfoEndpoint().setTenantInfo(tenantInfo)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if Tenant Information is returned successfully for given tenant id")
    @Test
    public void givenExistingTenantIdWhenGetTenantInfoThenTenantInformationIsReturnedTest() {
        TenantInfo receivedTenantInfo = new GetTenantInfoEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedTenantInfo).describedAs("Sent Tenant information was different than received by API")
                .usingRecursiveComparison().isEqualTo(tenantInfo);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 403 FORBIDDEN is returned during getting unexisting tenant")
    @Test
    public void givenUnExistingTenantIdWhenGetTenantInfoThenForbiddenIsReturnedTest() {
        new GetTenantInfoEndpoint()
                .sendInvalidRequest()
                .assertStatusCode(HttpStatus.SC_FORBIDDEN);
    }

}
