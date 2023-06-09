package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.regionalOffice;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.regionalOffice.RegionalOffice;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

public class RegionalOfficeTestDataGenerator extends TestDataGenerator {

    public RegionalOffice generateRegionalOffice() {
        RegionalOffice regionalOffice = new RegionalOffice();

        regionalOffice.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        regionalOffice.setVisitingAddress(faker().address().fullAddress());
        regionalOffice.setPostalAddress(faker().address().streetAddress());
        regionalOffice.setPostalCode(faker().address().zipCode());
        regionalOffice.setCity(faker().address().city());
        regionalOffice.setState(faker().address().state());
        regionalOffice.setCountry(faker().address().country());
        regionalOffice.setPhoneNumber(faker().phoneNumber().cellPhone());
        regionalOffice.setFaxNumber(faker().phoneNumber().phoneNumber());
        regionalOffice.setCompanyURL(faker().internet().url());
        regionalOffice.setEmail(faker().internet().emailAddress());
        regionalOffice.setOfficeName(faker().company().name());

        return regionalOffice;
    }
}
