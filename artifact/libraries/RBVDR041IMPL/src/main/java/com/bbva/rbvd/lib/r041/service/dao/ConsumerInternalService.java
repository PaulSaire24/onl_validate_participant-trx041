package com.bbva.rbvd.lib.r041.service.dao;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.lib.r048.RBVDR048;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ConsumerInternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerInternalService.class);

    private RBVDR048 rbvdr048;

    public ConsumerInternalService(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }
    public String executeKsmkCryptographyService(String customerId){
        LOGGER.info("***** ConsumerInternalService - executeKsmkCryptographyService Start *****");
        return rbvdr048.executeKsmkCryptography(customerId);
    }

    public PEWUResponse executeGetCustomerServiceByDocType(String documentNumber, String documentType) {
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService Start *****");
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService numDoc {} *****", documentNumber);
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService typeDoc {} *****", documentType);
        return rbvdr048.executeGetCustomerByDocType(documentNumber, documentType);
    }

    public ListBusinessesASO executeListBusinessService(String encryptedCustomerId){
        LOGGER.info("***** ConsumerInternalService - executeListBusinessService Start *****");
        return rbvdr048.executeListBusiness(encryptedCustomerId);
    }

    public QuotationCustomerDAO getQuotationDB(String quotationId) {
        LOGGER.info("***** ConsumerInternalService - getQuotationDB quotationId {}",quotationId);
        return rbvdr048.executeGetCustomerInformationFromQuotation(quotationId);
    }

   public QuotationLifeDAO getDataInsuredBD(String quotationId, String productId, String planId, String documentNumber, String documentType){
        LOGGER.info("***** ConsumerInternalService - getDataInsuredBD quotationId {}",quotationId);
        return rbvdr048.executeGetDataInsuredBD(quotationId,productId,planId,documentNumber,documentType);
   }

    public List<RolDAO> getRolesByCompanyDB(BigDecimal insuranceCompanyId) {
        LOGGER.info("***** ConsumerInternalService - getRolesByCompanyDB insuranceCompanyId {}",insuranceCompanyId);
        return rbvdr048.executeGetRolesByCompany(insuranceCompanyId);
    }

}

