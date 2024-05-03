package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.impl.CrossProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;

public class GeneralProductStrategy implements StrategyProductHandler {

    @Override
    public AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        CrossProductBusinessImpl crossProductBusiness = new CrossProductBusinessImpl(rbvdr048,applicationConfigurationService);
        return crossProductBusiness.createRequestByCompany(payloadConfig);
    }
}
