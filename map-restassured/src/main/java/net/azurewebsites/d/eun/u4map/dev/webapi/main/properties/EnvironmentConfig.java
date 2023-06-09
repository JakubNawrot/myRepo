package net.azurewebsites.d.eun.u4map.dev.webapi.main.properties;

import org.aeonbits.owner.Config;
@Config.Sources("classpath:EnvironmentConfig.properties")
public interface EnvironmentConfig extends Config {

    @Key("BASE_URI")
    String baseUri();

    @Key("BASE_PATH")
    String basePath();

    @Key("TENANT_ID")
    String tenantId();

    @Key("ACCESS_TOKEN_URL")
    String getAccessTokenUrl();

    @Key("CLIENT_ID")
    String getClientId();

    @Key("CLIENT_SECRET")
    String getClientSecret();

}
