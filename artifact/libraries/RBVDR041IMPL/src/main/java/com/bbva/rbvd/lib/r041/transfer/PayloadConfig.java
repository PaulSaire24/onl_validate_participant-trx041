package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;

import java.util.List;
import java.util.Map;

public class PayloadConfig {

    private InputParticipantsDTO input;
    private List<PayloadCustomer> properties;
    private String quotationId;
    private Map<String,Object> dataInsuredBD;
    public InputParticipantsDTO getInput() {
        return input;
    }

    public void setInput(InputParticipantsDTO input) {
        this.input = input;
    }

    public List<PayloadCustomer> getProperties() {
        return properties;
    }

    public void setProperties(List<PayloadCustomer> properties) {
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
