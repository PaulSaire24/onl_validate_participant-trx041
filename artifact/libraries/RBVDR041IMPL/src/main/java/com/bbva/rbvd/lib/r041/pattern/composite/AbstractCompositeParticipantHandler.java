package com.bbva.rbvd.lib.r041.pattern.composite;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.enrichoperation.impl.EnrichPayloadProductImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.pattern.strategy.product.GeneralProductStrategy;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractCompositeParticipantHandler implements ParticipantHandler{

    private static final  Map<String, StrategyProductHandler> productHandlers = new HashMap<>();

    @Override
    public AgregarTerceroBO handleRequest(InputParticipantsDTO input, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService, ParticipantProperties participantProperties, QuotationCustomerDAO quotationInformation) {

        EnrichPayloadProductImpl enrichPayloadProduct = new EnrichPayloadProductImpl(participantProperties,rbvdr048);
        PayloadConfig payloadConfig = enrichPayloadProduct.enrichParticipantData(input,applicationConfigurationService,quotationInformation);

        StrategyProductHandler productHandler = productHandlers.get(quotationInformation.getInsuranceProduct().getInsuranceProductType());

        AgregarTerceroBO rimacRequest;
        if (Objects.nonNull(productHandler)) {
             rimacRequest =  productHandler.prepareCompanyRequest(payloadConfig,applicationConfigurationService);
        } else {
            StrategyProductHandler strategyProductHandler = new GeneralProductStrategy();
            rimacRequest = strategyProductHandler.prepareCompanyRequest(payloadConfig,applicationConfigurationService);
        }

        rimacRequest = enrichPayloadProduct.enrichRimacPayloadByProductAndParticipantType(rimacRequest, payloadConfig.getQuotationInformation(), payloadConfig.getParticipants());
        return new ConsumerExternalService(rbvdr048).sendToApiAndValidateResponseError(payloadConfig, rimacRequest);
    }

    protected static void addProductHandler(String productCode, StrategyProductHandler strategyProductHandler) {
        productHandlers.put(productCode, strategyProductHandler);
    }

}
