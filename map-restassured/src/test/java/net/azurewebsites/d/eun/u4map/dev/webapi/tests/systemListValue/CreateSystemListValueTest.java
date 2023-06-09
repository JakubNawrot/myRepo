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
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemListValue.SystemListValueTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CreateSystemListValueTest extends SuiteTestBase {
    private SystemList systemList;
    private SystemListValue systemListValue;

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
    @Description("The goal of this test is to verify if it is possible to post System List Value successfully to system list")
    @Test
    public void givenSystemListIdWhenPostSystemListValueThenSystemListValueIsCreatedTest() {
        for (int amountOfValuesInSystemList = 10; amountOfValuesInSystemList > 0; amountOfValuesInSystemList--) {
            systemListValue = new SystemListValueTestDataGenerator().generateSystemListValue();
            systemListValue.setSystemListId(systemList.getId());
            SystemListValue postedSystemListValue = new CreateSystemListValueEndpoint().setSystemListValue(systemListValue)
                    .sendRequest()
                    .assertRequestSuccess()
                    .getResponseModel();

            Assertions.assertThat(postedSystemListValue).describedAs("Sent System List Value was different than received by API")
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(systemListValue);
        }
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("To goal of this test is to verify 400 BAD REQUEST is returned during posting System List Value with missing systemListId")
    @Test
    public void givenSystemListValueWithEmptySystemListIdWhenPostSystemListValueThenBadRequestTest() {
        systemListValue = new SystemListValueTestDataGenerator().generateSystemListValue();
        JsonPath receivedResponseBody = new CreateSystemListValueEndpoint().setSystemListValue(systemListValue)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        String errorMsg = receivedResponseBody.getString("error");
        assertEquals(errorMsg, "The specified system list does not exist in the database.", "Error - system list not found");
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
