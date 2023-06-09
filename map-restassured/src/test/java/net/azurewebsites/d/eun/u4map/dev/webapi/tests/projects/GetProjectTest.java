package net.azurewebsites.d.eun.u4map.dev.webapi.tests.projects;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.DynamicProperty;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.*;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.CreateRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.regionalOffice.DeleteRegionalOfficeEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.CreateSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemList.DeleteSystemListEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.systemListValue.CreateSystemListValueEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project.ProjectTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice.RegionalOfficeTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.SystemListTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemListValue.SystemListValueTestDataGenerator;
import net.azurewebsites.d.eun.u4map.dev.webapi.tests.testbases.SuiteTestBase;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class GetProjectTest extends SuiteTestBase {
    private RegionalOffice regionalOffice;
    private Project project;
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        //generate and post new regional office to connect it to created project
        regionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();
        project = new ProjectTestDataGenerator().generateProject();

        regionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(regionalOffice)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //generate system list to created project
        systemList = new SystemListTestDataGenerator().generateSystemList();
        systemList = new CreateSystemListEndpoint().setSystemList(systemList)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //generate values for system list
        SystemListValue systemListValue = new SystemListValueTestDataGenerator().generateSystemListValue();
        systemListValue.setSystemListId(systemList.getId());
        systemListValue = new CreateSystemListValueEndpoint().setSystemListValue(systemListValue)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //prepare dynamicProperties for project and include system list and system list values inside
        DynamicProperty dynamicProperty = new DynamicProperty();
        dynamicProperty.setSystemListId(systemList.getId());
        dynamicProperty.setInputType(systemList.getValueType());
        dynamicProperty.setKey(systemListValue.getCode());
        dynamicProperty.setValue(systemListValue.getText());

        List<DynamicProperty> dynamicProperties = new ArrayList<>();
        dynamicProperties.add(dynamicProperty);

        project = new ProjectTestDataGenerator().generateProject();
        project.setOfficeId(regionalOffice.getId());
        project.setDynamicProperties(dynamicProperties);

        new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
    }

    @TmsLink("718116")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if project is returned successfully for given project id")
    @Test
    public void givenExistingProjectIdWhenGetProjectThenProjectIsReturnedTest() {
        Project postedProject = new GetProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        ///tbd as soon as dynamic properties will be implemented in tests
        Assertions.assertThat(postedProject).describedAs("Sent project was different than received by API")
                .usingRecursiveComparison().ignoringFields("dynamicProperties", "updatedAt", "createdAt").isEqualTo(project);

    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 404 NOT FOUND is returned when GET the unexisting project")
    @Test
    public void givenUnexistingProjectIdWhenGetProjectThenProjectNotFoundTest() {
        //creating a project
        Project projectToBeDeleted = new ProjectTestDataGenerator().generateProject();

        Project postedProject = new CreateProjectEndpoint().setProject(projectToBeDeleted)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        //deleting created project to be sure it does not exist
        new DeleteProjectEndpoint().setProjectId(postedProject.getId())
                .sendRequest()
                .assertRequestSuccess();

        new GetProjectEndpoint().setProjectId(postedProject.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }


    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to get all projects from the tenant and compare amount of the returned projects " +
            "with a value returned by endpoint that counts the projects: /api/Projects/{tenantId}/count")
    @Test
    public void givenTenantIdWhenGetAllProjectsThenAllProjectsAreReturnedTest() {
        Project[] projects = new GetAllProjectsEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Integer projectCounter = new GetProjectCountEndpoint()
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        assertEquals(projects.length, projectCounter, "Project count");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 403 - Forbidden is returned when invalid tenant is given")
    @Test
    public void givenInvalidTenantIdWhenGetAllProjectsThenForbiddenTest() {
        new GetAllProjectsEndpoint()
                .sendInvalidRequest()
                .assertStatusCode(HttpStatus.SC_FORBIDDEN)
                .assertBody("403 - Not authorized, provided tenant could not be verified in the claims.");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 403 - Forbidden is returned when invalid token is given")
    @Test
    public void givenInvalidTokenIdWhenGetAllProjectsThenForbiddenTest() {
        new GetAllProjectsEndpoint()
                .sendUnauthorizedRequest()
                .assertStatusCode(HttpStatus.SC_FORBIDDEN).assertBody("403 - Not authorized, provided tenant could not be verified in the claims.");
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}