package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;

import java.util.Map;

public class Participant {

    private String customerId;
    private String documentType;
    private String documentNumber;
    private String rolCode;
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

    @Override
    public String toString() {
        return "Participant{" +
                "customerId='" + customerId + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", rolId='" + rolCode + '\'' +
                ", customer=" + customer +
                ", nonCustomer=" + nonCustomerLife +
                ", legalCustomer=" + legalCustomer +
                '}';
    }
}
