package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;

import java.util.List;

public class PayloadConfig {

    private ValidateParticipantDTO input;
    private List<PayloadProperties> properties;
    private String quotationId;
    public ValidateParticipantDTO getInput() {
        return input;
    }

    public void setInput(ValidateParticipantDTO input) {
        this.input = input;
    }

    public List<PayloadProperties> getProperties() {
        return properties;
    }

    public void setProperties(List<PayloadProperties> properties) {
        this.properties = properties;
    }

    public String getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(String quotationId) {
        this.quotationId = quotationId;
    }

    @Override
    public String toString() {
        return "PayloadConfig{" +
                "input=" + input +
                ", properties=" + properties +
                ", quotationId='" + quotationId + '\'' +
                '}';
    }
}
