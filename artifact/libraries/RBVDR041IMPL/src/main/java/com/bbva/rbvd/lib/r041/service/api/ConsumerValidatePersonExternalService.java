package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ConsumerValidatePersonExternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPersonInternalService.class);
    private PISDR352 pisdr352;

    public AgregarTerceroBO executeValidateParticipantRimacService(AgregarTerceroBO payload, String quotationId, String productId, String traceId){
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService Start *****");
        AgregarTerceroBO output = pisdr352.executeAddParticipantsService(payload, quotationId, productId, traceId);
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService  ***** Response Rimac: {}", output);
        return output;
    }

    public void setPisdr352(PISDR352 pisdr352) {
        this.pisdr352 = pisdr352;
    }
}
