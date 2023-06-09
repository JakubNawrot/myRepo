package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemList;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CreateSystemListTest extends SuiteTestBase {
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        systemList = new SystemListTestDataGenerator().generateSystemList();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if it is possible to post System List successfully to tenant")
    @Test
    public void givenTenantIdWhenPostSystemListThenSystemListIsCreatedTest() {
       SystemList postedSystemList = new CreateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

       Assertions.assertThat(postedSystemList).describedAs("Sent System List was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt", "id").isEqualTo(systemList);

        //cleaning up
       new DeleteSystemListEndpoint().setId(postedSystemList.getId())
               .sendRequest()
               .assertRequestSuccess();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("To goal of this test is to verify 400 BAD REQUEST is returned during posting System List with missing mandatory data")
    @Test
    public void givenSystemListWithEmptyMandatoryFieldsWhenPostSystemListThenBadRequestTest() {
        systemList.setName(null);
        systemList.setValueType(null);

        JsonPath receivedResponseBody = new CreateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = receivedResponseBody.getMap("errors");
        assertEquals(validationErrors.get("Name").get(0), "The Name field is required.", "Error system list name");
        assertEquals(validationErrors.get("ValueType").get(0), "The ValueType field is required.", "Error system list value type");
    }
}
