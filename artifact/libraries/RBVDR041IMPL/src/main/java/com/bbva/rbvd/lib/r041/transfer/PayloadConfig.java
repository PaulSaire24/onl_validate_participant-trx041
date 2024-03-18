package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.dto.participant.mapper.RolDTO;

import java.util.List;
import java.util.Map;

public class PayloadConfig {

    private InputParticipantsDTO input;
    private List<Participant> participants;
    private String quotationId;
    private Map<String,Object> dataInsuredBD;
    private String personType;
    private List<RolDTO> registeredRolesDB;
    private QuotationJoinCustomerInformationDTO quotationInformation;
    private ParticipantProperties participantProperties;

    public InputParticipantsDTO getInput() {
        return input;
    }

    public void setInput(InputParticipantsDTO input) {
        this.input = input;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
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
                ", properties=" + participants +
                ", quotationId='" + quotationId + '\'' +
                ", dataInsuredBD=" + dataInsuredBD +
                ", personType='" + personType + '\'' +
                ", registeredRolesDB=" + registeredRolesDB +
                ", quotationInformation=" + quotationInformation +
                ", participantProperties=" + participantProperties +
                '}';
    }
}
