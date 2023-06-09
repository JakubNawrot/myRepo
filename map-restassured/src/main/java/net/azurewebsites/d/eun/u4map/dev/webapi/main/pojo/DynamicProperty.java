package net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.ValueTypes;

public class DynamicProperty {

    private String systemListId;
    private ValueTypes inputType;
    private String key;
    private String value;

    public String getSystemListId() {
        return systemListId;
    }

    public void setSystemListId(String systemListId) {
        this.systemListId = systemListId;
    }

    public ValueTypes getInputType() {
        return inputType;
    }

    public void setInputType(ValueTypes inputType) {
        this.inputType = inputType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
