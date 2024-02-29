package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;

import java.util.List;
import java.util.Map;

public class PayloadConfig {

    private ValidateParticipantDTO input;
    private List<PayloadProperties> properties;
    private String quotationId;
    private Map<String,Object> dataInsuredBD;
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

    public Map<String,Object> getDataInsuredBD() {
        return dataInsuredBD;
    }

    public void setDataInsuredBD(Map<String,Object> dataInsuredBD) {
        this.dataInsuredBD = dataInsuredBD;
    }

    @Override
    public String toString() {
        return "PayloadConfig{" +
                "input=" + input +
                ", properties=" + properties +
                ", quotationId='" + quotationId + '\'' +
                ", dataInsuredBD=" + dataInsuredBD +
                '}';
    }
}
