package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemListValue;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.CreateSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.DeleteSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.UpdateSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemListValue.SystemListValueTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UpdateSystemListValueTest extends SuiteTestBase {

    private SystemListValue systemListValue;
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        systemList = new SystemListTestDataGenerator().generateSystemList();
        systemList = new CreateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        systemListValue = new SystemListValueTestDataGenerator().generateSystemListValue();
        systemListValue.setSystemListId(systemList.getId());

        systemListValue = new CreateSystemListValueEndpoint().setSystemListValue(systemListValue)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of the test is to verify if it is possible to update whole System List Value data")
    @Test
    public void givenSystemListValueWhenUpdateSystemListValueThenSystemListValueIsUpdatedTest() {
        String systemListId = systemList.getId();
        String systemListValueID = systemListValue.getId();

        systemListValue = new SystemListValueTestDataGenerator().generateSystemListValue();
        systemListValue.setSystemListId(systemListId);
        systemListValue.setId(systemListValueID);

        SystemListValue updatedSystemListValue = new UpdateSystemListValueEndpoint().setSystemListValue(systemListValue)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedSystemListValue)
                .describedAs("Updated System List Value was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(systemListValue);
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a System List Value with too long data")
    @Test
    public void givenSystemListValueWhenUpdateSystemListValueDataWithTooLongDataThenBadRequestTest() {
        String systemListId = systemList.getId();
        String systemListValueID = systemListValue.getId();

        systemListValue = new SystemListValueTestDataGenerator().generateInvalidSystemListValue();
        systemListValue.setSystemListId(systemListId);
        systemListValue.setId(systemListValueID);

        JsonPath bodyFromResponse = new UpdateSystemListValueEndpoint().setSystemListValue(systemListValue)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");
        assertEquals("The field Text must be a string or array type with a maximum length of '200'.", validationErrors.get("Text").get(0), "Error text field");
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during updating System List Value that does not exist")
    @Test
    public void givenUnexistingSystemListValueWhenUpdateSystemListValueThenNotFoundTest() {
        SystemListValue systemListValueToBeDeleted = new SystemListValueTestDataGenerator().generateSystemListValue();
        systemListValueToBeDeleted.setSystemListId(systemList.getId());

        SystemListValue postedSystemListValue = new CreateSystemListValueEndpoint().setSystemListValue(systemListValueToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created System List Value to be sure it does not exist
        new DeleteSystemListValueEndpoint().setId(postedSystemListValue.getId())
                .sendRequest()
                .assertRequestSuccess();

        new UpdateSystemListValueEndpoint().setSystemListValue(postedSystemListValue)
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
