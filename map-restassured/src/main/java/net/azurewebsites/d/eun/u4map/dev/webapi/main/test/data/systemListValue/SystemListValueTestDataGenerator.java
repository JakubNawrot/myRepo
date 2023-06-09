package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemListValue;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemListValue.SystemListValue;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

public class SystemListValueTestDataGenerator extends TestDataGenerator {

    public SystemListValue generateSystemListValue(){
        SystemListValue systemListValue = new SystemListValue();

        systemListValue.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        systemListValue.setCode(faker().regexify("\\w{29,30}"));
        systemListValue.setText(faker().regexify("\\w{199,200}"));
        return systemListValue;
    }

    public SystemListValue generateInvalidSystemListValue(){
        SystemListValue systemListValue = new SystemListValue();

        systemListValue.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        systemListValue.setCode(faker().regexify("\\w{31,32}"));
        systemListValue.setText(faker().regexify("\\w{201,202}"));
        return systemListValue;
    }
}
