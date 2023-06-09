package net.azurewebsites.d.eun.u4map.dev.webapi.tests.regionalOffice;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.CreateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.DeleteRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice.RegionalOfficeTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CreateRegionalOfficeTest extends SuiteTestBase {
    private RegionalOffice regionalOffice;

    @BeforeMethod
    public void beforeTest() {
        regionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if it is possible to post Regional Office successfully to tenant")
    @Test
    public void givenTenantIdWhenPostRegionalOfficeThenRegionalOfficeIsCreatedTest() {
        RegionalOffice postedRegionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(postedRegionalOffice).describedAs("Sent Regional Office was different than received by API")
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(regionalOffice);

        //cleaning up
        new DeleteRegionalOfficeEndpoint().setId(postedRegionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("729157")
    @Severity(SeverityLevel.NORMAL)
    @Description("To goal of this test is to verify 400 BAD REQUEST is returned during posting Regional Office with missing mandatory data")
    @Test
    public void givenRegionalOfficeWithEmptyMandatoryFieldsWhenPostRegionalOfficeThenBadRequestTest() {
        regionalOffice.setOfficeName(null);

        JsonPath receivedResponseBody = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = receivedResponseBody.getMap("errors");
        assertEquals(validationErrors.get("OfficeName").get(0), "The OfficeName field is required.", "Error office name");
    }
}
