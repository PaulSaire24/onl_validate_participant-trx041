package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;

public class PayloadProperties {
    private String documetType;
    private String documetNumber;
    private PEWUResponse customer;
    private ListBusinessesASO legalCustomer;

    public String getDocumetType() {
        return documetType;
    }

    public void setDocumetType(String documetType) {
        this.documetType = documetType;
    }

    public String getDocumetNumber() {
        return documetNumber;
    }

    public void setDocumetNumber(String documetNumber) {
        this.documetNumber = documetNumber;
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
                "documetType='" + documetType + '\'' +
                ", documetNumber='" + documetNumber + '\'' +
                ", customer=" + customer +
                ", legalCustomer=" + legalCustomer +
                '}';
    }
}
