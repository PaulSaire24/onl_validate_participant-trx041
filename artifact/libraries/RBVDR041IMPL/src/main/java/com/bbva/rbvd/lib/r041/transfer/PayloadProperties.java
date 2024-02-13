package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;

public class PayloadProperties {

    private String customerId;
    private String documetType;
    private String documetNumber;
    private PEWUResponse customer;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDocumetType() {
        return documetType;
    }

    public void setDocumetType(String documetType) {
        this.documetType = documetType;
    }

    public String getDocumetNumber() {
        return documetNumber;
    }

    public void setDocumetNumber(String documetNumber) {
        this.documetNumber = documetNumber;
    }

    public PEWUResponse getCustomer() {
        return customer;
    }

    public void setCustomer(PEWUResponse customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "PayloadProperties{" +
                "customerId='" + customerId + '\'' +
                ", documetType='" + documetType + '\'' +
                ", documetNumber='" + documetNumber + '\'' +
                ", customer=" + customer +
                '}';
    }
}
