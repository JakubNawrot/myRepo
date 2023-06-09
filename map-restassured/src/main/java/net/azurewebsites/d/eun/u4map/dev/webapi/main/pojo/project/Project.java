package net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.project;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.DynamicProperty;

import java.util.List;

public class Project {

    private String id;
    private String name;
    private String description;
    private String tenantId;
    private String code;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private List<DynamicProperty> dynamicProperties;
    private String officeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<DynamicProperty> getDynamicProperties() {
        return dynamicProperties;
    }

    public void setDynamicProperties(List<DynamicProperty> dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

}
