package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;

import java.util.List;

public class PayloadConfig {

    private ValidateParticipantDTO input;
    private List<PayloadProperties> properties;
    private String quotationId;
    private String personType;
    private List<RolDTO> registeredRolesDB;
    private QuotationJoinCustomerInformationDTO quotationInformation;
    private ParticipantProperties participantProperties;
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

    public List<RolDTO> getRegisteredRolesDB() {
        return registeredRolesDB;
    }

    public void setRegisteredRolesDB(List<RolDTO> registeredRolesDB) {
        this.registeredRolesDB = registeredRolesDB;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public QuotationJoinCustomerInformationDTO getQuotationInformation() {
        return quotationInformation;
    }

    public void setQuotationInformation(QuotationJoinCustomerInformationDTO quotationInformation) {
        this.quotationInformation = quotationInformation;
    }

    public ParticipantProperties getParticipantProperties() {
        return participantProperties;
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }

    @Override
    public String toString() {
        return "PayloadConfig{" +
                "input=" + input +
                ", properties=" + properties +
                ", quotationId='" + quotationId + '\'' +
                ", personType='" + personType + '\'' +
                ", registeredRolesDB=" + registeredRolesDB +
                ", quotationInformation=" + quotationInformation +
                ", participantProperties=" + participantProperties +
                '}';
    }
}
