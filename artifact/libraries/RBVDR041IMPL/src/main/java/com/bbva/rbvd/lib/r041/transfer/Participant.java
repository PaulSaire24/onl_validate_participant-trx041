package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;

public class Participant implements Cloneable{


    private String customerId;
    private String documentType;
    private String documentNumber;
    private String rolCode;
    private ParticipantsDTO inputParticipant;
    private PEWUResponse customer;
    private QuotationLifeDAO nonCustomerLife;
    private ListBusinessesASO legalCustomer;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public QuotationLifeDAO getNonCustomerLife() {
        return nonCustomerLife;
    }

    public void setNonCustomerLife(QuotationLifeDAO nonCustomerLife) {
        this.nonCustomerLife = nonCustomerLife;
    }

    public String getRolCode() {
        return rolCode;
    }

    public void setRolCode(String rolCode) {
        this.rolCode = rolCode;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public PEWUResponse getCustomer() {
        return customer;
    }

    public void setCustomer(PEWUResponse customer) {
        this.customer = customer;
    }

    public ListBusinessesASO getLegalCustomer() {
        return legalCustomer;
    }

    public void setLegalCustomer(ListBusinessesASO legalCustomer) {
        this.legalCustomer = legalCustomer;
    }

    public ParticipantsDTO getInputParticipant() {
        return inputParticipant;
    }

    public void setInputParticipant(ParticipantsDTO inputParticipant) {
        this.inputParticipant = inputParticipant;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "customerId='" + customerId + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", rolCode='" + rolCode + '\'' +
                ", inputParticipant=" + inputParticipant +
                ", customer=" + customer +
                ", nonCustomerLife=" + nonCustomerLife +
                ", legalCustomer=" + legalCustomer +
                '}';
    }

    public Participant(String customerId, String documentType, String documentNumber, String rolCode, ParticipantsDTO inputParticipant, PEWUResponse customer, QuotationLifeDAO nonCustomerLife, ListBusinessesASO legalCustomer) {
        this.customerId = customerId;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.rolCode = rolCode;
        this.inputParticipant = inputParticipant;
        this.customer = customer;
        this.nonCustomerLife = nonCustomerLife;
        this.legalCustomer = legalCustomer;
    }

    public Participant(){}

    @Override
    public Participant clone() {
       Participant participant = new Participant(this.customerId, this.documentType, this.documentNumber, this.rolCode, this.inputParticipant, this.customer, this.nonCustomerLife, this.legalCustomer);
       return participant;
    }
}
