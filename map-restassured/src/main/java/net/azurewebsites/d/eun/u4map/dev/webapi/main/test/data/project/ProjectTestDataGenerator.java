package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.project;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project.Project;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

public class ProjectTestDataGenerator extends TestDataGenerator {

    public Project generateProject() {
        Project project = new Project();

        project.setId(faker().internet().uuid());
        project.setName(faker().regexify("\\w{99,100}"));
        project.setDescription(faker().regexify("\\w{999,1000}"));
        project.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        project.setCode(faker().code().ean13());
        project.setOfficeId("");

        return project;
    }
}
