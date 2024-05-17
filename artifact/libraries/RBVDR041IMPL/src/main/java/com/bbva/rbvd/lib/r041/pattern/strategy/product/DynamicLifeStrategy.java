package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.impl.DynamicLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class DynamicLifeStrategy implements StrategyProductHandler {

    @Override
    public AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, ApplicationConfigurationService applicationConfigurationService) {
        DynamicLifeProductBusinessImpl dynamicLifeParticipantBusiness = new DynamicLifeProductBusinessImpl(applicationConfigurationService);
        return dynamicLifeParticipantBusiness.doDynamicLife(payloadConfig);
    }
}