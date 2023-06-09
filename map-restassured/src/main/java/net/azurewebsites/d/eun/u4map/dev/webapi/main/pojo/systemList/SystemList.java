package net.azurewebsites.d.eun.u4map.dev.webapi.main.pojo.systemList;

import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.OrderByValues;
import net.azurewebsites.d.eun.u4map.dev.webapi.main.test.data.systemList.ValueTypes;

public class SystemList {
    private String id;
    private String name;
    private String description;
    private String tenantId;
    private Boolean displayCode;
    private Boolean allowDuplicates;
    private Boolean isActive;
    private Boolean isRequired;
    private OrderByValues orderBy;
    private ValueTypes valueType;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;

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

    public Boolean getDisplayCode() {
        return displayCode;
    }

    public void setDisplayCode(Boolean displayCode) {
        this.displayCode = displayCode;
    }

    public Boolean getAllowDuplicates() {
        return allowDuplicates;
    }

    public void setAllowDuplicates(Boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public OrderByValues getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderByValues orderBy) {
        this.orderBy = orderBy;
    }

    public ValueTypes getValueType() {
        return valueType;
    }

    public void setValueType(ValueTypes valueType) {
        this.valueType = valueType;
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

}