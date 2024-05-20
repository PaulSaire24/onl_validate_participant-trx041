package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.impl.DynamicLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class DynamicLifeStrategy implements StrategyProductHandler {

    @Override
    public AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        DynamicLifeProductBusinessImpl dynamicLifeParticipantBusiness = new DynamicLifeProductBusinessImpl();
        return dynamicLifeParticipantBusiness.doDynamicLife(payloadConfig, rimacRequestData );
    }
}