package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerExternalService extends AbstractLibrary {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerExternalService.class);
    private RBVDR048 rbvdr048;

    public ConsumerExternalService(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public AgregarTerceroBO sendToApiAndValidateResponseError(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequest) {
        String quotationId = payloadConfig.getQuotationId();
        String productId = payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductType();
        String traceId = payloadConfig.getInput().getTraceId();
        String channelCode = payloadConfig.getInput().getChannelId();
        return executeValidateParticipantRimacService(rimacRequest, quotationId, productId, traceId, channelCode);
    }

    public AgregarTerceroBO executeValidateParticipantRimacService(AgregarTerceroBO payload,String quotationId,String productId,String traceId, String channelId) {
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService Start *****");
        AgregarTerceroBO output = new AgregarTerceroBO();
        try {
           output = rbvdr048.executeAddParticipants(payload,quotationId,productId,traceId,channelId);
            LOGGER.info("**** executeValidateParticipantRimacService successful execution");
        }catch(BusinessException ex){
            LOGGER.info("**** executeValidateParticipantRimacService failed execution");
            this.addAdviceWithDescription(ex.getAdviceCode(), ex.getMessage());
        }
        LOGGER.info("***** RBVDR041Impl - executeValidateParticipantRimacService  ***** Response Rimac: {}", output);
        return output;
    }

}
