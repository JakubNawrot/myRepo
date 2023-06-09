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
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.CreateProjectEndpoint;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.rop.project.DeleteProjectEndpoint;
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
import java.util.Map;

import static org.testng.Assert.assertEquals;


public class CreateProjectTest extends SuiteTestBase {
    private Project project;
    private RegionalOffice regionalOffice;
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
    }

    @TmsLink("718116")
    @Severity(SeverityLevel.CRITICAL)
    @Description("The goal of this test is to verify if it is possible to post project successfully to tenant")
    @Test
    public void givenProjectWhenPostProjectThenProjectIsCreatedTest() {
        Project postedProject = new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertRequestSuccess()
                .getResponseModel();

        Assertions.assertThat(postedProject).describedAs("Sent project was different than received by API")
                .usingRecursiveComparison().ignoringFields("createdAt", "updatedAt").isEqualTo(project);

        //cleaning up after test
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("718116")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of this test is to verify if 403 FORBIDDEN is returned during posting project to invalid tenant")
    @Test
    public void givenProjectWhenPostProjectToInvalidTenantThenForbiddenTest() {
        new CreateProjectEndpoint().setProject(project)
                .sendInvalidRequest()
                .assertStatusCode(HttpStatus.SC_FORBIDDEN)
                .assertBody("403 - Not authorized, provided tenant could not be verified in the claims.");
    }

    @TmsLink("718116")
    @Severity(SeverityLevel.NORMAL)
    @Description("To goal of this test is to verify 400 BAD REQUEST is returned during posting project with missing mandatory data")
    @Test
    public void givenProjectWithEmptyMandatoryFieldsWhenPostProjectThenBadRequestTest() {
        project.setName(null);
        project.setCode(null);

        JsonPath receivedResponseBody = new CreateProjectEndpoint().setProject(project)
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_BAD_REQUEST)
                .extractBodyFromResponse();

        Map<String, ArrayList<String>> validationErrors = receivedResponseBody.getMap("errors");

        assertEquals(validationErrors.get("Code").get(0), "The Code field is required.", "Error code");
        assertEquals(validationErrors.get("Name").get(0), "The Name field is required.", "Error name");
    }

    @AfterMethod
    public void cleanUpAfterTest() {
        new DeleteRegionalOfficeEndpoint().setId(regionalOffice.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteSystemListEndpoint().setId(systemList.getId())
                .sendRequest()
                .assertRequestSuccess();
    }
}