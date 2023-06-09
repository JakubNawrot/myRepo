package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.tenantInfo;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.tenantInfo.TenantInfo;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.properties.EnvironmentConfig;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.TestDataGenerator;
import org.aeonbits.owner.ConfigFactory;

public class TenantInfoTestDataGenerator extends TestDataGenerator {

    public TenantInfo generateTenantInfo() {
        TenantInfo tenantInfo = new TenantInfo();
        tenantInfo.setTenantId(ConfigFactory.create(EnvironmentConfig.class).tenantId());
        tenantInfo.setVisitingAddress(faker().harryPotter().location());
        tenantInfo.setPostalAddress(faker().address().streetAddress());
        tenantInfo.setPostalCode(faker().address().zipCode());
        tenantInfo.setCity(faker().address().city());
        tenantInfo.setState(faker().address().state());
        tenantInfo.setCountry(faker().address().country());
        tenantInfo.setPhoneNumber(faker().phoneNumber().cellPhone());
        tenantInfo.setFaxNumber(faker().phoneNumber().phoneNumber());
        tenantInfo.setCompanyURL(faker().internet().url());
        tenantInfo.setEmail(faker().internet().emailAddress());
        tenantInfo.setCompanyName(faker().harryPotter().house());
        tenantInfo.setCompanyLogo("https://scontent-fra5-1.xx.fbcdn.net/v/t1.18169-9/318124_3876446969813_39480919_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=de6eea&_nc_ohc=XBmWnxKRTiUAX_v7YFF&_nc_ht=scontent-fra5-1.xx&oh=00_AfCsGEUIk41Ec2ZX-aegj6gh0WVd0PZyxtUvhcI4gm9c5w&oe=64928644");

        return tenantInfo;
    }
}
