package net.azurewebsites.d.eun.u4map.dev.webapi.tests.estimates;

import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.CreateEstimateEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.DeleteEstimateEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.CreateProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.DeleteProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate.EstimateTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project.ProjectTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;

public class DeleteEstimateTest extends SuiteTestBase {
    private Project project;
    private Estimate estimate;

    @BeforeMethod
    public void beforeTest() {
        project = new ProjectTestDataGenerator().generateProject();

        Project postedProject = new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        estimate = new EstimateTestDataGenerator().generateEstimate();
        estimate.setProjectId(postedProject.getId());

        new CreateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify that an Estimate can be deleted successfully")
    @Test
    public void givenExistingEstimateIdWhenDeleteEstimateThenEstimateIsDeleted() {
        new DeleteEstimateEndpoint().setEstimateId(estimate.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during deleting unexisting estimate")
    @Test
    public void givenUnexistingEstimateIdWhenDeleteEstimateThenEstimateIsNotFound() {
        //deleting created estimate to be sure it does not exist for the further deletinon
        new DeleteEstimateEndpoint().setEstimateId(estimate.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteEstimateEndpoint().setEstimateId(estimate.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}