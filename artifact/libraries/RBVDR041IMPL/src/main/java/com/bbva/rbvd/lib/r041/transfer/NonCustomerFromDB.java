package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;

public class NonCustomerFromDB {
    private QuotationLifeDAO quotationLife;

    public QuotationLifeDAO getQuotationLife() {
        return quotationLife;
    }

    public void setQuotationLife(QuotationLifeDAO quotationLife) {
        this.quotationLife = quotationLife;
    }
}
