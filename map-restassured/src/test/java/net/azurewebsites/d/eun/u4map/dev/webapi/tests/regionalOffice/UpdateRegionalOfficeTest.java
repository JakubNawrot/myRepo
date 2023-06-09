package net.azurewebsites.d.eun.u4map.dev.webapi.tests.regionalOffice;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.CreateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.DeleteRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.UpdateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice.RegionalOfficeTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UpdateRegionalOfficeTest extends SuiteTestBase {
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
    @Description("The goal of the test is to verify if it is possible to update whole Regional Office data")
    @Test
    public void givenRegionalOfficeWhenUpdateRegionalOfficeThenRegionalOfficeIsUpdatedTest() {
        String regionalOfficeId = regionalOffice.getId();
        regionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();
        regionalOffice.setId(regionalOfficeId);

        RegionalOffice updatedRegionalOffice = new UpdateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedRegionalOffice)
                .describedAs("Updated Regional Office was different than received by API")
                .usingRecursiveComparison().isEqualTo(regionalOffice);
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a Regional Office with empty mandatory data")
    @Test
    public void givenRegionalOfficeWhenUpdateRegionalOfficeDataWithEmptyMandatoryFieldsThenBadRequestTest() {
        regionalOffice.setOfficeName(null);

        JsonPath bodyFromResponse = new UpdateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");

        assertEquals("The OfficeName field is required.", validationErrors.get("OfficeName").get(0), "Error Office Name");
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during updating unexisting Regional Office")
    @Test
    public void givenUnexistingRegionalOfficeWhenUpdateRegionalOfficeThenRegionalOfficeIsNotFoundTest() {
        RegionalOffice regionalOfficeToBeDeleted = new RegionalOfficeTestDataGenerator().generateRegionalOffice();

        RegionalOffice postedRegionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOfficeToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created Regional Office to be sure it does not exist
        new DeleteRegionalOfficeEndpoint().setId(postedRegionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new UpdateRegionalOfficeEndpoint().setRegionalOffice(postedRegionalOffice)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
