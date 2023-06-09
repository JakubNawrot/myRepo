package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.estimate;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.estimate.Estimate;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

import java.util.Random;

public class EstimateTestDataGenerator extends TestDataGenerator {

    public Estimate generateEstimate() {
        Estimate estimate = new Estimate();

        estimate.setId(faker().internet().uuid());
        estimate.setName(faker().regexify("\\w{99,100}"));
        estimate.setDescription(faker().lordOfTheRings().location());
        estimate.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        //estimate.setDesktopEstimateReference(faker().rickAndMorty().character());

        EstimateStatus estimateStatus = randomEstimateStatus();
        estimate.setStatus(estimateStatus.getStatus());

        return estimate;
    }

    private EstimateStatus randomEstimateStatus() {
        int pick = new Random().nextInt(EstimateStatus.values().length);
        return EstimateStatus.values()[pick];
    }


}
