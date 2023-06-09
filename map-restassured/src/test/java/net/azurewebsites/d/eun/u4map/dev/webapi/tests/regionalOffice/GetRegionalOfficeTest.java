package net.azurewebsites.d.eun.u4map.dev.webapi.tests.regionalOffice;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.CreateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.DeleteRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.GetAllRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.GetRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice.RegionalOfficeTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GetRegionalOfficeTest extends SuiteTestBase {
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
    @Description("The goal of this test is to verify if Regional Office is returned successfully for given id")
    @Test
    public void givenExistingRegionalOfficeIdWhenGetRegionalOfficeThenRegionalOfficeIsReturnedTest() {
        RegionalOffice receivedRegionalOffice = new GetRegionalOfficeEndpoint().setRegionalOfficeId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedRegionalOffice).describedAs("Sent Regional Office was different than received by API")
                .usingRecursiveComparison().isEqualTo(regionalOffice);
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 404 NOT FOUND is returned when GET the unexisting Regional Office")
    @Test
    public void givenUnExistingRegionalOfficeIdWhenGetRegionalOfficeThenNotFoundTest() {
        RegionalOffice regionalOfficeToBeDeleted = new RegionalOfficeTestDataGenerator().generateRegionalOffice();

        RegionalOffice postedRegionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOfficeToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created Regional Office to be sure it does not exist
        new DeleteRegionalOfficeEndpoint().setId(postedRegionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new GetRegionalOfficeEndpoint().setRegionalOfficeId(postedRegionalOffice.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to get all Regional Offices from the tenant")
    @Test
    public void givenTenantIdWhenGetAllRegionalOfficesThenAllRegionalOfficesAreReturnedTest() {
        RegionalOffice[] regionalOffices = new GetAllRegionalOfficeEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
        Assertions.assertThat(regionalOffices).describedAs("Array of received Regional Offices contains null value").doesNotContainNull();

    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}