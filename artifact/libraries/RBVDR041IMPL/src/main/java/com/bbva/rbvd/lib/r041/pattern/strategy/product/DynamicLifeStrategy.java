package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.impl.DynamicLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;

public class DynamicLifeStrategy implements StrategyProductHandler {

    @Override
    public AgregarTerceroBO addParticipantsToInsuranceCompany(PayloadConfig payloadConfig,RBVDR048 rbvdr048) {
        DynamicLifeProductBusinessImpl dynamicLifeParticipantBusiness = new DynamicLifeProductBusinessImpl(rbvdr048);
        return dynamicLifeParticipantBusiness.doDynamicLife(payloadConfig);
    }
}