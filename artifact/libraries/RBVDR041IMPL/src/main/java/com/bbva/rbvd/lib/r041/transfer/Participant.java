package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
public class Participant implements Cloneable{
    private String documentType;
    private String documentNumber;
    private String rolCode;
    private ParticipantsDTO inputParticipant;
    private PEWUResponse customer;
    private ListBusinessesASO legalCustomer;
    private InputNonCustomer inputNonCustomer;
    private NonCustomerFromDB nonCustomerFromDB;
    private LegalRepresentative legalRepresentative;

    public NonCustomerFromDB getNonCustomerFromDB() {
        return nonCustomerFromDB;
    }

    public void setNonCustomerFromDB(NonCustomerFromDB nonCustomerFromDB) {
        this.nonCustomerFromDB = nonCustomerFromDB;
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

    public InputNonCustomer getInputNonCustomer() {
        return inputNonCustomer;
    }

    public void setInputNonCustomer(InputNonCustomer inputNonCustomer) {
        this.inputNonCustomer = inputNonCustomer;
    }

    public LegalRepresentative getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(LegalRepresentative legalRepresentative) {
        this.legalRepresentative = legalRepresentative;
    }

    public Participant(String documentType, String documentNumber, String rolCode, ParticipantsDTO inputParticipant, PEWUResponse customer, NonCustomerFromDB nonCustomerFromDB, ListBusinessesASO legalCustomer, InputNonCustomer inputNonCustomer, LegalRepresentative legalRepresentative) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.rolCode = rolCode;
        this.inputParticipant = inputParticipant;
        this.customer = customer;
        this.nonCustomerFromDB = nonCustomerFromDB;
        this.inputNonCustomer = inputNonCustomer;
        this.legalCustomer = legalCustomer;
        this.legalRepresentative = legalRepresentative;
    }

    public Participant(){}

    @Override
    public Participant clone() {
        return new Participant(this.documentType, this.documentNumber, this.rolCode, this.inputParticipant, this.customer, this.nonCustomerFromDB, this.legalCustomer, this.inputNonCustomer, this.legalRepresentative);
    }
}
