package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemListValue;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.CreateSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.GetSystemListValuesEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.GetTenantSystemListValuesEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.GetOneSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemListValue.SystemListValueTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetSystemListValueTest extends SuiteTestBase {
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
    @Description("The goal of this test is to get all Values from the tenant")
    @Test
    public void givenTenantIdWhenGetAllSystemListValuesThenAllSystemListValuesAreReturnedTest() {
        SystemListValue[] systemListValues = new GetTenantSystemListValuesEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(systemListValues).describedAs("Array of received System Lists contains null value").doesNotContainNull();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 404 NOT FOUND is returned when GET the unexisting System List Value")
    @Test
    public void givenUnExistingSystemListValueIdWhenGetSystemListValueThenNotFoundTest() {
        //at first step SystemList and SystemListValue must be created for further deletion
        SystemList systemListToBeDeleted = new SystemListTestDataGenerator().generateSystemList();
        systemListToBeDeleted = new CreateSystemListEndpoint().setSystemList(systemListToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        SystemListValue systemListValueToBeDeleted = new SystemListValueTestDataGenerator().generateSystemListValue();
        systemListValueToBeDeleted.setSystemListId(systemListToBeDeleted.getId());

        systemListValueToBeDeleted = new CreateSystemListValueEndpoint().setSystemListValue(systemListValueToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting the SystemList and SystemListValue nested into it
        new DeleteSystemListEndpoint().setId(systemListToBeDeleted.getId())
                .sendRequest()
                .assertRequestSuccess();

        new GetOneSystemListValueEndpoint().setId(systemListValueToBeDeleted.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to get all Values from the System List")
    @Test
    public void givenSystemListIdWhenGetSystemListValuesThenSystemListValuesAssignedToSystemListIdAreReturnedTest() {
        SystemListValue[] systemListValues = new GetSystemListValuesEndpoint().setSystemListId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(systemListValues).describedAs("Array of received System Lists contains null value").doesNotContainNull();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if System List Value is returned successfully for given id")
    @Test
    public void givenExistingSystemListValueIdWhenGetSystemListValueThenSystemListValueIsReturnedTest() {
        SystemListValue receivedSystemListValue = new GetOneSystemListValueEndpoint().setId(systemListValue.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedSystemListValue).describedAs("Sent System List was different than received by API")
                .usingRecursiveComparison().isEqualTo(systemListValue);
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
