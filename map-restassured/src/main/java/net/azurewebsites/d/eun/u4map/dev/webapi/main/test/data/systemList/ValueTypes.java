package net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList;

public enum ValueTypes {
    TEXT("text"),
    NUMBER("number");

    private final String type;

    ValueTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

