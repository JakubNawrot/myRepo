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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class DeleteProjectTest extends SuiteTestBase {
    private RegionalOffice regionalOffice;
    private Project project;
    private SystemList systemList;

    @BeforeMethod
    public void beforeTest() {
        //generate regional office to connect it to created project
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
    @Description("The goal of the test is to verify if it is possible to delete existing project")
    @Test
    public void givenExistingProjectIdWhenDeleteProjectThenProjectIsDeletedTest() {
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();
    }

    @TmsLink("718116")
    @Severity(SeverityLevel.NORMAL)
    @Description("The goal of the test is to verify if 404 NOT FOUND is returned during deleting unexisting project")
    @Test
    public void givenUnExistingProjectIdWhenDeleteProjectThenProjectIsNotFoundTest() {
        //deleting created project to be sure it does not exist for the further deletinon
        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertRequestSuccess();

        new DeleteProjectEndpoint().setProjectId(project.getId())
                .sendRequest()
                .assertStatusCode(HttpStatus.SC_NOT_FOUND);
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
