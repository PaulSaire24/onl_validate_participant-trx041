package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerExternalService extends AbstractLibrary {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerExternalService.class);
    private RBVDR048 rbvdr048;

    public ConsumerExternalService(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public AgregarTerceroBO executeValidateParticipantRimacService(AgregarTerceroBO payload, String quotationId, String productId, String traceId){
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService Start *****");
        AgregarTerceroBO output = new AgregarTerceroBO();
        try {
           output = rbvdr048.executeAddParticipants(payload, quotationId, productId, traceId);
            LOGGER.info("**** executeValidateParticipantRimacService successful execution");
        }catch(BusinessException ex){
            LOGGER.info("**** executeValidateParticipantRimacService failed execution");
            this.addAdviceWithDescription(ex.getAdviceCode(), ex.getMessage());
        }
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService  ***** Response Rimac: {}", output);
        return output;
    }

}
