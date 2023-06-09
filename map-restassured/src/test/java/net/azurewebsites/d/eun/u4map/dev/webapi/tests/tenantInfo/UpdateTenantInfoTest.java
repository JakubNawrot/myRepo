package net.azurewebsites.d.eun.u4map.dev.webapi.tests.tenantInfo;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.tenantInfo.TenantInfo;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.tenantInfo.GetTenantInfoEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.tenantInfo.UpdateTenantInfoEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.tenantInfo.TenantInfoTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UpdateTenantInfoTest extends SuiteTestBase {

    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of the test is to verify if it is possible to update tenant information for given TenantId")
    @Test
    public void givenTenantIdWhenTenantInformationGetsUpdatedThenTenantInformationIsUpdatedTest() {
        TenantInfo tenantInfo = new TenantInfoTestDataGenerator().generateTenantInfo();

        TenantInfo updatedTenantInfo = new UpdateTenantInfoEndpoint().setTenantInfo(tenantInfo)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        tenantInfo.setId(updatedTenantInfo.getId());

        TenantInfo receivedTenantInfo = new GetTenantInfoEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedTenantInfo).describedAs("Sent Tenant information was different than received by API")
                .usingRecursiveComparison().isEqualTo(updatedTenantInfo);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a Tenant Information with empty mandatory data")
    @Test
    public void givenTenantIdWhenUpdateTenantInformationWithEmptyMandatoryFieldThenBadRequestTest() {
        TenantInfo tenantInfo = new TenantInfoTestDataGenerator().generateTenantInfo();
        //setting mandatory value to null
        tenantInfo.setCompanyName(null);

        JsonPath bodyFromResponse = new UpdateTenantInfoEndpoint().setTenantInfo(tenantInfo)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();
        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");
        assertEquals("The CompanyName field is required.", validationErrors.get("CompanyName").get(0), "Error CompanyName");
    }
}
