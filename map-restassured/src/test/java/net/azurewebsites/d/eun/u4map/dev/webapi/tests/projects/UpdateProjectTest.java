package net.azurewebsites.d.eun.u4map.dev.webapi.tests.projects;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import io.restassured.path.json.JsonPath;
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

import java.util.*;

import static org.testng.Assert.assertEquals;

public class UpdateProjectTest extends SuiteTestBase {
    private RegionalOffice regionalOffice;
    private Project project;
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        //generate regional office to connect it to created project
        regionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();

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
    @Description("The goal of the test is to verify if it is possible to update whole project data")
    @Test
    public void givenProjectWhenUpdateProjectThenProjectIsUpdatedTest() {
        String projectID = project.getId();

        RegionalOffice updatedRegionalOffice = new RegionalOfficeTestDataGenerator().generateRegionalOffice();
        updatedRegionalOffice = new CreateRegionalOfficeEndpoint().setRegionalOffice(updatedRegionalOffice)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();
        project.setOfficeId(updatedRegionalOffice.getId());

        //generate system list to created project
        SystemList updatedSystemList = new SystemListTestDataGenerator().generateSystemList();
        updatedSystemList = new CreateSystemListEndpoint().setSystemList(updatedSystemList)
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
        dynamicProperty.setSystemListId(updatedSystemList.getId());
        dynamicProperty.setInputType(updatedSystemList.getValueType());
        dynamicProperty.setKey(systemListValue.getCode());
        dynamicProperty.setValue(systemListValue.getText());

        List<DynamicProperty> dynamicProperties = new ArrayList<>();
        dynamicProperties.add(dynamicProperty);

        project = new ProjectTestDataGenerator().generateProject();
        project.setOfficeId(regionalOffice.getId());
        project.setDynamicProperties(dynamicProperties);
        project.setId(projectID);

        Project updatedProject = new UpdateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(updatedProject)
                .describedAs("Updated project was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(project);

        //cleaning up
        new DeleteRegionalOfficeEndpoint().setId(updatedRegionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteSystemListEndpoint().setId(updatedSystemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during updating unexisting project")
    @Test
    public void givenUnexistingProjectWhenUpdateProjectThenProjectIsNotFoundTest() {
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

        new UpdateProjectEndpoint().setProject(postedProject)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 400 BAD REQUEST is returned during updating a project with empty mandatory data")
    @Test
    public void givenProjectWhenUpdateProjectDataWithEmptyMandatoryFieldsThenBadRequestTest() {
        //setting mandatory values to null
        project.setCode(null);
        project.setName(null);

        JsonPath bodyFromResponse = new UpdateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = bodyFromResponse.getMap("errors");

        assertEquals("The Code field is required.", validationErrors.get("Code").get(0), "Error code");
        assertEquals("The Name field is required.", validationErrors.get("Name").get(0), "Error name");
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 403 FORBIDDEN is returned during updating a project for unexisting tenant")
    @Test
    public void givenProjectWhenUpdateProjectDataForInvalidTenantThenForbiddenTest() {
        new UpdateProjectEndpoint().setProject(project)
                .sendInvalidRequest()
                .assertStatusCode(HttpStatus.SC_FORBIDDEN)
                .assertBody("403 - Not authorized, provided tenant could not be verified in the claims.");
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