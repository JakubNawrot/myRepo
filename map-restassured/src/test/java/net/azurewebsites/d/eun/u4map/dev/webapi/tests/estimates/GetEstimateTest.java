package net.azurewebsites.d.eun.u4map.dev.webapi.tests.estimates;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.estimate.*;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.CreateProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.DeleteProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate.EstimateTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project.ProjectTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;

public class GetEstimateTest extends SuiteTestBase {
    private Project project;
    private Estimate estimate;
    private String estimateId;

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

        Estimate postedEstimate = new CreateEstimateEndpoint().setEstimate(estimate)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        estimate.setCreatedAt(postedEstimate.getCreatedAt());
        estimate.setUpdatedAt(postedEstimate.getUpdatedAt());
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify that an Estimate can be read successfully")
    @Test
    public void givenExistingEstimateIdWhenGetEstimateThenEstimateIsReturnedTest() {
        Estimate receivedEstimate = new GetEstimateEndpoint().setEstimateId(estimateId)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(receivedEstimate).describedAs("Sent Estimate was different from what was received by the API")
                .usingRecursiveComparison().isEqualTo(estimate);
    }
    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to get all estimates from the tenant")
    @Test
    public void givenTenantIdWhenGetAllEstimatesThenAllEstimatesAreReturnedTest() {
        Estimate[] estimates = new GetAllEstimatesEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(estimates).describedAs("Array of received estimates contains null value").doesNotContainNull();
    }

    @TmsLink("718117")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to get all estimates assigned to the project")
    @Test
    public void givenProjectIdWhenGetAllProjectEstimatesThenAllProjectEstimatesAreReturnedTest() {
        Estimate[] projectsEstimates = new GetProjectsEstimatesEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
        Assertions.assertThat(projectsEstimates).describedAs("Array of received estimates contains null value").doesNotContainNull();

    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteEstimateEndpoint().setEstimateId(estimate.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}
