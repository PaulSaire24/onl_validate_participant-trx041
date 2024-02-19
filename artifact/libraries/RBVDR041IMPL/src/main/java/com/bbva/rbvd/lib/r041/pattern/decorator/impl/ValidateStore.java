package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateStore implements PostValidate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateStore.class);
    private RBVDR048 rbvdr048;

    public ValidateStore(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    @Override
    public AgregarTerceroBO end(AgregarTerceroBO requestRimac, String quotationId, String productId, String traceId) {
        LOGGER.info("** end :: quotationId -> {}",quotationId);
        LOGGER.info("** end :: productId -> {}",productId);
        LOGGER.info("** end :: traceId -> {}",traceId);
        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);
        return consumerService.executeValidateParticipantRimacService(requestRimac,quotationId,productId,traceId);
    }
}
