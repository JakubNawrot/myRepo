package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data;

public class FakePropertiesGenerator extends TestDataGenerator {

    public String generateFakeUuid() {
        return faker().internet().uuid();
    }

    public String generateFakeToken() {
        return faker().regexify("[[:alnum:]]{150}");
    }
}
