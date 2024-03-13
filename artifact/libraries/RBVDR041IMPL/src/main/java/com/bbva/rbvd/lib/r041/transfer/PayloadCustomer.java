package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;

public class PayloadCustomer {

    private String customerId;
    private String documentType;
    private String documentNumber;
    private PEWUResponse customer;
    private ListBusinessesASO legalCustomer;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
        return "PayloadProperties{" +
                "customerId='" + customerId + '\'' +
                ", documetType='" + documentType + '\'' +
                ", documetNumber='" + documentNumber + '\'' +
                ", customer=" + customer +
                ", legalCustomer=" + legalCustomer +
                '}';
    }
}
