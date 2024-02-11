package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateStore implements PostValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateStore.class);
    private PISDR352 pisdr352;

    public ValidateStore(PISDR352 pisdr352) {
        this.pisdr352 = pisdr352;
    }

    @Override
    public AgregarTerceroBO end(AgregarTerceroBO requestRimac, String quotationId, String productId, String traceId) {

        ConsumerExternalService consumerService = new ConsumerExternalService(pisdr352);
        return consumerService.executeValidateParticipantRimacService(requestRimac,quotationId,productId,traceId);

    }
}
