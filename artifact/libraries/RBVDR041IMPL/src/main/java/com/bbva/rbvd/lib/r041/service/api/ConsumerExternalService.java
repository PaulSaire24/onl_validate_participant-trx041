package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerExternalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerExternalService.class);
    private PISDR352 pisdr352;

    public ConsumerExternalService(PISDR352 pisdr352) {
        this.pisdr352 = pisdr352;
    }

    public AgregarTerceroBO executeValidateParticipantRimacService(AgregarTerceroBO payload, String quotationId, String productId, String traceId){
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService Start *****");
        AgregarTerceroBO output = pisdr352.executeAddParticipantsService(payload, quotationId, productId, traceId);
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService  ***** Response Rimac: {}", output);
        return output;
    }
}
