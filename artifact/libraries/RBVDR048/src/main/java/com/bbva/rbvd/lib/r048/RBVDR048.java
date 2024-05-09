package com.bbva.rbvd.lib.r048;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;

import java.math.BigDecimal;
import java.util.List;

public interface RBVDR048 {
	AgregarTerceroBO executeAddParticipants(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId,String channelId);
    PEWUResponse executeGetCustomerByDocType(String documentNumber, String documentType);
    String executeKsmkCryptography(String customerId);
    ListBusinessesASO executeListBusiness(String encryptedCustomerId);
    QuotationLifeDAO executeGetDataInsuredBD(String quotationId, String productId, String planId, String ducumentNumber, String documentType);
    QuotationCustomerDAO executeGetCustomerInformationFromQuotation(String quotationId);
    List<RolDAO> executeGetRolesByCompany(BigDecimal insuranceCompanyId);

}
