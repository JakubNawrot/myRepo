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
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.CreateProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.DeleteProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate.EstimateTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project.ProjectTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class CreateEstimateTest extends SuiteTestBase {
    private Estimate estimate;
    private Project project;

    @BeforeMethod
    public void beforeTest() {
        project = new ProjectTestDataGenerator().generateProject();
        estimate = new EstimateTestDataGenerator().generateEstimate();
        estimate.setProjectId(project.getId());

        new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if it is possible to post estimate successfully to a project")
    @Test
    public void givenProjectIdWhenPostEstimateThenEstimateIsCreatedTest() {
        Estimate postedEstimate = new CreateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(postedEstimate).describedAs("Sent estimate was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(estimate);

        //cleaning up the estimate
        new DeleteEstimateEndpoint().setEstimateId(estimate.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during posting an estimate with empty mandatory data")
    @Test
    public void givenEstimateWhenPostEstimateWithEmptyMandatoryFieldsThenBadRequestTest() {
        //setting mandatory values to null
        estimate.setName(null);
        estimate.setStatus(null);
        estimate.setProjectId(null);

        JsonPath bodyFromResponse = new CreateEstimateEndpoint().setEstimate(estimate)
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
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}