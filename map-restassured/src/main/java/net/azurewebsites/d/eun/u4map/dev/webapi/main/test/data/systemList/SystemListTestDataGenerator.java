package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList.SystemList;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

import java.util.Random;

public class SystemListTestDataGenerator extends TestDataGenerator {

    public SystemList generateSystemList(){
        SystemList systemList = new SystemList();

        systemList.setName(faker().regexify("\\w{199,200}"));
        systemList.setDescription(faker().regexify("\\w{999,1000}"));
        systemList.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        systemList.setDisplayCode(faker().random().nextBoolean());
        systemList.setAllowDuplicates(faker().random().nextBoolean());
        systemList.setIsActive(faker().random().nextBoolean());
        systemList.setIsRequired(faker().random().nextBoolean());
        systemList.setOrderBy(randomOrderByValue());
        systemList.setValueType(randomValueTypes());

        return systemList;
    }

    private OrderByValues randomOrderByValue() {
        int pick = new Random().nextInt(OrderByValues.values().length);
        return OrderByValues.values()[pick];
    }

    private ValueTypes randomValueTypes() {
        int pick = new Random().nextInt(ValueTypes.values().length);
        return ValueTypes.values()[pick];
    }
}
