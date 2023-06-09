package net.azurewebsites.d.eun.u4map.dev.webapi.tests.systemList;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DeleteSystemListTest extends SuiteTestBase {
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
    @Description("The goal of the test is to verify if it is possible to delete existing System List")
    @Test
    public void givenExistingSystemListIdWhenDeleteSystemListThenSystemListIsDeletedTest() {
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("733859")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during deleting unexisting System List")
    @Test
    public void givenUnExistingSystemListIdWhenDeleteSystemListThenSystemListIsNotFoundTest() {
        //deleting created System List to be sure it does not exist for the further deletion
        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

}
