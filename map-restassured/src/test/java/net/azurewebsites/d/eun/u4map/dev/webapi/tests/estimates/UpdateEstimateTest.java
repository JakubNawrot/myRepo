package net.azurewebsites.d.eun.u4map.dev.webapi.tests.estimates;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.CreateEstimateEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.DeleteEstimateEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.UpdateEstimateEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.CreateProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.DeleteProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate.EstimateStatus;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate.EstimateTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project.ProjectTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class UpdateEstimateTest extends SuiteTestBase {
    private Project project;
    private Estimate estimate;
    private String estimateId;

    @DataProvider
    public Object[][] estimateStatuses() {
        return new Object[][]
                {
                        {EstimateStatus.CANCELLED},
                        {EstimateStatus.DRAFT},
                        {EstimateStatus.PRODUCTION},
                };
    }

    @BeforeMethod
    public void beforeTest() {
        project = new ProjectTestDataGenerator().generateProject();

        Project postedProject = new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        estimate = new EstimateTestDataGenerator().generateEstimate();
        estimate.setProjectId(postedProject.getId());
        estimateId = estimate.getId();

        new CreateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if it is possible to update all possible statuses of the estimate")
    @Test(dataProvider = "estimateStatuses")
    public void givenEstimateWhenEstimateGetsUpdatedStatusThenEstimateIsUpdatedTest(EstimateStatus estimateStatus) {
        estimate.setStatus(estimateStatus.getStatus());
        Estimate updatedEstimate = new UpdateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedEstimate.getStatus())
                .describedAs("Estimate Status")
                .isEqualTo(estimateStatus.getStatus());
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify that an Estimate can be updated successfully")
    @Test
    public void givenEstimateWhenUpdateEstimateThenEstimateIsUpdatedTest() {
        //creating an estimate with updated data
        estimate = new EstimateTestDataGenerator().generateEstimate();
        estimate.setProjectId(project.getId());
        estimate.setId(estimateId);

        Estimate updatedEstimate = new UpdateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedEstimate).describedAs("Updated estimate was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(estimate);
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during updating unexisting estimate")
    @Test
    public void givenUnexistingEstimateWhenUpdateEstimateThenEstimateIsNotFoundTest() {
        //creating an estimate with updated data but with unexisting estimateId in portal
        estimate = new EstimateTestDataGenerator().generateEstimate();
        estimate.setProjectId(project.getId());

        new UpdateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a estimate with empty mandatory data")
    @Test
    public void givenEstimateWhenUpdateEstimateDataWithEmptyMandatoryFieldsThenBadRequestTest() {
        //setting mandatory values to null
        estimate.setName(null);
        estimate.setStatus(null);
        estimate.setProjectId(null);

        JsonPath bodyFromResponse = new UpdateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");
        assertEquals("The Name field is required.", validationErrors.get("Name").get(0), "Error name");
        assertEquals("The Status field is required.", validationErrors.get("Status").get(0), "Error status");
        assertEquals("The ProjectId field is required.", validationErrors.get("ProjectId").get(0), "Error projectId");
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteEstimateEndpoint().setEstimateId(estimateId)
                .sendRequest()
                .assertRequestSuccess();

        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}