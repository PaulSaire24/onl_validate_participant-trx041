package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class ConsumerInternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerInternalService.class);

    private RBVDR048 rbvdr048;

    public ConsumerInternalService(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public PEWUResponse executeGetCustomerService(String numDoc, String typeDoc){
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService Start *****");
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService numDoc {} *****", numDoc);
        LOGGER.info("***** ConsumerInternalService - executeGetCustomerService typeDoc {} *****", typeDoc);
        return  rbvdr048.executeGetCustomerService(numDoc,typeDoc);

    }

   public Map<String,Object> getDataInsuredBD(String quotationId,String productId,String planId){
        LOGGER.info("***** ConsumerInternalService - getDataInsuredBD quotationId {}",quotationId);
        return rbvdr048.executeGetDataInsuredBD(quotationId,productId,planId);
   }

    public Map<String,Object> getProducAndPlanByQuotation(String quotationId){
        LOGGER.info("***** ConsumerInternalService - getDataInsuredBD quotationId {}",quotationId);
        return rbvdr048.executeGetProducAndPlanByQuotation(quotationId);
    }
}
