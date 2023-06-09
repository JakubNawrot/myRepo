package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemList;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.UpdateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UpdateSystemListTest extends SuiteTestBase {
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        systemList = new SystemListTestDataGenerator().generateSystemList();

        systemList = new CreateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of the test is to verify if it is possible to update whole System List data")
    @Test
    public void givenSystemListWhenUpdateSystemListThenSystemListIsUpdatedTest() {
        String systemListId = systemList.getId();
        systemList = new SystemListTestDataGenerator().generateSystemList();
        systemList.setId(systemListId);

        SystemList updatedSystemList = new UpdateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedSystemList)
                .describedAs("Updated System List was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(systemList);
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a System List with empty mandatory data")
    @Test
    public void givenSystemListWhenUpdateSystemListDataWithEmptyMandatoryFieldsThenBadRequestTest() {
        systemList.setName(null);
        systemList.setValueType(null);

        JsonPath bodyFromResponse = new UpdateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");

        assertEquals("The Name field is required.", validationErrors.get("Name").get(0), "Error Name");
        assertEquals("The ValueType field is required.", validationErrors.get("ValueType").get(0), "Error ValueType");
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during updating unexisting System List")
    @Test
    public void givenUnexistingSystemListWhenUpdateSystemListThenSystemListIsNotFoundTest() {
        SystemList systemListToBeDeleted = new SystemListTestDataGenerator().generateSystemList();

        SystemList postedSystemList = new CreateSystemListEndpoint().setSystemList(systemListToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created System List to be sure it does not exist
        new DeleteSystemListEndpoint().setId(postedSystemList.getId())
                .sendRequest()
                .assertRequestSuccess();

        new UpdateSystemListEndpoint().setSystemList(postedSystemList)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
