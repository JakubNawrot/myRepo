package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemList;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.*;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetSystemListTest extends SuiteTestBase {
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
    @Description("The goal of this test is to get all System Lists from the tenant")
    @Test
    public void givenTenantIdWhenGetAllSystemListsThenAllSystemListsAreReturnedTest() {
        SystemList[] systemLists = new GetAllSystemListsEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(systemLists).describedAs("Array of received System Lists contains null value").doesNotContainNull();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if System List is returned successfully for given id")
    @Test
    public void givenExistingSystemListIdWhenGetSystemListThenSystemListIsReturnedTest() {
        SystemList receivedSystemList = new GetSystemListEndpoint().setSystemListId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedSystemList).describedAs("Sent System List was different than received by API")
                .usingRecursiveComparison().isEqualTo(systemList);
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 404 NOT FOUND is returned when GET the unexisting System List")
    @Test
    public void givenUnExistingSystemListIdWhenGetSystemListThenNotFoundTest() {
        SystemList systemListToBeDeleted = new SystemListTestDataGenerator().generateSystemList();
        SystemList postedSystemList = new CreateSystemListEndpoint().setSystemList(systemListToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created System List to be sure it does not exist
        new DeleteSystemListEndpoint().setId(postedSystemList.getId())
                .sendRequest()
                .assertRequestSuccess();

        new GetSystemListEndpoint().setSystemListId(postedSystemList.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if values defined for System List are returned successfully")
    @Test
    public void givenTenantIdWhenGetSystemListValuesThenValuesAreReturnedTest() {
        new GetValuesOfSystemListEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
