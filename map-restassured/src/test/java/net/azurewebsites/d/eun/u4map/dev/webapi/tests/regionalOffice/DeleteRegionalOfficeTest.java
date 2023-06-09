package net.azurewebsites.d.eun.u4map.dev.webapi.tests.regionalOffice;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.CreateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.DeleteRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice.RegionalOfficeTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DeleteRegionalOfficeTest extends SuiteTestBase {
    private RegionalOffice regionalOffice;

    @BeforeMethod
    public void beforeTest() {
        regionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();

        regionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of the test is to verify if it is possible to delete existing Regional Office")
    @Test
    public void givenExistingRegionalOfficeIdWhenDeleteRegionalOfficeThenRegionalOfficeIsDeletedTest() {
        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during deleting unexisting Regional Office")
    @Test
    public void givenUnExistingRegionalOfficeIdWhenDeleteRegionalOfficeThenRegionalOfficeIsNotFoundTest() {
        //deleting created Regional Office to be sure it does not exist for the further deletion
        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }
}
