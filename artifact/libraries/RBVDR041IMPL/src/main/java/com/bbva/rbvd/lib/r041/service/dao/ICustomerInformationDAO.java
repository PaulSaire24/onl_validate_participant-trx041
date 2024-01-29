package com.bbva.rbvd.lib.r041.service.dao;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;


public interface ICustomerInformationDAO {
    QuotationJoinCustomerInformationDTO getCustomerBasicInformation(String quotationId);
}

