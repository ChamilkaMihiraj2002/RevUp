package com.revup.chatboat_service.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * DTO representing a service type returned by the external microservice.
 */
public class ServiceType {

    @JsonProperty("service_type_id")
    private Long serviceTypeId;

    @JsonProperty("base_duration_minutes")
    private Integer baseDurationMinutes;

    @JsonProperty("base_price")
    private BigDecimal basePrice;

    private String code;
    private String description;
    private String name;

    public Long getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Long serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getBaseDurationMinutes() {
        return baseDurationMinutes;
    }

    public void setBaseDurationMinutes(Integer baseDurationMinutes) {
        this.baseDurationMinutes = baseDurationMinutes;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceType{" +
                "serviceTypeId=" + serviceTypeId +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", baseDurationMinutes=" + baseDurationMinutes +
                ", basePrice=" + basePrice +
                '}';
    }
}
