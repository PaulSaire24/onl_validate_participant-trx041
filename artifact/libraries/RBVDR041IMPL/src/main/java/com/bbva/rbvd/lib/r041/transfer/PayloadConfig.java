package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;

import java.util.List;

public class PayloadConfig {

    private InputParticipantsDTO input;
    private List<Participant> participants;
    private String quotationId;
    private List<RolDAO> registeredRolesDB;
    private QuotationCustomerDAO quotationInformation;
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


    public List<RolDAO> getRegisteredRolesDB() {
        return registeredRolesDB;
    }

    public void setRegisteredRolesDB(List<RolDAO> registeredRolesDB) {
        this.registeredRolesDB = registeredRolesDB;
    }
    public QuotationCustomerDAO getQuotationInformation() {
        return quotationInformation;
    }

    public void setQuotationInformation(QuotationCustomerDAO quotationInformation) {
        this.quotationInformation = quotationInformation;
    }

    public ParticipantProperties getParticipantProperties() {
        return participantProperties;
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }
}
