package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList;

public enum OrderByValues {
    TEXT("text"),
    CODE("code");

    private final String value;

    OrderByValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
